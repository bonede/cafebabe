package org.javaexplorer.model.classfile.constant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConstantModuleInfo implements ConstantInfo{
    private int nameIndex;
    private String name;
    @Override
    public ConstantTag getTag() {
        return ConstantTag.CONSTANT_Module;
    }
}
