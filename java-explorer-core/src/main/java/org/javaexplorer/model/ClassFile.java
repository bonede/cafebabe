package org.javaexplorer.model;

import lombok.Data;

@Data
public class ClassFile {
    private String path;
    private String content;
    private long size;
}
