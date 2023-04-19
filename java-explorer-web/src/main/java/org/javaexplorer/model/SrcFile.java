package org.javaexplorer.model;

import lombok.Data;
import org.javaexplorer.validator.FilePath;

import javax.validation.constraints.NotNull;

@Data
public class SrcFile {
    @FilePath
    private String path;
    @NotNull
    private String content;
}
