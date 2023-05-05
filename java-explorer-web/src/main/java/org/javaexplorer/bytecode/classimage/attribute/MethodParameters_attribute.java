package org.javaexplorer.bytecode.classimage.attribute;

import org.javaexplorer.bytecode.classimage.ClassImage;

public class MethodParameters_attribute extends attribute_info {
    private int parameters_count;
    private parameter[] parameters;

    public int getParameters_count() {
        return parameters_count;
    }

    public parameter[] getParameters() {
        return parameters;
    }

    public MethodParameters_attribute(ClassImage classImage, int attribute_name_index, int attribute_length) {
        super(classImage, attribute_name_index, attribute_length);
    }

    @Override
    public void read() {
        parameters_count = classImage.readu1();
        parameters = new parameter[parameters_count];
        for (int i = 0; i < parameters_count; i++) {
            parameter parameter = new parameter();
            parameters[i] = parameter;
            parameter.name_index = classImage.readu2();
            parameter.access_flags = classImage.readShort();
            parameter.name = classImage.getUtf8At(parameter.name_index);
        }
    }
}
