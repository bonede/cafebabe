package org.javaexplorer.model.classfile.attribute;

import lombok.Data;
import org.javaexplorer.model.classfile.annotation.ParameterAnnotationInfo;

import java.util.List;

/**
 * SPEC: https://docs.oracle.com/javase/specs/jvms/se11/html/jvms-4.html#jvms-4.7.19
 */
@Data
public class AttributeRuntimeInvisibleParameterAnnotations extends AttributeInfo {
    private List<ParameterAnnotationInfo> annotations;
    public AttributeRuntimeInvisibleParameterAnnotations(int nameIndex, String name, int length, List<ParameterAnnotationInfo> annotations) {
        super(nameIndex, name, length);
        this.annotations = annotations;
    }
}
