package org.javaexplorer.bytecode.classimage.attribute;

import lombok.Data;
import org.javaexplorer.bytecode.classimage.annotation;

@Data
public class parameter_annotation {
    public int num_annotations;
    public annotation[] annotations;
}
