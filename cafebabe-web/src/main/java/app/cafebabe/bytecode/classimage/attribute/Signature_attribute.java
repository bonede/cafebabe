package app.cafebabe.bytecode.classimage.attribute;

import app.cafebabe.bytecode.classimage.ClassImage;

/**
 * <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.7.9">Ref</a>
 */
public class Signature_attribute extends attribute_info {
    private int signature_index;

    public Signature_attribute(ClassImage classImage, int attribute_name_index, int attribute_length) {
        super(classImage, attribute_name_index, attribute_length);
    }

    @Override
    public void read() {
        signature_index = classImage.readu2();
    }

    public int getSignatureIndex() {
        return signature_index;
    }

    public String getSignature() {
        return classImage.getUtf8At(signature_index);
    }
}
