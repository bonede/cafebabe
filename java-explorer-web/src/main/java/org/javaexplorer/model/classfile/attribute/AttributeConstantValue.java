package org.javaexplorer.model.classfile.attribute;

import lombok.Data;
import org.javaexplorer.model.classfile.constant.ConstantInfo;

@Data
public class AttributeConstantValue extends AttributeInfo{
    private int constantValueIndex;
    private ConstantInfo constantInfo;
    public AttributeConstantValue(int nameIndex, String name, int length, int constantValueIndex, ConstantInfo constantInfo) {
        super(nameIndex, name, length);
        this.constantValueIndex = constantValueIndex;
        this.constantInfo = constantInfo;
    }
}
