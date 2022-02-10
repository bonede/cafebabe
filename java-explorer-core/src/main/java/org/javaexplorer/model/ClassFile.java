package org.javaexplorer.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
public class ClassFile {
    @NotEmpty
    private String path;
    @NotEmpty
    private String content;
    @Min(0)
    private long size;
}
