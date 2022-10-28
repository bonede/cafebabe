package org.javaexplorer.model.classfile.annotation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ElementValueDouble implements ElementValueInfo {
    private int constValueIndex;
    private double value;
    @Override
    public ElementValueType getTag() {
        return ElementValueType.ELEMENT_VALUE_DOUBLE;
    }
}
