package app.cafebabe.model;

import app.cafebabe.model.vo.CompilerOps;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ShareFile {
    public static final String REDIS_KEY = "ShareFile";
    private String id;
    private String deletingToken;
    private Integer hoursToLive;
    private List<SrcFile> srcFiles;
    private CompilerOps ops;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;
    @Data
    public static class PubShareFile{
        private String id;
        private List<SrcFile> srcFiles;
        private CompilerOps ops;
    }
}
