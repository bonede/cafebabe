package org.javaexplorer.model.classfile.annotation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ElementValueShort implements ElementValueInfo {
    private int constValueIndex;
    private short value;
    @Override
    public ElementValueType getTag() {
        return ElementValueType.ELEMENT_VALUE_SHORT;
    }
}
