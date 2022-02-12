package org.javaexplorer.model.classfile.attribute;

import lombok.Data;

@Data
public class AttributeEnclosingMethod extends AttributeInfo{
    private int classIndex;
    private String className;
    private int methodIndex;
    private String method;
    public AttributeEnclosingMethod(
            int nameIndex,
            String name,
            int length,
            int classIndex,
            String className,
            int methodIndex,
            String method)
    {
        super(nameIndex, name, length);
        this.classIndex = classIndex;
        this.className = className;
        this.methodIndex = methodIndex;
        this.method = method;
    }
}
