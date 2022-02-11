package org.javaexplorer.model.classfile.constant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConstantFloatInfo implements ConstantInfo{
    private float value;
    @Override
    public ConstantTag getTag() {
        return ConstantTag.CONSTANT_Float;
    }
}
