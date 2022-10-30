package org.javaexplorer.web.service;

import lombok.extern.slf4j.Slf4j;
import org.javaexplorer.model.SrcFile;
import org.javaexplorer.model.vo.ExplorerReq;
import org.javaexplorer.model.vo.ExplorerResult;
import org.javaexplorer.utils.JsonUtils;
import org.javaexplorer.utils.ResourcesUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Arrays;
@SpringBootTest
@Slf4j
class ExplorerServiceTest {
    @Autowired
    private ExplorerService explorerService;


    @Test
    void explore() throws IOException {
        byte[] javaFileBytes = ResourcesUtils.readResourceToBytes(this.getClass(), "source/Main.java");
        SrcFile srcFile = new SrcFile();
        srcFile.setPath("Main.java");
        srcFile.setSize(javaFileBytes.length);
        srcFile.setContent(new String(javaFileBytes));
        ExplorerReq explorerReq = new ExplorerReq();
        explorerReq.setCompilerName("openjdk8");
        explorerReq.setSave(false);
        explorerReq.setSrcFiles(Arrays.asList(srcFile));
        ExplorerResult result = explorerService.explore(explorerReq);
        log.info("result {}", JsonUtils.toJson(result));
    }
}