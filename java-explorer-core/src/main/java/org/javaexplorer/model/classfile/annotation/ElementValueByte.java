package org.javaexplorer.model.classfile.annotation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ElementValueByte implements ElementValueInfo {
    private int constValueIndex;
    private byte value;
    @Override
    public ElementValueType getTag() {
        return ElementValueType.ELEMENT_VALUE_BYTE;
    }
}
