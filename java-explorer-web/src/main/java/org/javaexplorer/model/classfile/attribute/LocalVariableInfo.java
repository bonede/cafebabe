package org.javaexplorer.model.classfile.attribute;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocalVariableInfo {
    private int startPc;
    private int length;
    private int nameIndex;
    private String name;
    private int descriptorIndex;
    private String descriptor;
    private int index;
}
