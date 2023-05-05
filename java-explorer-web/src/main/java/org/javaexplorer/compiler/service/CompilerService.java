package org.javaexplorer.compiler.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.javaexplorer.bytecode.classimage.ClassImage;
import org.javaexplorer.error.ApiException;
import org.javaexplorer.model.ClassFile;
import org.javaexplorer.model.SrcFile;
import org.javaexplorer.model.vo.CompileOutput;
import org.javaexplorer.model.vo.CompileReq;
import org.javaexplorer.utils.CommandUtils;
import org.javaexplorer.web.service.AppService.AppConfig.CompilerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.javaexplorer.web.service.AppService.AppConfig;

@Service
@Slf4j
public class CompilerService {
    @Autowired
    private AppConfig appConfig;

    @PostMapping
    public void init() throws IOException {
        Path workingDir = Paths.get(appConfig.getWorkingDir());
        if(Files.exists(workingDir)){
            Files.createDirectories(workingDir);
        }
    }

    private Path genWorkingDir(){
        return Paths.get(appConfig.getWorkingDir(), RandomStringUtils.randomAlphanumeric(6));
    }

    /**
     * 保存文件
     * @param srcFiles
     * @return 工作文件夹
     * @throws IOException
     */
    private Path saveSrcFiles(List<SrcFile> srcFiles) throws IOException {
        Path workingDir = genWorkingDir();
        Files.createDirectories(workingDir);
        srcFiles.forEach(javaFile -> {
            Path filePath = workingDir.resolve(javaFile.getPath());
            try(OutputStream outputStream = Files.newOutputStream(filePath)) {
                IOUtils.write(javaFile.getContent(), outputStream, "utf-8");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        Files.createDirectories(workingDir.resolve(appConfig.getBuildDir()));
        return workingDir;
    }

    public boolean isClassFile(Path path){
        return path.getFileName().toString().endsWith(".class") && path.toFile().isFile();
    }

    public List<ClassFile> collectClassFile(Path workingDir) throws IOException {
        try (Stream<Path> stream = Files.walk(workingDir)){
            return stream.filter(this::isClassFile)
                    .map(this::toClassFile)
                    .sorted(Comparator.comparing(ClassFile::getPath).reversed())
                    .collect(Collectors.toList());
        }
    }

    private ClassFile toClassFile(Path classFilePath)  {
        try(
            FileInputStream fileInputStream = new FileInputStream(classFilePath.toFile());
        ) {
            ClassFile classFile = new ClassFile();
            classFile.setContent(IOUtils.toByteArray(fileInputStream));
            Path pathWithBuildDir = genWorkingDir().relativize(classFilePath);
            Path pathWithoutBuildDir = pathWithBuildDir.subpath(3, pathWithBuildDir.getNameCount());
            classFile.setPath(pathWithoutBuildDir.toString());
            classFile.setClassImage(new ClassImage(classFile.getContent()));
            fileInputStream.close();
            return classFile;
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private String formatCmd(
            CompilerConfig compilerConfig,
                             String workingDir,
                             String compilerOptions,
                             List<SrcFile> srcFiles
    ){
        String srcFileParameter = srcFiles.stream()
                .map(SrcFile::getPath)
                .collect(Collectors.joining(" "));
        String cmd = compilerConfig.getCmd()
                .replace("{OPTS}", compilerOptions == null ? "" : compilerOptions)
                .replace("{SRC_FILES}", srcFileParameter);
        if(!appConfig.isUsingDocker()){
            return cmd;
        }
        return appConfig.getDockerCmd()
                .replace("{WD}", workingDir)
                .replace("{IMG}", compilerConfig.getImg())
                .replace("{CMD}", cmd);
    }


    public CompileOutput compile(CompileReq compileReq) throws IOException {
        CompilerConfig compiler = appConfig.getCompilers().stream()
                .filter(c -> c.getName().equals(compileReq.getOps().getCompilerName()))
                .findFirst()
                .orElseThrow(() -> ApiException.error("Invalid compiler"));

        Path workingDir = saveSrcFiles(compileReq.getSrcFiles());
        String cmdArgs = compiler.getDebugAndOptimizeArgs(compileReq.getOps().getDebug(), compileReq.getOps().getOptimize());
        String cmd = formatCmd(compiler, workingDir.toString(), cmdArgs, compileReq.getSrcFiles());
        log.info("CMD {}", cmd);
        CommandUtils.CommandResult result = CommandUtils.run(workingDir.toFile(), cmd.split(" "));
        if(result.getCode() == 0){
            List<ClassFile> classFiles = collectClassFile(workingDir);
            CompileOutput compileOutput = CompileOutput.success(
                    classFiles,
                    result.getStdout(),
                    result.getStderr(),
                    compileReq.getOps()
            );
            FileSystemUtils.deleteRecursively(workingDir.toFile());
            return compileOutput;
        }else {
            FileSystemUtils.deleteRecursively(workingDir.toFile());
            return CompileOutput.fail(
                    result.getCode(),
                    result.getStdout(),
                    result.getStderr(),
                    compileReq.getOps()
            );
        }
    }



}
