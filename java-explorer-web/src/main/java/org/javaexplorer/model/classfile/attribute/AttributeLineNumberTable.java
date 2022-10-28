package org.javaexplorer.model.classfile.attribute;

import lombok.Data;

import java.util.List;

/**
 * SPEC: https://docs.oracle.com/javase/specs/jvms/se11/html/jvms-4.html#jvms-4.7.12
 */
@Data
public class AttributeLineNumberTable extends AttributeInfo {
    private List<LineNumberInfo> lineNumberTable;
    public AttributeLineNumberTable(int nameIndex, String name, int length, List<LineNumberInfo> lineNumberTable) {
        super(nameIndex, name, length);
        this.lineNumberTable = lineNumberTable;
    }
}
