package app.cafebabe.web.service;

import app.cafebabe.model.SrcFile;
import app.cafebabe.model.vo.CreateShareReq;
import app.cafebabe.model.vo.ShareResp;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Arrays;

@SpringBootTest
@Slf4j
class ShareFileServiceTest {
    @Autowired
    private ShareFileService shareFileService;


    @Test
    void share() {
        CreateShareReq req = new CreateShareReq();
        SrcFile srcFile = new SrcFile();
        srcFile.setContent("foobar");
        srcFile.setPath("foobar.java");
        req.setHoursToLive(1);
        req.setSrcFiles(Arrays.asList(srcFile));
        ShareResp resp = shareFileService.share(req, new MockHttpServletRequest());
        log.info("{}", resp);
        log.info("{}", shareFileService.findById(resp.getId()));
    }
}