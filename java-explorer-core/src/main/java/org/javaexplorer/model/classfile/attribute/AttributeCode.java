package org.javaexplorer.model.classfile.attribute;

import lombok.Data;
import org.javaexplorer.model.classfile.annotation.ExceptionHandler;

import java.util.List;

/**
 * SPEC: https://docs.oracle.com/javase/specs/jvms/se11/html/jvms-4.html#jvms-4.7.3
 */
@Data
public class AttributeCode extends AttributeInfo{
    private int maxStack;
    private int maxLocals;
    private int[] code;
    private List<AttributeInfo> attributes;
    private List<ExceptionHandler> exceptionHandlers;
    public AttributeCode(
            int nameIndex,
            String name,
            int length,
            int maxStack,
            int maxLocals,
            int[] code,
            List<AttributeInfo> attributes,
            List<ExceptionHandler> exceptionHandlers
    ) {
        super(nameIndex, name, length);
        this.maxStack = maxStack;
        this.maxLocals = maxLocals;
        this.code = code;
        this.attributes = attributes;
        this.exceptionHandlers = exceptionHandlers;
    }
}
