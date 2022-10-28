package org.javaexplorer.model.classfile.attribute;

import lombok.Data;

@Data
public class AttributeSourceFile extends AttributeInfo{
    private int sourceFileIndex;
    private String sourceFile;
    public AttributeSourceFile(int nameIndex, String name, int length, int sourceFileIndex, String sourceFile) {
        super(nameIndex, name, length);
        this.sourceFileIndex = sourceFileIndex;
        this.sourceFile = sourceFile;
    }
}
