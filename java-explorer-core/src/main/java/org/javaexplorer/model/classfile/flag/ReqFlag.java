package org.javaexplorer.model.classfile.flag;

public enum ReqFlag {
    ACC_TRANSITIVE(0x0020),
    ACC_STATIC_PHASE(0x0040),
    ACC_SYNTHETIC(0x1000),
    ACC_MANDATED(0x8000);
    private int value;
    public int getValue(){
        return value;
    }
    ReqFlag(int i) {
        this.value = i;
    }
}
