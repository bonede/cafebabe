package org.javaexplorer.model.classfile.attribute;

import lombok.Data;

@Data
public class AttributeSynthetic extends AttributeInfo {
    public AttributeSynthetic(int nameIndex, String name, int length) {
        super(nameIndex, name, length);
    }
}
