package org.javaexplorer.bytecode.classimage.attribute;

import org.javaexplorer.bytecode.classimage.ClassImage;
import org.javaexplorer.bytecode.classimage.annotation;

/**
 * <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.7.16">ref</a>
 */
public class RuntimeVisibleAnnotations_attribute extends attribute_info {
    private int num_annotations;
    private annotation[] annotations;

    public RuntimeVisibleAnnotations_attribute(ClassImage classImage, int attribute_name_index, int attribute_length) {
        super(classImage, attribute_name_index, attribute_length);
    }

    public int getNumAnnotations() {
        return num_annotations;
    }

    public annotation[] getAnnotations() {
        return annotations;
    }

    @Override
    public void read() {
        num_annotations = classImage.readu2();
        annotations = new annotation[num_annotations];
        for (int i = 0; i < num_annotations; i++) {
            annotations[i] = ClassImage.readAnnotation(classImage);
        }
    }
}
