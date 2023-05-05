package app.cafebabe.bytecode.classimage;

import java.util.ArrayList;
import java.util.List;

public enum parameter_access_flag {
    ACC_FINAL(0x0010),
    ACC_SYNTHETIC(0x1000),
    ACC_MANDATED(0x8000);
    private final int value;

    parameter_access_flag(int value) {
        this.value = value;
    }

    public static List<parameter_access_flag> fromBitField(short flags) {
        List<parameter_access_flag> result = new ArrayList<>();
        for (parameter_access_flag flag : parameter_access_flag.values()) {
            if ((flag.value & flags) > 0) {
                result.add(flag);
            }
        }
        return result;
    }
}
