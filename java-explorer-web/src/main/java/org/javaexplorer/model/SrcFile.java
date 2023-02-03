package org.javaexplorer.model;

import lombok.Data;
import org.javaexplorer.validator.JavaFilePath;

import javax.validation.constraints.NotNull;

@Data
public class SrcFile {
    @JavaFilePath
    private String path;
    @NotNull
    private String content;
}
