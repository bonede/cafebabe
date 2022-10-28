package org.javaexplorer.model.classfile.flag;

public enum OpenFlag {
    ACC_SYNTHETIC(0x1000),
    ACC_MANDATED(0x8000 );
    private int value;
    public int getValue(){
        return value;
    }
    OpenFlag(int i) {
        this.value = i;
    }
}
