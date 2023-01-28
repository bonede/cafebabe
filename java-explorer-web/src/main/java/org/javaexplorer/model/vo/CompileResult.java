package org.javaexplorer.model.vo;

import lombok.Data;
import org.javaexplorer.bytecode.vm.ClassImage;

import java.util.List;

@Data
public class CompileResult {
    private List<ClassImage> classImages;
    private int returnCode;
    private String stdout;
    private String stderr;
    private String shareUrl;
}
