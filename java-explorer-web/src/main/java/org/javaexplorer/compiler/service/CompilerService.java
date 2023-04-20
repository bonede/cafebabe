package org.javaexplorer.compiler.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.javaexplorer.bytecode.vm.ClassImage;
import org.javaexplorer.error.ApiException;
import org.javaexplorer.model.ClassFile;
import org.javaexplorer.model.SrcFile;
import org.javaexplorer.model.vo.CompileOutput;
import org.javaexplorer.model.vo.CompileReq;
import org.javaexplorer.utils.CommandUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;
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
                .filter(p -> p.toFile().isFile() && p.toFile().getName().endsWith(".class"))
                .map(p -> {
                    try {
                        return toClassFile(workingDir, p);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .sorted(Comparator.comparing(ClassFile::getPath).reversed())
                .collect(Collectors.toList());
    }

    private ClassFile toClassFile(Path workingDir, Path classFilePath) throws IOException {
        try(
            FileInputStream fileInputStream = new FileInputStream(classFilePath.toFile());
        ) {
            ClassFile classFile = new ClassFile();
            classFile.setContent(IOUtils.toByteArray(fileInputStream));
            Path pathWithBuildDir = workingDir.relativize(classFilePath);
            Path pathWithoutBuildDir = pathWithBuildDir.subpath(1, pathWithBuildDir.getNameCount());
            classFile.setPath(pathWithoutBuildDir.toString());
            classFile.setClassImage(new ClassImage(classFile.getContent()));
            return classFile;
        }
    }

    private String formatCmd(String cmd,
                             String workingDir,
                             String compilerOptions,
                             List<SrcFile> srcFiles
    ){
        String srcFileParameter = srcFiles.stream()
                .map(SrcFile::getPath)
                .collect(Collectors.joining(" "));
        return cmd.replace("{WORKING_DIR}", workingDir)
                .replace("{OPTS}", compilerOptions == null ? "" : compilerOptions)
                .replace("{SRC_FILES}", srcFileParameter);
    }


    public CompileOutput compile(CompileReq compileReq) throws IOException {
        CompilerConfig.Compiler compiler = compilerConfig.getCompilers().stream()
                .filter(c -> c.getName().equals(compileReq.getOps().getCompilerName()))
                .findFirst()
                .orElseThrow(() -> ApiException.error("Invalid compiler"));

        Path workingDir = saveFile(compileReq.getSrcFiles());
        String cmdArgs = compiler.getDebugAndOptimizeArgs(compileReq.getOps().getDebug(), compileReq.getOps().getOptimize());
        String cmd = formatCmd(compiler.getCmd(), workingDir.toString(), cmdArgs, compileReq.getSrcFiles());
        CommandUtils.CommandResult result = CommandUtils.run(workingDir.toFile(), cmd.split(" "));
        if(result.getCode() == 0){
            List<ClassFile> classFiles = collectClassFile(workingDir);
            CompileOutput compileOutput = CompileOutput.success(
                    classFiles,
                    result.getStdout(),
                    result.getStderr(),
                    compileReq.getOps()
            );
            FileUtils.deleteDirectory(workingDir.toFile());
            return compileOutput;
        }else {
            FileUtils.deleteDirectory(workingDir.toFile());
            return CompileOutput.fail(
                    result.getCode(),
                    result.getStdout(),
                    result.getStderr(),
                    compileReq.getOps()
            );
        }

    }

    @ConfigurationProperties("compiler")
    @Component
    @Data
    public static class CompilerConfig {
        private String workingDir;
        private String buildDir;
        private List<Compiler> compilers;
        @Data
        public static class Compiler{
            private String name;
            private String lang;
            private String cmd;
            private String example;
            private String debugArgs;
            private String optimizeArgs;

            public String getDebugAndOptimizeArgs(boolean debug, boolean optimize){
                StringJoiner stringJoiner = new StringJoiner(" ");
                if(debug){
                    stringJoiner.add(debugArgs);
                }
                if(optimize){
                    stringJoiner.add(optimizeArgs);
                }
                return stringJoiner.toString();
            }
        }
    }
}
