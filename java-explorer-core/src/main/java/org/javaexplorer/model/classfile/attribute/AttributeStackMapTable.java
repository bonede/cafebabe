package org.javaexplorer.model.classfile.attribute;

import lombok.Data;

import java.util.List;

/**
 * SPEC https://docs.oracle.com/javase/specs/jvms/se11/html/jvms-4.html#jvms-4.7.4
 */
@Data
public class AttributeStackMapTable extends AttributeInfo{
    private List<StackMapFrame> stackMapFrames;
    public AttributeStackMapTable(int nameIndex, String name, int length, List<StackMapFrame> stackMapFrames) {
        super(nameIndex, name, length);
        this.stackMapFrames = stackMapFrames;
    }
}
