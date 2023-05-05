package org.javaexplorer.bytecode.classimage.attribute;

import org.javaexplorer.bytecode.classimage.ClassImage;

/**
 * <a href="https://docs.oracle.com/javase/specs/jvms/se11/html/jvms-4.html#jvms-4.7.28">ref</a>
 */
public class NestMembers_attribute extends attribute_info {
    private int number_of_classes;
    private int[] class_indices;
    private String[] classNames;

    public NestMembers_attribute(ClassImage classImage, int attribute_name_index, int attribute_length) {
        super(classImage, attribute_name_index, attribute_length);
    }

    public int getNumber_of_classes() {
        return number_of_classes;
    }

    public int[] getClass_indices() {
        return class_indices;
    }
    public String[] getClassNames() {
        return classNames;
    }

    @Override
    public void read() {
        number_of_classes = classImage.readu2();
        class_indices = new int[number_of_classes];
        classNames = new String[number_of_classes];
        for (int i = 0; i < number_of_classes; i++) {
            class_indices[i] = classImage.readu2();
            classNames[i] = classImage.getClassInfoAt(class_indices[i]).getName();
        }
    }
}
