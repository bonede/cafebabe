package org.javaexplorer.model.classfile.attribute;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocalVariableTypeInfo {
    private int startPc;
    private int length;
    private int nameIndex;
    private String name;
    private int signatureIndex;
    private String signature;
    private int index;
}
