package org.javaexplorer.model.classfile.attribute;

import lombok.Data;

import java.util.List;

@Data
public class AttributeLocalVariableTypeTable extends AttributeInfo {
    private List<LocalVariableTypeInfo> localVariableTypeTable;
    public AttributeLocalVariableTypeTable(int nameIndex, String name, int length, List<LocalVariableTypeInfo> localVariableTypeTable) {
        super(nameIndex, name, length);
        this.localVariableTypeTable = localVariableTypeTable;
    }
}
