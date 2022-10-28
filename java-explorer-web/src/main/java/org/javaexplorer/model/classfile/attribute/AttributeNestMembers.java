package org.javaexplorer.model.classfile.attribute;

import lombok.Data;

@Data
public class AttributeNestMembers extends AttributeInfo{
    private int[] classIndices;
    public AttributeNestMembers(
            int nameIndex,
            String name,
            int length,
            int[] classIndices) {
        super(nameIndex, name, length);
        this.classIndices = classIndices;
    }
}
