package org.javaexplorer.model;

import lombok.Data;
import org.javaexplorer.validator.JavaFilePath;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class JavaFile {
    @JavaFilePath
    private String path;
    @NotNull
    private String content;
    @Min(1)
    private int size;
}
