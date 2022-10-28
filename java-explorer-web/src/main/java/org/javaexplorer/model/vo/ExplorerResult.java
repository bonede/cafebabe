package org.javaexplorer.model.vo;

import lombok.Data;
import org.javaexplorer.bytecode.vm.ClassImage;

import java.util.List;

@Data
public class ExplorerResult {
    private List<ClassImage> classImages;
    private int code;
    private String output;
    private String shareUrl;
}
