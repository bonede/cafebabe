package org.javaexplorer.model.classfile.flag;

public enum ModFlag {
    ACC_OPEN(0x0020),
    ACC_SYNTHETIC(0x1000),
    ACC_MANDATED(0x8000);
    private int value;
    public int getValue(){
        return value;
    }
    ModFlag(int i) {
        this.value = i;
    }
}
