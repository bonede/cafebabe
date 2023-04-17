package org.javaexplorer.web.service;


import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.validator.constraints.URL;
import org.javaexplorer.bytecode.op.InstructionDoc;
import org.javaexplorer.bytecode.op.JdkVersion;
import org.javaexplorer.compiler.service.CompilerService;
import org.javaexplorer.model.vo.AppInfo;
import org.javaexplorer.model.vo.CompilerInfo;
import org.javaexplorer.utils.ResourcesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AppService {
    private static AppInfo appInfo = null;
    @Autowired
    private AppConfig appConfig;

    @Autowired
    private CompilerService.CompilerConfig compilerConfig;

    public AppInfo getAppInfo(){
        if(appInfo != null){
            return appInfo;
        }
        appInfo = new AppInfo();
        appInfo.setCompilers(compilerConfig.getCompilers().stream().map( c -> {
            CompilerInfo compilerInfo = new CompilerInfo();
            compilerInfo.setName(c.getName());
            compilerInfo.setLang(c.getLang());
            compilerInfo.setFileName(FilenameUtils.getName(c.getExample()));
            try {
                compilerInfo.setExample(ResourcesUtils.readResourceToString(this.getClass(), c.getExample()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return compilerInfo;
        }).collect(Collectors.toList()));
        appInfo.setInstructionDocs(appConfig.getInstructionDocs());
        appInfo.setVersions(appConfig.getVersions());
        appInfo.setSpecUrl(appConfig.getSpecUrl());
        return appInfo;
    }

    @Component
    @ConfigurationProperties(prefix = "app")
    @Data
    @Validated
    public static class AppConfig {
        /**
         * Web url.
         */
        @URL
        @NotNull
        private String url;
        @NotNull
        private String specUrl;
        @NotNull
        private Map<String, InstructionDoc> instructionDocs;
        @NotNull
        private Map<String, JdkVersion> versions;
    }
}
