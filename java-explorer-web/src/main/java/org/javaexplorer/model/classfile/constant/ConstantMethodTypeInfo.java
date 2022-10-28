package org.javaexplorer.model.classfile.constant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConstantMethodTypeInfo implements ConstantInfo{
    private int descriptorIndex;
    private String descriptor;
    @Override
    public ConstantTag getTag() {
        return ConstantTag.CONSTANT_MethodType;
    }
}
