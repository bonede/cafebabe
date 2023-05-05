package app.cafebabe.bytecode.classimage.attribute;

import app.cafebabe.bytecode.classimage.annotation;
import lombok.Data;

@Data
public class parameter_annotation {
    public int num_annotations;
    public annotation[] annotations;
}
