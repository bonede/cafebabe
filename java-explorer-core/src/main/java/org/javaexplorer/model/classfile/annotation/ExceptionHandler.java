package org.javaexplorer.model.classfile.annotation;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.javaexplorer.model.classfile.constant.ConstantClassInfo;

@Data
@AllArgsConstructor
public class ExceptionHandler {
    private int startPc;
    private int endPc;
    private int handlerPc;
    private int catchTypeIndex;
    private ConstantClassInfo catchType;
}
