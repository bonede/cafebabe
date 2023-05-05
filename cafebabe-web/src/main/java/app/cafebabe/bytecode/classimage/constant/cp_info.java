package app.cafebabe.bytecode.classimage.constant;

import app.cafebabe.bytecode.classimage.ClassImage;

public interface cp_info {
    ClassImage.tag getTag();

    void read();
}
