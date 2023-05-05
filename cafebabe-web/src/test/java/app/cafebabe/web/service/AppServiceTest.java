package app.cafebabe.web.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;
@SpringBootTest
@Slf4j
class AppServiceTest {
    @Autowired
    private AppService appService;
    @Test
    void getAppInfo() {
        assertTrue(appService.getAppInfo().getCompilers().size() > 0);
    }
}