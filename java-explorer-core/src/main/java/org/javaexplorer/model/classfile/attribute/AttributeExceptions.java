package org.javaexplorer.model.classfile.attribute;

import lombok.Data;
import org.javaexplorer.model.classfile.constant.ConstantClassInfo;

import java.util.List;

/**
 * SPEC
 */
@Data
public class AttributeExceptions extends AttributeInfo{
    private List<ConstantClassInfo> exceptionTable;
    public AttributeExceptions(int nameIndex, String name, int length, List<ConstantClassInfo> exceptionTable) {
        super(nameIndex, name, length);
        this.exceptionTable = exceptionTable;
    }
}
