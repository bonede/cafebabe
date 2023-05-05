package org.javaexplorer.bytecode.classimage.attribute;

import org.javaexplorer.bytecode.classimage.ClassImage;

/**
 * <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.7.2">ref</a></a>
 */
public class ConstantValue_attribute extends attribute_info {
    private short constantvalue_index;

    public ConstantValue_attribute(ClassImage classImage, int attribute_name_index, int attribute_length) {
        super(classImage, attribute_name_index, attribute_length);
    }

    public short getConstantValueIndex() {
        return constantvalue_index;
    }

    @Override
    public void read() {
        constantvalue_index = classImage.readShort();
    }

    @Override
    public String toString() {
        return "ConstantValue_attribute: @" + constantvalue_index;
    }
}
