package org.javaexplorer.bytecode.classimage.attribute;

import org.javaexplorer.bytecode.classimage.ClassImage;

public abstract class attribute_info {
    protected ClassImage classImage;
    protected int attribute_name_index;
    protected int attribute_length;

    public attribute_info(ClassImage classImage, int attribute_name_index, int attribute_length) {
        this.classImage = classImage;
        this.attribute_name_index = attribute_name_index;
        this.attribute_length = attribute_length;
    }

    public int getAttributeNameIndex() {
        return attribute_name_index;
    }

    public String getAttributeName() {
        return classImage.getUtf8At(attribute_name_index);
    }

    public int attribute_name_index() {
        return attribute_name_index;
    }

    public int attribute_length() {
        return attribute_length;
    }

    public abstract void read();
}
