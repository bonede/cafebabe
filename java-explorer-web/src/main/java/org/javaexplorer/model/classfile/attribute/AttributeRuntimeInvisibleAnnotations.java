package org.javaexplorer.model.classfile.attribute;

import lombok.Data;
import org.javaexplorer.model.classfile.annotation.AnnotationInfo;

import java.util.List;

@Data
public class AttributeRuntimeInvisibleAnnotations extends AttributeInfo {
    private List<AnnotationInfo> annotations;
    public AttributeRuntimeInvisibleAnnotations(int nameIndex, String name, int length, List<AnnotationInfo> annotations) {
        super(nameIndex, name, length);
        this.annotations = annotations;
    }
}
