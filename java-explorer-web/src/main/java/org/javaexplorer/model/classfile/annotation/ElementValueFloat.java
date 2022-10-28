package org.javaexplorer.model.classfile.annotation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ElementValueFloat implements ElementValueInfo {
    private int constValueIndex;
    private float value;
    @Override
    public ElementValueType getTag() {
        return ElementValueType.ELEMENT_VALUE_FLOAT;
    }
}
