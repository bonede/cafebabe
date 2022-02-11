package org.javaexplorer.model.classfile.attribute;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * SPEC: https://docs.oracle.com/javase/specs/jvms/se11/html/jvms-4.html#jvms-4.7.12
 */
@Data
@AllArgsConstructor
public class LineNumberInfo {
    private int startPc;
    private int lineNumber;
}
