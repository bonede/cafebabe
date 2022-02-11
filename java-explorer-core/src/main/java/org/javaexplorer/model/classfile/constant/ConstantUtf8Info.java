package org.javaexplorer.model.classfile.constant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConstantUtf8Info implements ConstantInfo{
    private String value;
    @Override
    public ConstantTag getTag() {
        return ConstantTag.CONSTANT_Utf8;
    }
}
