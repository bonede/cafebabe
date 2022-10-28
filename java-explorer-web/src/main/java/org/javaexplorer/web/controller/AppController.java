package org.javaexplorer.web.controller;

import org.javaexplorer.model.vo.ApiResp;
import org.javaexplorer.model.vo.WebAppInfo;
import org.javaexplorer.web.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
@Validated
public class AppController {
    @Autowired
    private AppService appService;

    @GetMapping("/info")
    public ApiResp<WebAppInfo> getInfo(){
        return ApiResp.ok(appService.getAppInfo());
    }
}
