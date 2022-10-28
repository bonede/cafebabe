package org.javaexplorer.model.classfile.annotation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ElementValueClass implements ElementValueInfo {
    private int classInfoIndex;
    private String classInfo;
    @Override
    public ElementValueType getTag() {
        return ElementValueType.ELEMENT_VALUE_CLASS;
    }
}
