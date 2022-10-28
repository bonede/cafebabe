package org.javaexplorer.web.service;


import org.javaexplorer.model.vo.WebAppInfo;
import org.javaexplorer.web.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppService {
    @Autowired
    private AppConfig appConfig;

    public WebAppInfo getAppInfo(){
        WebAppInfo appInfo = new WebAppInfo();

        return appInfo;
    }
}
