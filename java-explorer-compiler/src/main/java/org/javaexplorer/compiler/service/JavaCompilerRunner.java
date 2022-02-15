package org.javaexplorer.compiler.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.javaexplorer.compiler.model.CompilerConfig;
import org.javaexplorer.model.ClassFile;
import org.javaexplorer.model.JavaFile;
import org.javaexplorer.model.vo.CompileResult;
import org.javaexplorer.utils.CommandUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JavaCompilerRunner extends CompilerRunner {

    @Autowired
    private CompilerConfig compilerConfig;

    private String compilerVersion;

    private static final String COMPILER = "javac";
    private static final String SRC_DIR = "src";
    private static final String DST_DIR = "build";
    private static final String[] SUPPORT_COMPILER_NICKNAMES = {
            "openjdk8",
            "openjdk11"
    };

    @PostConstruct
    public void init(){
        CommandUtils.CommandResult commandResult = CommandUtils.run(
                getCompiler(),
                "-version"
        );
        if(!commandResult.success()){
            throw new RuntimeException(commandResult.toString());
        }
        compilerVersion = commandResult.getOutput().trim();
    }

    @Override
    List<String> getSupportCompilerNicknames() {
        return Arrays.asList(SUPPORT_COMPILER_NICKNAMES);
    }

    @Override
    public String getCompilerVersion(){

        return compilerVersion;
    }

    @Override
    public String getCompiler() {
        return COMPILER;
    }

    @Override
    String sanitizeCompilerOptions(String compilerOptions) {
        // TODO implement
        return compilerOptions;
    }

    private List<Path> createSourceFiles(Path srcDir, List<JavaFile> javaFiles) throws IOException {
        List<Path> javaFilePaths = new ArrayList<>();
        for(JavaFile javaFile : javaFiles){
            Path javaFilePath = srcDir.resolve(javaFile.getPath());
            Files.createDirectories(javaFilePath.getParent());
            copyJavaFile(javaFile, javaFilePath);
            javaFilePaths.add(javaFilePath);
        }
        return javaFilePaths;
    }

    private boolean isClassFile(String fileName){
        return "class".equals(FilenameUtils.getExtension(fileName));
    }

    private List<ClassFile> collectClassFile(Path dstDir) throws IOException {
        return Files.walk(dstDir)
                .filter(p -> p.toFile().isFile())
                .map(p -> toClassFile(dstDir, p))
                .collect(Collectors.toList());
    }

    ClassFile toClassFile(Path dstDir, Path classFilePath){
        try(
             FileInputStream fileInputStream = new FileInputStream(classFilePath.toFile());
        ) {
            byte[] classFileBytes = IOUtils.toByteArray(fileInputStream);
            String classFileBase64 = Base64.encodeBase64String(classFileBytes);
            ClassFile clsFile = new ClassFile();
            clsFile.setContent(classFileBase64);
            clsFile.setSize(Files.size(classFilePath));
            clsFile.setPath(dstDir.relativize(classFilePath).toString());
            return clsFile;
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public void copyJavaFile(JavaFile javaFile, Path javaFilePath) throws IOException {
        ByteArrayInputStream javaFileInputStream = new ByteArrayInputStream(javaFile.getContent().getBytes("utf-8"));
        FileOutputStream javaFileOutputStream = new FileOutputStream(javaFilePath.toFile());
        IOUtils.copy(
                javaFileInputStream,
                javaFileOutputStream
        );
        javaFileOutputStream.close();
        javaFileInputStream.close();
    }

    private String sourceFileArgs(List<Path> sourceFilePaths){
        return sourceFilePaths.stream()
                .map(p -> p.toString())
                .collect(Collectors.joining(" "));
    }

    @Override
    CompileResult compileWithRawCompilerOptions(List<JavaFile> javaFiles, String compilerOptions) throws IOException {
        Path tempDir = createTempleDir();
        Path srcDir = Files.createDirectories(tempDir.resolve(SRC_DIR));
        Path dstDir = Files.createDirectories(tempDir.resolve(DST_DIR));
        List<Path> sourceFilePaths = createSourceFiles(srcDir, javaFiles);
        String sourceFileArgs = sourceFileArgs(sourceFilePaths);
        CommandUtils.CommandResult commandResult = CommandUtils.run(
                COMPILER,
                    "-J-Duser.language=en",
                "-d",
                dstDir.toString(),
                sourceFileArgs
        );
        CompileResult compileResult;
        if(commandResult.getCode() != 0){
            FileUtils.deleteDirectory(srcDir.toFile());
            FileUtils.deleteDirectory(dstDir.toFile());
            compileResult = CompileResult.fail(
                    commandResult.getOutput(),
                    COMPILER,
                    compilerOptions,
                    compilerConfig.getNickname(),
                    getCompilerVersion()
            );
        }else{
            List<ClassFile> classFiles = collectClassFile(dstDir);
            compileResult = CompileResult.success(
                    classFiles,
                    COMPILER,
                    compilerOptions,
                    compilerConfig.getNickname(),
                    getCompilerVersion()
            );
        }
        FileUtils.deleteDirectory(srcDir.toFile());
        FileUtils.deleteDirectory(dstDir.toFile());
        return compileResult;
    }
}
