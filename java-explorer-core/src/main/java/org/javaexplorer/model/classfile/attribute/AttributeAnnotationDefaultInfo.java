package org.javaexplorer.model.classfile.attribute;

import lombok.Data;
import org.javaexplorer.model.classfile.annotation.ElementValueInfo;

@Data
public class AttributeAnnotationDefaultInfo extends AttributeInfo {
    private ElementValueInfo elementValue;
    public AttributeAnnotationDefaultInfo(int nameIndex, String name, int length, ElementValueInfo elementValue) {
        super(nameIndex, name, length);
        this.elementValue = elementValue;
    }
}
