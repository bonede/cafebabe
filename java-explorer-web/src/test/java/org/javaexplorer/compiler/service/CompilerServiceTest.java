package org.javaexplorer.compiler.service;

import lombok.extern.slf4j.Slf4j;
import org.javaexplorer.model.SrcFile;
import org.javaexplorer.model.vo.CompileOutput;
import org.javaexplorer.model.vo.CompileReq;
import org.javaexplorer.utils.JsonUtils;
import org.javaexplorer.utils.ResourcesUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Arrays;

@SpringBootTest(properties = {"compiler.workingDir=javac"})
@Slf4j
class CompilerServiceTest {
    @Autowired
    private CompilerService compilerService;
    private String javaFileName = "Main.java";

    @Test
    void explore() throws IOException {
        byte[] javaFileBytes = ResourcesUtils.readResourceToBytes(this.getClass(), "source/Main.java");
        SrcFile srcFile = new SrcFile();
        srcFile.setPath("Main.java");
        srcFile.setContent(new String(javaFileBytes));
        CompileReq compileReq = new CompileReq();
        compileReq.setCompilerName("openjdk8");
        compileReq.setSrcFiles(Arrays.asList(srcFile));
        CompileOutput result = compilerService.compile(compileReq);
        log.info("result {}", JsonUtils.toJson(result));
    }
}