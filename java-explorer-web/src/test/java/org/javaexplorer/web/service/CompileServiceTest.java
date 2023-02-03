package org.javaexplorer.web.service;

import lombok.extern.slf4j.Slf4j;
import org.javaexplorer.model.SrcFile;
import org.javaexplorer.model.vo.CompileReq;
import org.javaexplorer.model.vo.CompileResult;
import org.javaexplorer.utils.JsonUtils;
import org.javaexplorer.utils.ResourcesUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Arrays;
@SpringBootTest
@Slf4j
class CompileServiceTest {
    @Autowired
    private CompileService compileService;


    @Test
    void explore() throws IOException {
        byte[] javaFileBytes = ResourcesUtils.readResourceToBytes(this.getClass(), "source/Main.java");
        SrcFile srcFile = new SrcFile();
        srcFile.setPath("Main.java");
        srcFile.setContent(new String(javaFileBytes));
        CompileReq compileReq = new CompileReq();
        compileReq.setCompilerName("openjdk8");
        compileReq.setSrcFiles(Arrays.asList(srcFile));
        CompileResult result = compileService.explore(compileReq);
        log.info("result {}", JsonUtils.toJson(result));
    }
}