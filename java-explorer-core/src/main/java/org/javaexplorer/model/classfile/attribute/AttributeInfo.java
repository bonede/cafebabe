package org.javaexplorer.model.classfile.attribute;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttributeInfo {
    protected int nameIndex;
    protected String name;
    protected int length;

    public AttributeInfo() {
    }
}
