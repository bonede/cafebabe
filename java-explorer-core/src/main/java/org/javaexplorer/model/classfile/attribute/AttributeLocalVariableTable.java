package org.javaexplorer.model.classfile.attribute;

import lombok.Data;

import java.util.List;

/**
 * SPEC https://docs.oracle.com/javase/specs/jvms/se11/html/jvms-4.html#jvms-4.7.13
 */
@Data
public class AttributeLocalVariableTable extends AttributeInfo {
    private List<LocalVariableInfo> localVariableTable;
    public AttributeLocalVariableTable(int nameIndex, String name, int length, List<LocalVariableInfo> localVariableTable) {
        super(nameIndex, name, length);
        this.localVariableTable = localVariableTable;
    }
}
