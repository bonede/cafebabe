package app.cafebabe.bytecode.classimage.attribute;

import app.cafebabe.bytecode.classimage.ClassImage;

/**
 * <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.7.2">ref</a></a>
 */
public class Deprecated_attribute extends attribute_info {
    public Deprecated_attribute(ClassImage classImage, int attribute_name_index, int attribute_length) {
        super(classImage, attribute_name_index, attribute_length);
    }

    @Override
    public void read() {
        // Do nothing
    }

}
