package org.javaexplorer.model.classfile.attribute;

import lombok.Data;

@Data
public class AttributeDeprecated extends AttributeInfo {
    public AttributeDeprecated(int nameIndex, String name, int length){
        super(nameIndex, name, length);
    }
}
