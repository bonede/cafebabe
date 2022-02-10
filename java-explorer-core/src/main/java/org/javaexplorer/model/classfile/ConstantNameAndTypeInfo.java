package org.javaexplorer.model.classfile;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConstantNameAndTypeInfo implements ConstantInfo{
    private int nameIndex;
    private int descriptorIndex;
    private String name;
    private String descriptor;

    @Override
    public String toString() {
        return name + descriptor;
    }

    @Override
    public ConstantTag getTag() {
        return ConstantTag.CONSTANT_NameAndType;
    }
}
