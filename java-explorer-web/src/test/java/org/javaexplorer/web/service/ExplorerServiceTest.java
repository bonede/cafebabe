package org.javaexplorer.web.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.javaexplorer.model.ClassFile;
import org.javaexplorer.model.JavaFile;
import org.javaexplorer.model.vo.*;
import org.javaexplorer.utils.ResourcesUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
@SpringBootTest
@Slf4j
class ExplorerServiceTest {
    @Autowired
    private ExplorerService explorerService;

    @Test
    void compile() throws IOException {
        byte[] javaFileBytes = ResourcesUtils.readResourceToBytes(this.getClass(), "source/Main.java");
        JavaFile javaFile = new JavaFile();
        javaFile.setPath("Main.java");
        javaFile.setSize(javaFileBytes.length);
        javaFile.setContent(new String(javaFileBytes));
        CompileReq compileReq = new CompileReq();
        compileReq.setCompilerNickname("openjdk8");
        compileReq.setJavaFiles(Arrays.asList(
               javaFile
        ));
        ApiResp<CompileResult> result = explorerService.compile(compileReq);
        log.info("result {}", result);
    }

    @Test
    void disassemble() throws IOException {
        byte[] classFileBytes = ResourcesUtils.readResourceToBytes(this.getClass(), "source/Main.class");
        ClassFile classFile = new ClassFile();
        classFile.setContent(Base64.encodeBase64String(classFileBytes));
        classFile.setPath("Main.class");
        classFile.setSize(classFileBytes.length);
        DisassembleReq disassembleReq = new DisassembleReq();
        disassembleReq.setClassFiles(Arrays.asList(classFile));
        ApiResp<Map<String, Object>> result = explorerService.disassemble(disassembleReq);
        log.info("result {}", result);
    }

    @Test
    void explore() throws IOException {
        byte[] javaFileBytes = ResourcesUtils.readResourceToBytes(this.getClass(), "source/Main.java");
        JavaFile javaFile = new JavaFile();
        javaFile.setPath("Main.java");
        javaFile.setSize(javaFileBytes.length);
        javaFile.setContent(new String(javaFileBytes));
        ExplorerReq explorerReq = new ExplorerReq();
        explorerReq.setCompilerNickname("openjdk8");
        explorerReq.setSave(false);
        explorerReq.setJavaFiles(Arrays.asList(javaFile));
        ExplorerResult result = explorerService.explore(explorerReq);
        log.info("result {}", result);
    }
}