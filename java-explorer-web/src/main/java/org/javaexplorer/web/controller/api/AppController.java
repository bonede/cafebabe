package org.javaexplorer.web.controller.api;

import org.javaexplorer.model.vo.ApiResp;
import org.javaexplorer.model.vo.AppInfo;
import org.javaexplorer.web.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app")
@Validated
@CrossOrigin(originPatterns = {"http://localhost:5173/"})
public class AppController {
    @Autowired
    private AppService appService;

    @GetMapping("/info")
    public ApiResp<AppInfo> getInfo(){
        return ApiResp.ok(appService.getAppInfo());
    }
}
