package org.javaexplorer.model.classfile.annotation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ElementValueChar implements ElementValueInfo {
    private int constValueIndex;
    private char value;
    @Override
    public ElementValueType getTag() {
        return ElementValueType.ELEMENT_VALUE_CHAR;
    }
}
