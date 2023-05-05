package org.javaexplorer.bytecode.classimage.constant;

import org.javaexplorer.bytecode.classimage.ClassImage;

public class CONSTANT_Utf8_info implements cp_info {
    private ClassImage classImage;
    private short length;

    public CONSTANT_Utf8_info(ClassImage classImage) {
        this.classImage = classImage;
    }

    private String value;

    public String getValue() {
        return value;
    }

    @Override
    public ClassImage.tag getTag() {
        return ClassImage.tag.CONSTANT_Utf8;
    }

    @Override
    public void read() {
        this.length = classImage.readShort();
        this.value = new String(classImage.readBytes(this.length));
    }

    @Override
    public String toString() {
        return String.format("CONSTANT_Utf8_info: [%d] %s",
                length,
                value
        );
    }
}
