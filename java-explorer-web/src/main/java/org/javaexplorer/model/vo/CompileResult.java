package org.javaexplorer.model.vo;

import lombok.Data;
import org.javaexplorer.model.ClassFile;

import java.util.List;

@Data
public class CompileResult {
    private List<ClassFile> classFiles;
    private int returnCode;
    private String stdout;
    private String stderr;
}
