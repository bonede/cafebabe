package org.javaexplorer.bytecode.classimage.attribute;

import org.javaexplorer.bytecode.classimage.ClassImage;
import org.javaexplorer.bytecode.classimage.annotation;

/**
 * <a href="https://docs.oracle.com/javase/specs/jvms/se11/html/jvms-4.html#jvms-4.7.18">ref</a>
 */
public class RuntimeVisibleParameterAnnotations_attribute extends attribute_info {
    private int num_parameters;
    private parameter_annotation[] parameter_annotations;

    public RuntimeVisibleParameterAnnotations_attribute(ClassImage classImage, int attribute_name_index, int attribute_length) {
        super(classImage, attribute_name_index, attribute_length);
    }


    public int getNum_parameters() {
        return num_parameters;
    }

    public parameter_annotation[] getParameter_annotations() {
        return parameter_annotations;
    }

    @Override
    public void read() {
        num_parameters = classImage.readu1();

        parameter_annotations = new parameter_annotation[num_parameters];
        for (int i = 0; i < num_parameters; i++) {
            parameter_annotation parameter_annotation = new parameter_annotation();
            int num_annotations = classImage.readu2();
            parameter_annotation.num_annotations = num_annotations;
            parameter_annotation.annotations = new annotation[num_annotations];
            for (int j = 0; j < num_annotations; j++) {
                parameter_annotation.annotations[j] = ClassImage.readAnnotation(classImage);
            }
            parameter_annotations[i] = parameter_annotation;
        }
    }
}
