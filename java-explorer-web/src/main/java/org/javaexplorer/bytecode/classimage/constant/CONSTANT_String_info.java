package org.javaexplorer.bytecode.classimage.constant;

import org.javaexplorer.bytecode.classimage.ClassImage;

/**
 * <a herf="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.3">ref</a>
 */
public class CONSTANT_String_info implements cp_info {
    private ClassImage classImage;

    public CONSTANT_String_info(ClassImage classImage) {
        this.classImage = classImage;
    }

    private int string_index;

    public int getStringIndex() {
        return string_index;
    }

    public String getString() {
        return classImage.getUtf8At(string_index);
    }

    @Override
    public ClassImage.tag getTag() {
        return ClassImage.tag.CONSTANT_String;
    }

    @Override
    public void read() {
        string_index = classImage.readu2();
    }

    @Override
    public String toString() {
        return String.format("CONSTANT_String: string@%d",
                string_index
        );
    }

    public String getValue(ClassImage classImage) {
        return classImage.getUtf8At(string_index);
    }
}
