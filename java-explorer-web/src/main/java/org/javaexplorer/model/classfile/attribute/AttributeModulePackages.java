package org.javaexplorer.model.classfile.attribute;

import lombok.Data;

@Data
public class AttributeModulePackages extends AttributeInfo {
    private int[] packageIndices;
    public AttributeModulePackages(
            int nameIndex,
            String name,
            int length,
            int[] packageIndices) {
        super(nameIndex, name, length);
        this.packageIndices = packageIndices;
    }
}
