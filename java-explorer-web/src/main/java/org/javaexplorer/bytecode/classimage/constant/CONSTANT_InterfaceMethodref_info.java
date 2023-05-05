package org.javaexplorer.bytecode.classimage.constant;

import org.javaexplorer.bytecode.classimage.ClassImage;

/**
 * <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.2>ref</a>
 */
public class CONSTANT_InterfaceMethodref_info implements cp_info {
    private ClassImage classImage;

    public CONSTANT_InterfaceMethodref_info(ClassImage classImage) {
        this.classImage = classImage;
    }

    private short class_index;
    private short name_and_type_index;

    @Override
    public ClassImage.tag getTag() {
        return ClassImage.tag.CONSTANT_InterfaceMethodref;
    }

    @Override
    public void read() {
        this.class_index = classImage.readShort();
        this.name_and_type_index = classImage.readShort();
    }

    @Override
    public String toString() {
        return String.format("CONSTANT_InterfaceMethodref: class@%d nameType@%d",
                class_index,
                name_and_type_index
        );
    }

    public String getMethodName() {
        return classImage.getNameAndTypeInfo(name_and_type_index).getName();
    }

    public String getMethodDescriptor() {
        return classImage.getNameAndTypeInfo(name_and_type_index).getDescriptor();
    }

    public short getClassIndex() {
        return class_index;
    }

    public short getNameAndTypeIndex() {
        return name_and_type_index;
    }
}
