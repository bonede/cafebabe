package org.javaexplorer.bytecode.classimage;

import java.util.ArrayList;
import java.util.List;

public enum method_access_flag {
    ACC_PUBLIC(0x0001),
    ACC_PRIVATE(0x0002),
    ACC_PROTECTED(0x0004),
    ACC_STATIC(0x0008),
    ACC_FINAL(0x0010),
    ACC_SYNCHRONIZED(0x0020),
    ACC_BRIDGE(0x0040),
    ACC_VARARGS(0x0080),
    ACC_NATIVE(0x0100),
    ACC_ABSTRACT(0x0400),
    ACC_STRICT(0x0800),
    ACC_SYNTHETIC(0x1000);
    private final int value;

    method_access_flag(int value) {
        this.value = value;
    }

    public static List<method_access_flag> fromBitField(short flags) {
        List<method_access_flag> result = new ArrayList<>();
        for (method_access_flag flag : method_access_flag.values()) {
            if ((flag.value & flags) > 0) {
                result.add(flag);
            }
        }
        return result;
    }
}
