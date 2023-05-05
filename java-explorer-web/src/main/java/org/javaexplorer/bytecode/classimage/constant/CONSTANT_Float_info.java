package org.javaexplorer.bytecode.classimage.constant;

import org.javaexplorer.bytecode.classimage.ClassImage;

public class CONSTANT_Float_info implements cp_info {
    private ClassImage classImage;

    public CONSTANT_Float_info(ClassImage classImage) {
        this.classImage = classImage;
    }

    private float value;

    @Override
    public ClassImage.tag getTag() {
        return ClassImage.tag.CONSTANT_Float;
    }

    @Override
    public void read() {
        this.value = classImage.readFloat();
    }

    @Override
    public String toString() {
        return "CONSTANT_Float: " + value;
    }

    public float getValue() {
        return value;
    }
}
