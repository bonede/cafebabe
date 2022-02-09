package org.javaexplorer.model.classfile;

import lombok.Data;

@Data
public class ClassFile {
    private int magic;
    private int minorVersion;
    private int majorVersion;
    private int constantPoolCount;
    private static ConstantInfo[] constantPool;

    public static void main(String[] args) {
        constantPool[1] = new ConstantClassInfo();
    }
}
