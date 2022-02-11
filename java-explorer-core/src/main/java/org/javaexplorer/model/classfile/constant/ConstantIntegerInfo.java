package org.javaexplorer.model.classfile.constant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConstantIntegerInfo implements ConstantInfo{
    private int value;
    @Override
    public ConstantTag getTag() {
        return ConstantTag.CONSTANT_Integer;
    }
}
