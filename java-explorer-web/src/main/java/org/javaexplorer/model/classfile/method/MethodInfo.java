package org.javaexplorer.model.classfile.method;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.javaexplorer.model.classfile.attribute.AttributeInfo;
import org.javaexplorer.model.classfile.flag.AccFlag;

import java.util.List;

/**
 * SPEC: https://docs.oracle.com/javase/specs/jvms/se11/html/jvms-4.html#jvms-4.6
 */
@Data
@AllArgsConstructor
public class MethodInfo {
    private List<AccFlag> accFlags;
    private int nameIndex;
    private String name;
    private int descriptorIndex;
    private String descriptor;
    private List<AttributeInfo> attributes;
}
