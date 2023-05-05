package app.cafebabe.bytecode.classimage.constant;

import app.cafebabe.bytecode.classimage.ClassImage;

public class CONSTANT_Double_info implements cp_info {
    private ClassImage classImage;

    public CONSTANT_Double_info(ClassImage classImage) {
        this.classImage = classImage;
    }

    private double value;

    public double getValue() {
        return value;
    }

    @Override
    public ClassImage.tag getTag() {
        return ClassImage.tag.CONSTANT_Double;
    }

    @Override
    public void read() {
        this.value = classImage.readDouble();
    }

    @Override
    public String toString() {
        return "CONSTANT_Double: " + value;
    }
}
