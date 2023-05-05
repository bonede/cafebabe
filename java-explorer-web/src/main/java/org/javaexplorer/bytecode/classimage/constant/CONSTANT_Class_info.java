package org.javaexplorer.bytecode.classimage.constant;

import org.javaexplorer.bytecode.classimage.ClassImage;

public class CONSTANT_Class_info implements cp_info {
    private ClassImage classImage;


    public CONSTANT_Class_info(ClassImage classImage) {
        this.classImage = classImage;
    }

    private short name_index;

    public short getNameIndex() {
        return name_index;
    }

    public String getName() {
        return classImage.getUtf8At(name_index);
    }

    @Override
    public ClassImage.tag getTag() {
        return ClassImage.tag.CONSTANT_Class;
    }

    @Override
    public void read() {
        this.name_index = classImage.readShort();
    }

    @Override
    public String toString() {
        return String.format("CONSTANT_Class_info: name@%d",
                name_index
        );
    }
}
