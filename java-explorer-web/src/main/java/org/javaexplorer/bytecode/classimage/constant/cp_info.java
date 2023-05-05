package org.javaexplorer.bytecode.classimage.constant;

import org.javaexplorer.bytecode.classimage.ClassImage;

public interface cp_info {
    ClassImage.tag getTag();

    void read();
}
