package app.cafebabe.bytecode.classimage;

import java.util.ArrayList;
import java.util.List;

public enum class_access_flag {
    ACC_PUBLIC(0x0001),
    ACC_FINAL(0x0010),
    ACC_SUPER(0x0020),
    ACC_INTERFACE(0x0200),
    ACC_ABSTRACT(0x0400),
    ACC_SYNTHETIC(0x1000),
    ACC_ANNOTATION(0x2000),
    ACC_ENUM(0x4000);
    private final int value;

    class_access_flag(int value) {
        this.value = value;
    }

    public static List<class_access_flag> fromBitField(short flags) {
        List<class_access_flag> result = new ArrayList<>();
        for (class_access_flag flag : class_access_flag.values()) {
            if ((flag.value & flags) > 0) {
                result.add(flag);
            }
        }
        return result;
    }
}
