package org.javaexplorer.bytecode.classimage.attribute;

import org.javaexplorer.bytecode.classimage.ClassImage;
import org.javaexplorer.bytecode.classimage.constant.cp_info;

/**
 * <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.10">ref</a>
 */
public class CONSTANT_InvokeDynamic_info implements cp_info {
    private ClassImage classImage;

    public CONSTANT_InvokeDynamic_info(ClassImage classImage) {
        this.classImage = classImage;
    }

    private short bootstrap_method_attr_index;
    private short name_and_type_index;

    public String getName() {
        return classImage.getNameAndTypeInfo(name_and_type_index).getName();
    }

    public String getDescriptor() {
        return classImage.getNameAndTypeInfo(name_and_type_index).getDescriptor();
    }

    public short getBootstrapMethodAttrIndex() {
        return bootstrap_method_attr_index;
    }

    public short getNameAndTypeIndex() {
        return name_and_type_index;
    }

    @Override
    public ClassImage.tag getTag() {
        return ClassImage.tag.CONSTANT_InvokeDynamic;
    }

    @Override
    public void read() {
        this.bootstrap_method_attr_index = classImage.readShort();
        this.name_and_type_index = classImage.readShort();
    }

    @Override
    public String toString() {
        return String.format("CONSTANT_InvokeDynamic: bootstrap_method_attr@%d name_and_type@%d",
                bootstrap_method_attr_index,
                name_and_type_index
        );
    }
}
