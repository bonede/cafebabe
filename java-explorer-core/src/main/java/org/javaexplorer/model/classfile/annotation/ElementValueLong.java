package org.javaexplorer.model.classfile.annotation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ElementValueLong implements ElementValueInfo {
    private int constValueIndex;
    private long value;
    @Override
    public ElementValueType getTag() {
        return ElementValueType.ELEMENT_VALUE_LONG;
    }
}
