package org.javaexplorer.model.classfile.attribute;

import lombok.Data;

import java.util.List;

/**
 * SPEC https://docs.oracle.com/javase/specs/jvms/se11/html/jvms-4.html#jvms-4.7.24
 */
@Data
public class AttributeMethodParameters extends AttributeInfo {
    private List<MethodParameter> methodParameters;
    public AttributeMethodParameters(int nameIndex, String name, int length, List<MethodParameter> methodParameters) {
        super(nameIndex, name, length);
        this.methodParameters = methodParameters;
    }
}
