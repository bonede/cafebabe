package org.javaexplorer.model.classfile.annotation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ElementValueInt implements ElementValueInfo {
    private int constValueIndex;
    private int value;
    @Override
    public ElementValueType getTag() {
        return ElementValueType.ELEMENT_VALUE_INT;
    }
}
