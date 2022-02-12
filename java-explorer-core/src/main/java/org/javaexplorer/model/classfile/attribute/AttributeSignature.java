package org.javaexplorer.model.classfile.attribute;

import lombok.Data;

/**
 * SPEC: https://docs.oracle.com/javase/specs/jvms/se11/html/jvms-4.html#jvms-4.7.9
 */
@Data
public class AttributeSignature extends AttributeInfo {

    private int signatureIndex;
    private String signature;
    public AttributeSignature(
            int nameIndex,
            String name,
            int length,
            int signatureIndex,
            String signature
    ) {
        super(nameIndex, name, length);
        this.signatureIndex = signatureIndex;
        this.signature = signature;
    }
}
