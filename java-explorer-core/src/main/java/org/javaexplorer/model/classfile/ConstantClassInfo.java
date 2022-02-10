package org.javaexplorer.model.classfile;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConstantClassInfo implements ConstantInfo{
    private int nameIndex;
    private String name;
    @Override
    public ConstantTag getTag() {
        return ConstantTag.CONSTANT_Class;
    }
}
