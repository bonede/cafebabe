package org.javaexplorer.model.classfile.attribute;

import lombok.Data;
import org.javaexplorer.model.classfile.annotation.ParameterAnnotationInfo;

import java.util.List;

@Data
public class AttributeRuntimeVisibleParameterAnnotations extends AttributeInfo {
    private List<ParameterAnnotationInfo> annotations;
    public AttributeRuntimeVisibleParameterAnnotations(int nameIndex, String name, int length, List<ParameterAnnotationInfo> annotations) {
        super(nameIndex, name, length);
        this.annotations = annotations;
    }
}
