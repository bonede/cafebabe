package org.javaexplorer.model;

import lombok.Data;
import org.javaexplorer.bytecode.vm.ClassImage;

/**
 * Class file with its content and parsed class image
 */
@Data
public class ClassFile {
    private String path;
    private byte[] content;
    private ClassImage classImage;
    public int getSize(){
        return content == null ? 0 : content.length;
    }
}
