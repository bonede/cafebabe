package org.javaexplorer.web.service;

import org.javaexplorer.model.JavaFile;
import org.javaexplorer.web.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StorageService {
    @Autowired
    private AppConfig appConfig;

    public String store(
            String compilerNickname,
            String compilerOptions,
            List<JavaFile> javaFiles,
            List<Map<String, Object>> disassembledClassFiles
    ){
        // TODO implement
        return "foo";
    }

    public String storeWithUrl(String compilerNickname,
                               String compilerOptions,
                               List<JavaFile> javaFiles,
                               List<Map<String, Object>> disassembledClassFiles){
        String storeId = store(compilerNickname, compilerOptions, javaFiles, disassembledClassFiles);
        return getStoreUrl(storeId);
    }

    public String getStoreUrl(String storeId){
        return appConfig.getUrl() + "/share/" + storeId;
    }
}
