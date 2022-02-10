package org.javaexplorer.model.classfile;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConstantDoubleInfo implements ConstantInfo{
    private double value;

    @Override
    public ConstantTag getTag() {
        return ConstantTag.CONSTANT_Double;
    }

}
