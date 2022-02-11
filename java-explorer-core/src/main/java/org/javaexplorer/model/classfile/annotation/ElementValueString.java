package org.javaexplorer.model.classfile.annotation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ElementValueString implements ElementValueInfo {
    private int constValueIndex;
    private String value;
    @Override
    public ElementValueType getTag() {
        return ElementValueType.ELEMENT_VALUE_STRING;
    }
}
