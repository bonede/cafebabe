package org.javaexplorer.model.classfile.constant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConstantInterfaceMethodrefInfo implements ConstantInfo{
    private int classIndex;
    private int nameAndTypeIndex;
    private String className;
    private String nameAndTypeName;
    @Override
    public ConstantTag getTag() {
        return ConstantTag.CONSTANT_InterfaceMethodref;
    }
}
