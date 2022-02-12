package org.javaexplorer.model.classfile.attribute;

import lombok.Data;

import java.util.List;

@Data
public class AttributeInnerClasses extends AttributeInfo {
    private List<InnerClassInfo> classes;
    public AttributeInnerClasses(int nameIndex, String name, int length, List<InnerClassInfo> classes) {
        super(nameIndex, name, length);
        this.classes = classes;
    }
}
