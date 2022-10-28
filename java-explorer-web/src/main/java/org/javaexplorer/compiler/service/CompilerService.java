package org.javaexplorer.compiler.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.javaexplorer.config.CompilerConfig;
import org.javaexplorer.model.ClassFile;
import org.javaexplorer.model.SrcFile;
import org.javaexplorer.model.vo.CompileResult;
import org.javaexplorer.utils.CommandUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CompilerService {
    @Autowired
    private CompilerConfig compilerConfig;

    @PostMapping
    public void init() throws IOException {
        Path workingDir = Paths.get(compilerConfig.getWorkingDir());
        if(Files.exists(workingDir)){
            Files.createDirectories(workingDir);
        }
    }

    private Path genWorkingDir(){
        return Paths.get(compilerConfig.getWorkingDir(), RandomStringUtils.randomAlphanumeric(6));
    }

    /**
     * 保存文件
     * @param srcFiles
     * @return 工作文件夹
     * @throws IOException
     */
    private Path saveFile(List<SrcFile> srcFiles) throws IOException {
        Path workingDir = genWorkingDir();
        Files.createDirectories(workingDir);
        srcFiles.forEach(javaFile -> {
            Path filePath = workingDir.resolve(javaFile.getPath());
            try(OutputStream outputStream = new FileOutputStream(filePath.toFile());) {
                IOUtils.write(javaFile.getContent(), outputStream, "utf-8");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        Files.createDirectories(workingDir.resolve(compilerConfig.getBuildDir()));
        return workingDir;
    }

    public List<ClassFile> collectClassFile(Path workingDir) throws IOException {
        return Files.walk(workingDir)
                .filter(p -> p.toFile().isFile())
                .map(p -> {
                    try {
                        return toClassFile(workingDir, p);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    private ClassFile toClassFile(Path workingDir, Path classFilePath) throws IOException {
        try(
            FileInputStream fileInputStream = new FileInputStream(classFilePath.toFile());
        ) {
            byte[] classFileBytes = IOUtils.toByteArray(fileInputStream);
            String classFileBase64 = Base64.encodeBase64String(classFileBytes);
            ClassFile classFile = new ClassFile();
            classFile.setContent(classFileBase64);
            classFile.setSize(Files.size(classFilePath));
            classFile.setPath(workingDir.relativize(classFilePath).toString());
            return classFile;
        }
    }

    private String formatCmd(String cmd,
                             String workingDir,
                             String compilerOptions,
                             List<SrcFile> srcFiles
    ){
        String srcFileParameter = srcFiles.stream()
                .map(s -> s.getPath())
                .collect(Collectors.joining(" "));
        return cmd.replace("{WORKING_DIR}", workingDir)
                .replace("{OPTS}", compilerOptions == null ? "" : compilerOptions)
                .replace("{SRC_FILES}", srcFileParameter);
    }


    public CompileResult compile(
            String compilerName,
            String compilerOptions,
            List<SrcFile> srcFiles
    ) throws IOException {
        CompilerConfig.Compiler compiler = compilerConfig.getCompilers().stream()
                .filter(c -> c.getName().equals(compilerName))
                .findFirst()
                .get();

        Path workingDir = saveFile(srcFiles);
        String cmd = formatCmd(compiler.getCmd(), workingDir.toString(), compilerOptions, srcFiles);
        CommandUtils.CommandResult result = CommandUtils.run(workingDir.toFile(), cmd.split(" "));

        if(result.getCode() == 0){
            CompileResult compileResult = CompileResult.success(collectClassFile(workingDir), result.getOutput(), compilerName, compilerOptions);
            FileUtils.deleteDirectory(workingDir.toFile());
            return compileResult;
        }else {
            FileUtils.deleteDirectory(workingDir.toFile());
            return CompileResult.fail(result.getCode(), result.getOutput(), compilerName, compilerOptions);
        }

    }
}
