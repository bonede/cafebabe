package org.javaexplorer.web.service;


import org.javaexplorer.config.CompilerConfig;
import org.javaexplorer.model.vo.AppInfo;
import org.javaexplorer.model.vo.CompilerInfo;
import org.javaexplorer.utils.ResourcesUtils;
import org.javaexplorer.web.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.stream.Collectors;

@Service
public class AppService {
    private static AppInfo appInfo = null;
    @Autowired
    private AppConfig appConfig;

    @Autowired
    private CompilerConfig compilerConfig;

    public AppInfo getAppInfo(){
        if(appInfo != null){
            return appInfo;
        }
        appInfo = new AppInfo();
        appInfo.setCompilers(compilerConfig.getCompilers().stream().map( c -> {
            CompilerInfo compilerInfo = new CompilerInfo();
            compilerInfo.setName(c.getName());
            try {
                compilerInfo.setExample(ResourcesUtils.readResourceToString(this.getClass(), c.getExample()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return compilerInfo;
        }).collect(Collectors.toList()));
        return appInfo;
    }
}
