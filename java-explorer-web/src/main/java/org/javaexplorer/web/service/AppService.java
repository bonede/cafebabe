package org.javaexplorer.web.service;


import org.javaexplorer.model.vo.AppInfo;
import org.javaexplorer.web.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppService {
    @Autowired
    private AppConfig appConfig;

    public AppInfo getAppInfo(){
        AppInfo appInfo = new AppInfo();

        return appInfo;
    }
}
