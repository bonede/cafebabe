package org.javaexplorer.model.classfile.attribute;

import lombok.Data;

@Data
public class AttributeModuleMainClass extends AttributeInfo {
    private int mainClassIndex;
    private String mainClassName;
    public AttributeModuleMainClass(int nameIndex, String name, int length, int mainClassIndex, String mainClassName) {
        super(nameIndex, name, length);
        this.mainClassIndex = mainClassIndex;
        this.mainClassName = mainClassName;
    }
}
