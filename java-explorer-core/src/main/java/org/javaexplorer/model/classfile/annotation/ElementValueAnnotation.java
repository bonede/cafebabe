package org.javaexplorer.model.classfile.annotation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ElementValueAnnotation implements ElementValueInfo {
    private AnnotationInfo annotation;
    @Override
    public ElementValueType getTag() {
        return ElementValueType.ELEMENT_VALUE_ANNOTATION;
    }
}
