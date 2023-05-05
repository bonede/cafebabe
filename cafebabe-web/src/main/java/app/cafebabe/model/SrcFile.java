package app.cafebabe.model;

import app.cafebabe.validator.FilePath;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SrcFile {
    @FilePath
    private String path;
    @NotNull
    private String content;
}
