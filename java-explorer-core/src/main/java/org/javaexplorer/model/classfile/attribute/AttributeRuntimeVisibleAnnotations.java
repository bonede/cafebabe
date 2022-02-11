package org.javaexplorer.model.classfile.attribute;

import lombok.Data;
import org.javaexplorer.model.classfile.annotation.AnnotationInfo;

import java.util.List;

/**
 * SPEC: https://docs.oracle.com/javase/specs/jvms/se11/html/jvms-4.html#jvms-4.7.16
 */
@Data
public class AttributeRuntimeVisibleAnnotations extends AttributeInfo{
    private List<AnnotationInfo> annotations;
    public AttributeRuntimeVisibleAnnotations(int nameIndex, String name, int length, List<AnnotationInfo> annotations) {
        super(nameIndex, name, length);
        this.annotations = annotations;
    }
}
