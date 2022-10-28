package org.javaexplorer.model.classfile.annotation;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ElementValueArray implements ElementValueInfo {

    private List<ElementValueInfo> elementValues;

    @Override
    public ElementValueType getTag() {
        return ElementValueType.ELEMENT_VALUE_ARRAY;
    }
}
