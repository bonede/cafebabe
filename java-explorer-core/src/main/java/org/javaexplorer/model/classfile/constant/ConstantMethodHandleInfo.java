package org.javaexplorer.model.classfile.constant;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Explain: https://stackoverflow.com/questions/25088414/what-are-constant-methodhandle-constant-methodtype-and-constant-invokedynamic
 */
@Data
@AllArgsConstructor
public class ConstantMethodHandleInfo implements ConstantInfo{
    // TODO add solid filed instead of int
    private int referenceKind;
    private int referenceIndex;
    @Override
    public ConstantTag getTag() {
        return ConstantTag.CONSTANT_MethodHandle;
    }
}
