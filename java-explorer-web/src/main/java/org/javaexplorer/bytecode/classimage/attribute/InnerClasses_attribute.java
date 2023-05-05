package org.javaexplorer.bytecode.classimage.attribute;

import org.javaexplorer.bytecode.classimage.ClassImage;
import org.javaexplorer.bytecode.classimage.class_access_flag;

/**
 * <a href="https://docs.oracle.com/javase/specs/jvms/se11/html/jvms-4.html#jvms-4.7.6">ref</a>
 */
public class InnerClasses_attribute extends attribute_info {
    private int number_of_classes;
    private inner_class_info[] classes;

    public InnerClasses_attribute(ClassImage classImage, int attribute_name_index, int attribute_length) {
        super(classImage, attribute_name_index, attribute_length);
    }

    public int getNumber_of_classes() {
        return number_of_classes;
    }

    public inner_class_info[] getClasses() {
        return classes;
    }


    @Override
    public void read() {
        number_of_classes = classImage.readu2();
        classes = new inner_class_info[number_of_classes];
        for (int i = 0; i < number_of_classes; i++) {
            classes[i] = new inner_class_info();
            classes[i].inner_class_info_index = classImage.readu2();
            classes[i].outer_class_info_index = classImage.readu2();
            classes[i].inner_name_index = classImage.readu2();
            classes[i].inner_class_access_flags = class_access_flag.fromBitField(classImage.readShort());
            classes[i].innerName = classImage.getUtf8At(classes[i].inner_name_index);
            classes[i].innerClass = classImage.getClassInfoAt(classes[i].inner_class_info_index).getName();
            classes[i].outerClass = classImage.getClassInfoAt(classes[i].outer_class_info_index).getName();
        }
    }
}
