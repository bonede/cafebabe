package org.javaexplorer.model.classfile.constant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConstantStringInfo implements ConstantInfo{
    private int stringIndex;
    private String value;
    @Override
    public ConstantTag getTag() {
        return ConstantTag.CONSTANT_String;
    }

}
