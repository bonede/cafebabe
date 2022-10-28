package org.javaexplorer.model.classfile.annotation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ElementValueBool implements ElementValueInfo {
    private int constValueIndex;
    private boolean value;
    @Override
    public ElementValueType getTag() {
        return ElementValueType.ELEMENT_VALUE_BOOL;
    }
}
