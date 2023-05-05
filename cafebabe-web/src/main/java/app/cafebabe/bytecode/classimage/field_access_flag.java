package app.cafebabe.bytecode.classimage;

import java.util.ArrayList;
import java.util.List;

public enum field_access_flag {
    ACC_PUBLIC(0x0001),
    ACC_PRIVATE(0x0002),
    ACC_PROTECTED(0x0004),
    ACC_STATIC(0x0008),
    ACC_FINAL(0x0010),
    ACC_VOLATILE(0x0040),
    ACC_TRANSIENT(0x0080),
    ACC_SYNTHETIC(0x1000),
    ACC_ENUM(0x4000);
    private final int value;

    field_access_flag(int value) {
        this.value = value;
    }

    public static List<field_access_flag> fromBitField(short flags) {
        List<field_access_flag> result = new ArrayList<>();
        for (field_access_flag flag : field_access_flag.values()) {
            if ((flag.value & flags) > 0) {
                result.add(flag);
            }
        }
        return result;
    }
}
