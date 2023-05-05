package app.cafebabe.web.service;

import app.cafebabe.model.SrcFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StorageService {
    @Autowired
    private AppService.AppConfig appConfig;

    public String store(
            String compilerNickname,
            String compilerOptions,
            List<SrcFile> srcFiles,
            List<Map<String, Object>> disassembledClassFiles
    ){
        // TODO implement
        return "foo";
    }

    public String storeWithUrl(String compilerNickname,
                               String compilerOptions,
                               List<SrcFile> srcFiles,
                               List<Map<String, Object>> disassembledClassFiles){
        String storeId = store(compilerNickname, compilerOptions, srcFiles, disassembledClassFiles);
        return getStoreUrl(storeId);
    }

    public String getStoreUrl(String storeId){
        return appConfig.getUrl() + "/share/" + storeId;
    }
}
