package org.javaexplorer.model.classfile;

public class ConstantClassInfo implements ConstantInfo{
    @Override
    public ConstantTag getTag() {
        return ConstantTag.CONSTANT_Class;
    }
}
