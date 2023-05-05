package org.javaexplorer.bytecode.classimage.constant;

import org.javaexplorer.bytecode.classimage.ClassImage;

/**
 * <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.6">ref</a>
 */
public class CONSTANT_NameAndType_info implements cp_info {
    private ClassImage classImage;
    private int name_index;

    public CONSTANT_NameAndType_info(ClassImage classImage) {
        this.classImage = classImage;
    }

    private int descriptor_index;

    public int getNameIndex() {
        return name_index;
    }

    public int getDescriptorIndex() {
        return descriptor_index;
    }

    public String getName() {
        return classImage.getUtf8At(name_index);
    }

    public String getDescriptor() {
        return classImage.getUtf8At(descriptor_index);
    }

    @Override
    public ClassImage.tag getTag() {
        return ClassImage.tag.CONSTANT_NameAndType;
    }

    @Override
    public void read() {
        this.name_index = classImage.readShort();
        this.descriptor_index = classImage.readShort();
    }

    @Override
    public String toString() {
        return String.format("CONSTANT_NameAndType: name@%d descriptor@%d",
                name_index,
                descriptor_index
        );
    }
}
