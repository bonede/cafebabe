package app.cafebabe.bytecode.classimage.attribute;

import app.cafebabe.bytecode.classimage.ClassImage;
import app.cafebabe.bytecode.classimage.constant.cp_info;

/**
 * <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.9">ref</a>
 */
public class CONSTANT_MethodType_info implements cp_info {
    private ClassImage classImage;

    public CONSTANT_MethodType_info(ClassImage classImage) {
        this.classImage = classImage;
    }

    private short descriptor_index;

    public short getDescriptorIndex() {
        return descriptor_index;
    }

    public String getDescriptor() {
        return classImage.getUtf8At(descriptor_index);
    }

    @Override
    public ClassImage.tag getTag() {
        return ClassImage.tag.CONSTANT_MethodType;
    }

    @Override
    public void read() {
        this.descriptor_index = classImage.readShort();
    }

    @Override
    public String toString() {
        return String.format("CONSTANT_MethodType: descriptor@%d",
                descriptor_index
        );
    }
}
