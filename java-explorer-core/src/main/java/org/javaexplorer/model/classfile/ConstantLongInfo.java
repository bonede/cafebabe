package org.javaexplorer.model.classfile;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConstantLongInfo implements ConstantInfo{
    private long value;
    @Override
    public ConstantTag getTag() {
        return ConstantTag.CONSTANT_Long;
    }
}
