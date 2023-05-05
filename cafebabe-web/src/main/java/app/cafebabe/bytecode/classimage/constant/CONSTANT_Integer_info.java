package app.cafebabe.bytecode.classimage.constant;

import app.cafebabe.bytecode.classimage.ClassImage;

public class CONSTANT_Integer_info implements cp_info {
    private ClassImage classImage;

    public CONSTANT_Integer_info(ClassImage classImage) {
        this.classImage = classImage;
    }

    private int value;

    @Override
    public ClassImage.tag getTag() {
        return ClassImage.tag.CONSTANT_Integer;
    }

    @Override
    public void read() {
        this.value = classImage.readInt();
    }

    @Override
    public String toString() {
        return "CONSTANT_Integer: " + value;
    }

    public int getValue() {
        return value;
    }
}
