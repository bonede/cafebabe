package org.javaexplorer.model.classfile.annotation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ElementValueEnum implements ElementValueInfo {
    private int typeNameIndex;
    private String typeName;
    private int constNameIndex;
    private String constName;
    @Override
    public ElementValueType getTag() {
        return ElementValueType.ELEMENT_VALUE_ENUM;
    }
}
