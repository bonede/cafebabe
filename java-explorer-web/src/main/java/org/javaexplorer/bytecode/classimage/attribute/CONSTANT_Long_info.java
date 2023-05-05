package org.javaexplorer.bytecode.classimage.attribute;

import org.javaexplorer.bytecode.classimage.ClassImage;
import org.javaexplorer.bytecode.classimage.constant.cp_info;

public class CONSTANT_Long_info implements cp_info {
    private ClassImage classImage;

    public CONSTANT_Long_info(ClassImage classImage) {
        this.classImage = classImage;
    }

    private long value;

    public long getValue() {
        return value;
    }

    @Override
    public ClassImage.tag getTag() {
        return ClassImage.tag.CONSTANT_Long;
    }

    @Override
    public void read() {
        this.value = classImage.readLong();
    }

    @Override
    public String toString() {
        return "CONSTANT_Long: " + value;
    }
}
