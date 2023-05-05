package app.cafebabe.bytecode.classimage.constant;

public enum Kind {
    REF_getField(1),
    REF_getStatic(2),
    REF_putField(3),
    REF_putStatic(4),
    REF_invokeVirtual(5),
    REF_invokeStatic(6),
    REF_invokeSpecial(7),
    REF_newInvokeSpecial(8),
    REF_invokeInterface(9),
    ;
    public final int value;

    Kind(int value) {
        this.value = value;
    }

    public static Kind ofValue(int value) {
        switch (value) {
            case 1:
                return REF_getField;
            case 2:
                return REF_getStatic;
            case 3:
                return REF_putField;
            case 4:
                return REF_putStatic;
            case 5:
                return REF_invokeVirtual;
            case 6:
                return REF_invokeStatic;
            case 7:
                return REF_invokeSpecial;
            case 8:
                return REF_newInvokeSpecial;
            case 9:
                return REF_invokeInterface;
        }
        throw new RuntimeException("Should not reach");
    }
}
