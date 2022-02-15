package org.javaexplorer.model.classfile.attribute;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttributeNestHost extends AttributeInfo {
    private int hostClassIndex;
    private String hostClassName;
    public AttributeNestHost(int nameIndex, String name, int length, int hostClassIndex, String hostClassName) {
        super(nameIndex, name, length);
        this.hostClassIndex = hostClassIndex;
        this.hostClassName = hostClassName;
    }
}
