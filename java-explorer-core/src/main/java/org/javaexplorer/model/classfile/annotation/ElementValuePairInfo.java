package org.javaexplorer.model.classfile.annotation;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * SPEC https://docs.oracle.com/javase/specs/jvms/se11/html/jvms-4.html#jvms-4.7.16
 */
@Data
@AllArgsConstructor
public class ElementValuePairInfo {
    private int elementNameIndex;
    private String elementName;
    ElementValueInfo elementValue;
}
