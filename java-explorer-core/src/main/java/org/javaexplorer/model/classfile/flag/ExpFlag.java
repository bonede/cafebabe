package org.javaexplorer.model.classfile.flag;

public enum ExpFlag {
    ACC_SYNTHETIC(0x1000),
    ACC_MANDATED(0x8000 );
    private int value;
    public int getValue(){
        return value;
    }
    ExpFlag(int i) {
        this.value = i;
    }
}
