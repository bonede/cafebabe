package app.cafebabe.model;

import app.cafebabe.model.vo.CompilerOps;
import lombok.Data;

import java.util.List;

@Data
public class ShareFile {
    public static final String REDIS_KEY = "ShareFile";
    private String id;
    private String deletingToken;
    private Integer hoursToLive;
    private List<SrcFile> srcFiles;
    private CompilerOps ops;

    @Data
    public static class PubShareFile{
        private String id;
        private List<SrcFile> srcFiles;
        private CompilerOps ops;
    }
}
