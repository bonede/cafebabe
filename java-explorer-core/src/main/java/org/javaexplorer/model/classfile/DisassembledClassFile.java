package org.javaexplorer.model.classfile;

import lombok.Data;
import org.javaexplorer.model.classfile.attribute.AttributeInfo;
import org.javaexplorer.model.classfile.constant.ConstantClassInfo;
import org.javaexplorer.model.classfile.constant.ConstantInfo;
import org.javaexplorer.model.classfile.field.FieldInfo;
import org.javaexplorer.model.classfile.flag.AccFlag;
import org.javaexplorer.model.classfile.method.MethodInfo;

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
    private List<AttributeInfo> attributes;
    private List<MethodInfo> methods;
    private List<FieldInfo> fields;
}
