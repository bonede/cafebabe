package org.javaexplorer.model.classfile.constant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConstantInvokeDynamicInfo implements ConstantInfo{
    private int nameAndTypeIndex;
    private String nameAndType;
    private int bootstrapMethodAttrIndex;
    @Override
    public ConstantTag getTag() {
        return ConstantTag.CONSTANT_InvokeDynamic;
    }
}
