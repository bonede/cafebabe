package org.javaexplorer.model.classfile.attribute;

import lombok.Data;

@Data
public class AttributeUnknown extends AttributeInfo {
    private int[] bytes;
    public AttributeUnknown(int nameIndex, String name, int length, int[] bytes) {
        super(nameIndex, name, length);
        this.bytes = bytes;
    }
}
