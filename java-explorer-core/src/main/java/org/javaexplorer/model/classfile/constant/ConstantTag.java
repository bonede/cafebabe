package org.javaexplorer.model.classfile.constant;

public enum ConstantTag {
    CONSTANT_Class(7),
    CONSTANT_Fieldref(9),
    CONSTANT_Methodref(10),
    CONSTANT_InterfaceMethodref(11),
    CONSTANT_String(8),
    CONSTANT_Integer(3),
    CONSTANT_Float(4),
    CONSTANT_Long(5),
    CONSTANT_Double(6),
    CONSTANT_NameAndType(12),
    CONSTANT_Utf8(1),
    CONSTANT_MethodHandle(15),
    CONSTANT_MethodType(16),
    CONSTANT_InvokeDynamic(18),
    CONSTANT_Module(19),
    CONSTANT_Package(18);
    private int value;
    ConstantTag(int i) {
        this.value = i;
    }
    public static ConstantTag valueOf(int value){
        switch (value){
            case 7: return CONSTANT_Class;
            case 9: return CONSTANT_Fieldref;
            case 10: return CONSTANT_Methodref;
            case 11: return CONSTANT_InterfaceMethodref;
            case 8: return CONSTANT_String;
            case 3: return CONSTANT_Integer;
            case 4: return CONSTANT_Float;
            case 5: return CONSTANT_Long;
            case 6: return CONSTANT_Double;
            case 12: return CONSTANT_NameAndType;
            case 1: return CONSTANT_Utf8;
        }
        throw new RuntimeException("Invalid constant tag " + value);
    }
    public int getValue() {
        return value;
    }
}
