package org.javaexplorer.model.classfile;

import lombok.Data;

import java.util.List;

@Data
public class DisassembledClassFile {
    private int minorVersion;
    private int majorVersion;
    private int constantPoolCount;
    private String sourceFileName;
    private ConstantClassInfo className;
    private ConstantClassInfo superClassName;
    private List<ConstantClassInfo> interfaces;
    private List<AccFlag> accessFlags;
    private List<ConstantInfo> constantPool;
}
