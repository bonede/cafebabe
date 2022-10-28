package org.javaexplorer.model.classfile.annotation;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * SPEC https://docs.oracle.com/javase/specs/jvms/se11/html/jvms-4.html#jvms-4.7.16
 */
@Data
@AllArgsConstructor
public class AnnotationInfo {
    private int typeIndex;
    private String type;
    List<ElementValuePairInfo> elementValuePairs;
}
