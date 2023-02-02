package org.javaexplorer.bytecode.vm;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.javaexplorer.bytecode.op.DescriptorParser;
import org.javaexplorer.bytecode.op.Instruction;
import org.javaexplorer.bytecode.op.Op;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClassImage {
    public static final int MAGIC = 0xCAFEBABE;
    /*
                ClassFile {
                    u4             magic;
                    u2             minor_version;
                    u2             major_version;
                    u2             constant_pool_count;
                    cp_info        constant_pool[constant_pool_count-1];
                    u2             access_flags;
                    u2             this_class;
                    u2             super_class;
                    u2             interfaces_count;
                    u2             interfaces[interfaces_count];
                    u2             fields_count;
                    field_info     fields[fields_count];
                    u2             methods_count;
                    method_info    methods[methods_count];
                    u2             attributes_count;
                    attribute_info attributes[attributes_count];
                }

                cp_info {
                    u1 tag;
                    u1 info[];
                }

                field_info {
                    u2             access_flags;
                    u2             name_index;
                    u2             descriptor_index;
                    u2             attributes_count;
                    attribute_info attributes[attributes_count];
                }
                method_info {
                        u2             access_flags;
                        u2             name_index;
                        u2             descriptor_index;
                        u2             attributes_count;
                        attribute_info attributes[attributes_count];
                }
                Sppecial method name:
                    1. <init> is the (or one of the) constructor(s) for the instance, and
                        non-static field initialization.
                    2. <clinit> are the static initialization blocks for the class, and
                        static field initialization.

                Constant pool tags
                Constant Type	                Value
                CONSTANT_Class	                 7
                CONSTANT_Fieldref	             9
                CONSTANT_Methodref	            10
                CONSTANT_InterfaceMethodref	    11
                CONSTANT_String	                 8
                CONSTANT_Integer	             3
                CONSTANT_Float	                 4
                CONSTANT_Long	                 5
                CONSTANT_Double             	 6
                CONSTANT_NameAndType	        12
                CONSTANT_Utf8                	 1
                CONSTANT_MethodHandle	        15
                CONSTANT_MethodType	            16
                CONSTANT_InvokeDynamic	        18
             */
    private final ByteBuffer byteBuffer;

    private short minor_version;
    private short major_version;
    private short constant_pool_count;
    private List<class_access_flag> access_flags;
    private cp_info[] constant_pool;
    private short this_class;
    private short super_class;
    private short interfaces_count;
    private short[] interfaces;
    private short fields_count;
    private field_info[] fields;
    private short methods_count;
    private method_info[] methods;
    private short attributes_count;
    private attribute_info[] attributes;
    public ClassImage(byte[] sourceClassFile) {
        this.byteBuffer = ByteBuffer.wrap(sourceClassFile);
        parse();
    }

    public List<class_access_flag> getAccessFlags(){
        return access_flags;
    }

    public static String debugAccessFlagString(List<class_access_flag> access_flags){
        return String.join(
                " ",
                access_flags.stream()
                        .map(e -> e.toString())
                        .collect(Collectors.toList())
        );
    }

    public short getMethodsCount() {
        return methods_count;
    }

    public static String debugMethodAccessFlagString(List<method_access_flag> access_flags){
        return String.join(
                " ",
                access_flags.stream()
                        .map(e -> e.toString())
                        .collect(Collectors.toList())
        );
    }
    public int getClassNameIndex(){
        return this_class;
    }
    public int getSuperClassNameIndex(){
        return super_class;
    }
    public String getClassName(){
        return getClassInfoAt(this_class).getName();
    }

    public String getSuperClassName() {
        return getClassInfoAt(super_class).getName();
    }

    public CONSTANT_NameAndType_info getNameAndTypeInfo(int index){
        return (CONSTANT_NameAndType_info) getConstant(index);
    }

    public short getFieldsCount(){
        return fields_count;
    }

    public field_info[] getFields(){
        return fields;
    }

    public cp_info[] getConstantPool() {
        return constant_pool;
    }

    public method_info[] getMethods() {
        return methods;
    }

    public short getMinorVersion() {
        return minor_version;
    }
    @JsonIgnore
    public method_info getMainMethod(){
        for(method_info m : methods){
            if( "main".equals(getUtf8At(m.name_index())) &&
                "([Ljava/lang/String;)V".equals(getUtf8At(m.getDescriptorIndex())) &&
                m.access_flags.contains(method_access_flag.ACC_PUBLIC)
            ){
                return m;
            }
        }
        return null;
    }

    public method_info getMethodByNameAndType(String name, String nameAndType){
        for(method_info m : methods){
            if( name.equals(getUtf8At(m.name_index())) &&
                    nameAndType.equals(getUtf8At(m.getDescriptorIndex()))
            ){
                return m;
            }
        }
        throw new RuntimeException("Invalid method: " + name + name);
    }

    public method_info getMethodByIndex(int index){
        return methods[index];
    }

    public short getMajorVersion() {
        return major_version;
    }

    public cp_info getConstant(int index){
        return constant_pool[index];
    }

    public short getConstantPoolCount() {
        return constant_pool_count;
    }

    public double readDouble(){
        return this.byteBuffer.getDouble();
    }

    public long readLong(){
        return this.byteBuffer.getLong();
    }

    public void resetReader(){
        byteBuffer.clear();
    }

    public byte[] readBytes(int size){
        byte[] dst = new byte[size];
        byteBuffer.get(dst);
        return dst;
    }

    public byte readByte(){
        return readBytes(1)[0];
    }

    public int readInt(){
        return byteBuffer.getInt();
    }

    public short readShort(){
        return byteBuffer.getShort();
    }

    public int readu1(){
        return Byte.toUnsignedInt(readByte());
    }

    public int readu2(){
        return Short.toUnsignedInt(byteBuffer.getShort());
    }

    public int readMagic(){
        return readInt();
    }

    public void parseMinorVersion(){
        this.minor_version = readShort();
    }

    public void parseMajorVersion(){
        this.major_version = readShort();
    }

    public void parseConstantPool(){
        // zero index means null
        // skip zero index
        constant_pool = new cp_info[constant_pool_count];
        for(int i = 1; i < constant_pool_count; ){
            constant_pool[i] = readConstantInfo();
            if(constant_pool[i] instanceof CONSTANT_Long_info || constant_pool[i] instanceof CONSTANT_Double_info){
                i += 2;
            }else {
                i++;
            }
        }
    }

    public cp_info readConstantInfo(){
        tag t = tag.valueOf(readByte());
        cp_info info;
        switch (t){
            case CONSTANT_Class: info = new CONSTANT_Class_info(this); break;
            case CONSTANT_Fieldref: info = new CONSTANT_Fieldref_info(this); break;
            case CONSTANT_Methodref: info = new CONSTANT_Methodref_info(this); break;
            case CONSTANT_InterfaceMethodref: info = new CONSTANT_InterfaceMethodref_info(this); break;
            case CONSTANT_String: info = new CONSTANT_String_info(this); break;
            case CONSTANT_Integer: info = new CONSTANT_Integer_info(this); break;
            case CONSTANT_Float: info = new CONSTANT_Float_info(this); break;
            case CONSTANT_Long: info = new CONSTANT_Long_info(this); break;
            case CONSTANT_Double: info = new CONSTANT_Double_info(this); break;
            case CONSTANT_NameAndType: info = new CONSTANT_NameAndType_info(this); break;
            case CONSTANT_Utf8: info = new CONSTANT_Utf8_info(this); break;
            case CONSTANT_MethodHandle: info = new CONSTANT_MethodHandle_info(this); break;
            case CONSTANT_MethodType: info = new CONSTANT_MethodType_info(this); break;
            case CONSTANT_InvokeDynamic: info = new CONSTANT_InvokeDynamic_info(this); break;
            default: throw new RuntimeException("Should not reach");
        }
        info.read();
        return info;
    }

    public void parseConstantPoolCount(){
        this.constant_pool_count = readShort();
    }

    public float readFloat() {
        return byteBuffer.getFloat();
    }

    @JsonIgnore
    public ByteBuffer getByteBuffer(){
        return byteBuffer;
    }

    public String getUtf8At(int index){
        return ((CONSTANT_Utf8_info) constant_pool[index]).getValue();
    }

    public CONSTANT_Class_info getClassInfoAt(int index){
        return ((CONSTANT_Class_info) constant_pool[index]);
    }

    public CONSTANT_Fieldref_info getFieldrefAt(int index){
        return ((CONSTANT_Fieldref_info)constant_pool[index]);
    }

    public attribute_info[] parseAttributeInfo(int attributes_count){
        attribute_info[] attributes = new attribute_info[attributes_count];
        for(short i = 0; i < attributes_count; i++){
            int name_index = this.readu2();
            int length = this.readInt();
            String name = this.getUtf8At(name_index);
            attribute_info attribute_info = null;
            switch (name){
                case "ConstantValue": attribute_info = new ConstantValue_attribute(this, name_index, length); break;
                case "Code": attribute_info = new Code_attribute(this, name_index, length); break;
                case "LineNumberTable": attribute_info = new LineNumberTable_attribute(this, name_index, length); break;
                case "Signature": attribute_info = new Signature_attribute(this, name_index, length); break;
                case "Exceptions": attribute_info = new Exceptions_attribute(this, name_index, length); break;
                case "Deprecated": attribute_info = new Deprecated_attribute(this, name_index, length); break;
                case "SourceFile": attribute_info = new SourceFile_attribute(this, name_index, length); break;
                case "RuntimeVisibleAnnotations": attribute_info = new RuntimeVisibleAnnotations_attribute(this, name_index, length); break;
                // TODO add more attributes
                default: attribute_info = new Unknown_attribute(this, name_index, length); break;
            }
            attribute_info.read();
            attributes[i] = attribute_info;
        }
        return attributes;
    }

    public void parse(){
        int magic = readMagic();
        if(magic != MAGIC){
            throw new RuntimeException("Invalid class file");
        }
        parseMinorVersion();
        parseMajorVersion();
        parseConstantPoolCount();
        parseConstantPool();
        parseAccessFlags();
        parseThisClass();
        parseSuperClass();
        parseInterfacesCount();
        parseInterfaces();
        parseFieldsCount();
        parseFields();
        parseMethodCount();
        parseMethods();
        parseAttributesCount();
        parseAttributes();
    }

    public int getAttributeCount(){
        return attributes_count;
    }

    public attribute_info[] getAttributes(){
        return attributes;
    }

    private void parseMethods() {
        methods = new method_info[methods_count];
        for(int i = 0; i < methods_count; i++){
            List<method_access_flag> access_flags = method_access_flag.fromBitField(readShort());
            int name_index = readu2();
            int descriptor_index = readu2();
            int attributes_count = readu2();
            attribute_info[] attributes = parseAttributeInfo(attributes_count);
            methods[i] = new method_info(
                    this,
                    i,
                    access_flags,
                    name_index,
                    descriptor_index,
                    attributes_count,
                    attributes
            );
        }
    }


    private void parseAttributes(){
        attributes = parseAttributeInfo(attributes_count);
    }
    private void parseMethodCount() {
        methods_count = readShort();
    }

    private void parseAttributesCount() {
        attributes_count = readShort();
    }

    private void parseFields() {
        this.fields = new field_info[fields_count];
        for(short i = 0; i < fields_count; i++){
            short access_flags = this.readShort();
            short name_index = this.readShort();
            short descriptor_index = this.readShort();
            short attributes_count = this.readShort();
            attribute_info[] attributes = parseAttributeInfo(attributes_count);
            fields[i] = new field_info(
                    this,
                    field_access_flag.fromBitField(access_flags),
                    name_index,
                    descriptor_index,
                    attributes_count,
                    attributes
            );
        }
    }
    private void parseFieldsCount() {
        this.fields_count = readShort();
    }

    private void parseInterfaces() {
        this.interfaces = new short[interfaces_count];
        for(short i = 0; i < interfaces_count; i++){
            this.interfaces[i] = readShort();
        }
    }

    private void parseInterfacesCount() {
        this.interfaces_count = readShort();
    }

    private void parseThisClass() {
        this.this_class = readShort();
    }

    private void parseSuperClass() {
        this.super_class = readShort();
    }

    private void parseAccessFlags() {
        short access_flags = readShort();
        this.access_flags = class_access_flag.fromBitField(access_flags);
    }

    @Override
    public String toString(){
        return "ClassImage: " + getClassName();
    }
    public enum tag {
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
        CONSTANT_InvokeDynamic(18);
        private final int value;
        tag(int value){
            this.value = value;
        }

        public static tag valueOf(int value){
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
                case 15: return CONSTANT_MethodHandle;
                case 16: return CONSTANT_MethodType;
                case 18: return CONSTANT_InvokeDynamic;
            }
            throw new RuntimeException("Should not reach: " + value);
        }
    }

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
        private final int value;
        Kind(int value) {
            this.value = value;
        }
        public static Kind ofValue(int value){
            switch (value){
                case 1: return REF_getField;
                case 2: return REF_getStatic;
                case 3: return REF_putField;
                case 4: return REF_putStatic;
                case 5: return REF_invokeVirtual;
                case 6: return REF_invokeStatic;
                case 7: return REF_invokeSpecial;
                case 8: return REF_newInvokeSpecial;
                case 9: return REF_invokeInterface;
            }
            throw new RuntimeException("Should not reach");
        }
    }

    public enum class_access_flag{
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
        public static List<class_access_flag> fromBitField(short flags){
            List<class_access_flag> result = new ArrayList<>();
            for(class_access_flag flag : class_access_flag.values()){
                if((flag.value & flags) > 0){
                    result.add(flag);
                }
            }
            return result;
        }
    }

    public enum field_access_flag{
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
        public static List<field_access_flag> fromBitField(short flags){
            List<field_access_flag> result = new ArrayList<>();
            for(field_access_flag flag : field_access_flag.values()){
                if((flag.value & flags) > 0){
                    result.add(flag);
                }
            }
            return result;
        }
    }

    public enum method_access_flag{
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

        public static List<method_access_flag> fromBitField(short flags){
            List<method_access_flag> result = new ArrayList<>();
            for(method_access_flag flag : method_access_flag.values()){
                if((flag.value & flags) > 0){
                    result.add(flag);
                }
            }
            return result;
        }
    }

    public interface cp_info{
        tag getTag();
        void read();
    }

    public static class method_info{
        private ClassImage classImage;

        private int index;
        private List<method_access_flag> access_flags;
        private int name_index;
        private int descriptor_index;
        private int attributes_count;
        private attribute_info[] attributes;
        private Code_attribute code_attribute;
        public List<method_access_flag> getAccessFlags(){
            return access_flags;
        }
        public int getNameIndex(){
            return name_index;
        }
        public method_info(
                            ClassImage classImage,
                            int index,
                            List<method_access_flag> access_flags,
                            int name_index,
                            int descriptor_index,
                            int attributes_count,
                            attribute_info[] attributes
        ) {
            this.classImage = classImage;
            this.index = index;
            this.access_flags = access_flags;
            this.name_index = name_index;
            this.descriptor_index = descriptor_index;
            this.attributes_count = attributes_count;
            this.attributes = attributes;
            for(attribute_info attribute_info : attributes){
                if(attribute_info instanceof Code_attribute){
                    code_attribute = (Code_attribute) attribute_info;
                }
            }
        }

        public int getIndex() {
            return index;
        }

        public boolean isConstructor(ClassImage classImage){
            return "<init>".equals(classImage.getUtf8At(name_index));
        }

        public boolean isStaticInitializer(ClassImage classImage){
            return "<clinit>".equals(classImage.getUtf8At(name_index));
        }

        public int getMaxLocals(){
            return code_attribute.getMaxLocals();
        }

        public int getMaxStack(){
            return code_attribute.getMaxStack();
        }

        public List<method_access_flag> access_flags() {
            return access_flags;
        }

        public int name_index() {
            return name_index;
        }
        public String getName(){
            return classImage.getUtf8At(name_index);
        }
        public String getDescriptor(ClassImage classImage){
            return classImage.getUtf8At(descriptor_index);
        }

        public int getDescriptorIndex() {
            return descriptor_index;
        }

        public String getDescriptor(){
            return classImage.getUtf8At(descriptor_index);
        }
        public attribute_info[] getAttributes() {
            return attributes;
        }

        public int getAttributesCount() {
            return attributes_count;
        }

        @Override
        public String toString() {
            return String.format(
                    "  Method: name_index@%d descriptor@%d %s\n%s",
                    name_index,
                    descriptor_index,
                    debugMethodAccessFlagString(access_flags),
                    String.join("\n", Arrays.stream(attributes).map(e -> e.toString()).collect(Collectors.joining()))
                );
        }

        public Instruction fetchInstruction(Vm vm) {
            if(vm.getPc() < 0){
                return null;
            }
            Instruction instruction = code_attribute.getInstructionAt(vm.getPc());
            return instruction;
        }
    }

    public static class CONSTANT_Class_info implements cp_info {
        private ClassImage classImage;


        public CONSTANT_Class_info(ClassImage classImage) {
            this.classImage = classImage;
        }

        private short name_index;

        public short getNameIndex() {
            return name_index;
        }

        public String getName(){
            return classImage.getUtf8At(name_index);
        }

        @Override
        public tag getTag() {
            return tag.CONSTANT_Class;
        }

        @Override
        public void read() {
            this.name_index = classImage.readShort();
        }

        @Override
        public String toString() {
            return String.format("CONSTANT_Class_info: name@%d",
                    name_index
            );
        }
    }
    /**
     * <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.2>ref</a>
     */
    public static class CONSTANT_Fieldref_info implements cp_info {
        private ClassImage classImage;

        public CONSTANT_Fieldref_info(ClassImage classImage) {
            this.classImage = classImage;
        }

        private short class_index;
        private short name_and_type_index;



        @JsonIgnore
        public DescriptorParser.FieldType getFieldType(){
            return (DescriptorParser.FieldType) DescriptorParser.parse(getFieldDescriptor());
        }
        public String getFieldDescriptor(){
            return classImage.getNameAndTypeInfo(name_and_type_index).getDescriptor();
        }
        public String getClassName(){
            return classImage.getClassInfoAt(class_index).getName();
        }
        public String getFieldName(){
            return classImage.getNameAndTypeInfo(name_and_type_index).getName();
        }
        public short getClassIndex() {
            return class_index;
        }

        public short getNameAndTypeIndex() {
            return name_and_type_index;
        }

        @Override
        public tag getTag() {
            return tag.CONSTANT_Fieldref;
        }

        @Override
        public void read() {
            this.class_index = classImage.readShort();
            this.name_and_type_index = classImage.readShort();
        }

        @Override
        public String toString() {
            return String.format("CONSTANT_Fieldref: class@%d nameType@%d",
                    class_index,
                    name_and_type_index
            );
        }
    }
    /**
     * <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.2>ref</a>
     */
    public static class CONSTANT_Methodref_info implements cp_info {
        private ClassImage classImage;

        public CONSTANT_Methodref_info(ClassImage classImage) {
            this.classImage = classImage;
        }

        private short class_index;
        private short name_and_type_index;

        public String getClassName(){
            return classImage.getClassInfoAt(class_index).getName();
        }

        public String getMethodName(){
            return classImage.getNameAndTypeInfo(name_and_type_index).getName();
        }
        public String getMethodDescriptor(){
            return classImage.getNameAndTypeInfo(name_and_type_index).getDescriptor();
        }
        public short getClassIndex() {
            return class_index;
        }

        public short getNameAndTypeIndex() {
            return name_and_type_index;
        }

        public CONSTANT_NameAndType_info getNameAndType(){
            return classImage.getNameAndTypeInfo(name_and_type_index);
        }

        @Override
        public tag getTag() {
            return tag.CONSTANT_Methodref;
        }

        @Override
        public void read() {
            this.class_index = classImage.readShort();
            this.name_and_type_index = classImage.readShort();
        }

        @Override
        public String toString() {
            return String.format("CONSTANT_Methodref: class@%d nameType@%d",
                    class_index,
                    name_and_type_index
            );
        }
    }

    /**
     * <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.2>ref</a>
     */
    public static class CONSTANT_InterfaceMethodref_info implements cp_info {
        private ClassImage classImage;

        public CONSTANT_InterfaceMethodref_info(ClassImage classImage) {
            this.classImage = classImage;
        }

        private short class_index;
        private short name_and_type_index;
        @Override
        public tag getTag() {
            return tag.CONSTANT_InterfaceMethodref;
        }

        @Override
        public void read() {
            this.class_index = classImage.readShort();
            this.name_and_type_index = classImage.readShort();
        }

        @Override
        public String toString() {
            return String.format("CONSTANT_InterfaceMethodref: class@%d nameType@%d",
                    class_index,
                    name_and_type_index
            );
        }

        public String getMethodName(){
            return classImage.getNameAndTypeInfo(name_and_type_index).getName();
        }
        public String getMethodDescriptor(){
            return classImage.getNameAndTypeInfo(name_and_type_index).getDescriptor();
        }
        public short getClassIndex() {
            return class_index;
        }

        public short getNameAndTypeIndex() {
            return name_and_type_index;
        }
    }

    /**
     * <a herf="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.3">ref</a>
     */
    public static class CONSTANT_String_info implements cp_info {
        private ClassImage classImage;

        public CONSTANT_String_info(ClassImage classImage) {
            this.classImage = classImage;
        }

        private int string_index;

        public int getStringIndex() {
            return string_index;
        }

        public String getString(){
            return classImage.getUtf8At(string_index);
        }

        @Override
        public tag getTag() {
            return tag.CONSTANT_String;
        }

        @Override
        public void read() {
            string_index = classImage.readu2();
        }

        @Override
        public String toString() {
            return String.format("CONSTANT_String: string@%d",
                    string_index
            );
        }

        public String getValue(ClassImage classImage) {
            return classImage.getUtf8At(string_index);
        }
    }

    public static class CONSTANT_Integer_info implements cp_info {
        private ClassImage classImage;

        public CONSTANT_Integer_info(ClassImage classImage) {
            this.classImage = classImage;
        }

        private int value;
        @Override
        public tag getTag() {
            return tag.CONSTANT_Integer;
        }
        @Override
        public void read() {
            this.value = classImage.readInt();
        }

        @Override
        public String toString() {
            return "CONSTANT_Integer: " + value;
        }

        public int getValue() {
            return value;
        }
    }

    public static class CONSTANT_Float_info implements cp_info {
        private ClassImage classImage;

        public CONSTANT_Float_info(ClassImage classImage) {
            this.classImage = classImage;
        }

        private float value;
        @Override
        public tag getTag() {
            return tag.CONSTANT_Float;
        }

        @Override
        public void read() {
            this.value = classImage.readFloat();
        }

        @Override
        public String toString() {
            return "CONSTANT_Float: " + value;
        }

        public float getValue() {
            return value;
        }
    }

    public static class CONSTANT_Long_info implements cp_info {
        private ClassImage classImage;

        public CONSTANT_Long_info(ClassImage classImage) {
            this.classImage = classImage;
        }

        private long value;

        public long getValue() {
            return value;
        }

        @Override
        public tag getTag() {
            return tag.CONSTANT_Long;
        }

        @Override
        public void read() {
            this.value = classImage.readLong();
        }
        @Override
        public String toString() {
            return "CONSTANT_Long: " + value;
        }
    }

    public static class CONSTANT_Double_info implements cp_info {
        private ClassImage classImage;

        public CONSTANT_Double_info(ClassImage classImage) {
            this.classImage = classImage;
        }

        private double value;

        public double getValue() {
            return value;
        }

        @Override
        public tag getTag() {
            return tag.CONSTANT_Double;
        }
        @Override
        public void read() {
            this.value = classImage.readDouble();
        }

        @Override
        public String toString() {
            return "CONSTANT_Double: " + value;
        }
    }

    /**
     * <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.6">ref</a>
     */
    public static class CONSTANT_NameAndType_info implements cp_info {
        private ClassImage classImage;
        private int name_index;

        public CONSTANT_NameAndType_info(ClassImage classImage) {
            this.classImage = classImage;
        }

        private int descriptor_index;

        public int getNameIndex() {
            return name_index;
        }

        public int getDescriptorIndex() {
            return descriptor_index;
        }

        public String getName(){
            return classImage.getUtf8At(name_index);
        }

        public String getDescriptor(){
            return classImage.getUtf8At(descriptor_index);
        }

        @Override
        public tag getTag() {
            return tag.CONSTANT_NameAndType;
        }

        @Override
        public void read() {
            this.name_index = classImage.readShort();
            this.descriptor_index = classImage.readShort();
        }

        @Override
        public String toString() {
            return String.format("CONSTANT_NameAndType: name@%d descriptor@%d",
                    name_index,
                    descriptor_index
            );
        }
    }

    public static class CONSTANT_Utf8_info implements cp_info {
        private ClassImage classImage;
        private short length;

        public CONSTANT_Utf8_info(ClassImage classImage) {
            this.classImage = classImage;
        }

        private String value;
        public String getValue(){
            return value;
        }
        @Override
        public tag getTag() {
            return tag.CONSTANT_Utf8;
        }
        @Override
        public void read() {
            this.length = classImage.readShort();
            this.value = new String(classImage.readBytes(this.length));
        }

        @Override
        public String toString() {
            return String.format("CONSTANT_Utf8_info: [%d] %s",
                    length,
                    value
            );
        }
    }

    /**
     * <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.8">ref</a>
     */
    public static class CONSTANT_MethodHandle_info implements cp_info {
        private ClassImage classImage;

        public CONSTANT_MethodHandle_info(ClassImage classImage) {
            this.classImage = classImage;
        }

        private Kind kind;
        private short reference_index;

        public Kind getKind() {
            return kind;
        }

        public short getReferenceIndex() {
            return reference_index;
        }

        @Override
        public tag getTag() {
            return tag.CONSTANT_MethodHandle;
        }
        @Override
        public void read() {
            this.kind = Kind.ofValue(classImage.readByte());
            this.reference_index = classImage.readShort();
        }

        @Override
        public String toString() {
            return String.format("CONSTANT_MethodHandle: kind=%d reference@%d",
                    kind.value,
                    reference_index
            );
        }
    }

    /**
     * <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.9">ref</a>
     */
    public static class CONSTANT_MethodType_info implements cp_info {
        private ClassImage classImage;

        public CONSTANT_MethodType_info(ClassImage classImage) {
            this.classImage = classImage;
        }

        private short descriptor_index;

        public short getDescriptorIndex() {
            return descriptor_index;
        }

        public String getDescriptor(){
            return classImage.getUtf8At(descriptor_index);
        }

        @Override
        public tag getTag() {
            return tag.CONSTANT_MethodType;
        }

        @Override
        public void read() {
            this.descriptor_index = classImage.readShort();
        }

        @Override
        public String toString() {
            return String.format("CONSTANT_MethodType: descriptor@%d",
                    descriptor_index
            );
        }
    }

    /**
     * <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.10">ref</a>
     */
    public static class CONSTANT_InvokeDynamic_info implements cp_info {
        private ClassImage classImage;

        public CONSTANT_InvokeDynamic_info(ClassImage classImage) {
            this.classImage = classImage;
        }

        private short bootstrap_method_attr_index;
        private short name_and_type_index;

        public short getBootstrapMethodAttrIndex() {
            return bootstrap_method_attr_index;
        }

        public short getNameAndTypeIndex() {
            return name_and_type_index;
        }

        @Override
        public tag getTag() {
            return tag.CONSTANT_InvokeDynamic;
        }
        @Override
        public void read() {
            this.bootstrap_method_attr_index = classImage.readShort();
            this.name_and_type_index = classImage.readShort();
        }
        @Override
        public String toString() {
            return String.format("CONSTANT_InvokeDynamic: bootstrap_method_attr@%d name_and_type@%d",
                    bootstrap_method_attr_index,
                    name_and_type_index
            );
        }
    }

    public static abstract class attribute_info{
        protected ClassImage classImage;
        protected int attribute_name_index;
        protected int attribute_length;
        public attribute_info(ClassImage classImage, int attribute_name_index, int attribute_length) {
            this.classImage = classImage;
            this.attribute_name_index = attribute_name_index;
            this.attribute_length = attribute_length;
        }

        public int getAttributeNameIndex(){
            return attribute_name_index;
        }

        public String getAttributeName(){
            return classImage.getUtf8At(attribute_name_index);
        }

        public int attribute_name_index(){
            return attribute_name_index;
        }
        public int attribute_length(){
            return attribute_length;
        }
        public abstract void read();
    }

    /**
     * <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.7.2">ref</a></a>
     */
    public static class ConstantValue_attribute extends attribute_info{
        private short constantvalue_index;

        public ConstantValue_attribute(ClassImage classImage, int attribute_name_index, int attribute_length) {
            super(classImage, attribute_name_index, attribute_length);
        }

        public short getConstantValueIndex() {
            return constantvalue_index;
        }

        @Override
        public void read() {
            constantvalue_index = classImage.readShort();
        }

        @Override
        public String toString() {
            return "ConstantValue_attribute: @" + constantvalue_index;
        }
    }

    public static class exception_table_item{
        public final int start_pc;
        public final int end_pc;
        public final int handler_pc;
        public final int catch_type;

        public int getStartPc() {
            return start_pc;
        }

        public int getEndPc() {
            return end_pc;
        }

        public int getHandlerPc() {
            return handler_pc;
        }

        public int getCatchType() {
            return catch_type;
        }

        public exception_table_item(int start_pc, int end_pc, int handler_pc, int catch_type) {
            this.start_pc = start_pc;
            this.end_pc = end_pc;
            this.handler_pc = handler_pc;
            this.catch_type = catch_type;
        }

        @Override
        public String toString() {
            return String.format("exception_table_item: start_pc=%d end_pc=end_pc handler_pc=%d catch_type@%d",
                    start_pc, end_pc, handler_pc, catch_type
            );
        }
    }

    public static class Code_attribute extends attribute_info{
        Instruction[] instructions;
        attribute_info[] attributes;
        private int max_stack;
        private int max_locals;
        private int code_length;
        private ByteBuffer codeBuffer;

        public int getCodeLength(){
            return code_length;
        }

        private int exception_table_length;
        private exception_table_item[] exception_table;
        private int attributes_count;

        public int getAttributesCount() {
            return attributes_count;
        }

        public Code_attribute(ClassImage classImage, int attribute_name_index, int attribute_length) {
            super(classImage, attribute_name_index, attribute_length);
        }

        @JsonIgnore
        public int getCodeBufferPosition(){
            return codeBuffer.position();
        }

        public int getMaxStack() {
            return max_stack;
        }

        public int getMaxLocals() {
            return max_locals;
        }

        public attribute_info[] getAttributes(){
            return attributes;
        }

        public int getExceptionTableLength() {
            return exception_table_length;
        }

        @Override
        public void read() {
            max_stack = classImage.readShort();
            max_locals = classImage.readShort();
            code_length = classImage.readInt();
            codeBuffer = ByteBuffer.wrap(classImage.readBytes(code_length));
            exception_table_length = classImage.readu2();
            parseExceptionTable(classImage);
            attributes_count = classImage.readu2();
            attributes = classImage.parseAttributeInfo(attributes_count);
        }


        private void parseExceptionTable(ClassImage classImage){
            List<exception_table_item> exception_table = new ArrayList<>();
            for(int i = 0; i < exception_table_length; i++){
                exception_table.add(new exception_table_item(
                        classImage.readu2(),
                        classImage.readu2(),
                        classImage.readu2(),
                        classImage.readu2()
                ));
            }
            this.exception_table = exception_table.toArray(new exception_table_item[]{});
        }

        private int readu1(ByteBuffer byteBuffer, int offset){
            return Byte.toUnsignedInt(byteBuffer.get(offset));
        }

        public Instruction getInstructionAt(int offset){
            int opCode = readu1(codeBuffer, offset);
            Instruction instruction = Op.instructionOf(opCode);
            codeBuffer.position(offset + 1);
            instruction.parse(this);
            codeBuffer.clear();
            return instruction;
        }

        public int readu1(){
            return Byte.toUnsignedInt(codeBuffer.get());
        }

        public void skip(int bytes){
            codeBuffer.position(codeBuffer.position() + bytes);
        }

        public byte readByte(){
            return codeBuffer.get();
        }

        public List<Instruction> getInstructions(){
            codeBuffer.clear();
            List<Instruction> instructions = new ArrayList<>();
            while (codeBuffer.hasRemaining()){
                int opCode = Byte.toUnsignedInt(codeBuffer.get());
                Instruction instruction = Op.instructionOf(opCode);
                instruction.parse(this);
                instructions.add(instruction);
            }
            return instructions;
        }

        @Override
        public String toString() {
            return String.format(
                    "    Code_attribute:\n" +
                    "    max_stack: %d\n" +
                    "    max_locals: %d\n" +
                    "    code_length: %d\n" +
                    "    exception_table_length: %d\n" +
                    "    instructions:\n%s\n" +
                    "    %s\n" +
                    "    exception_table: %s" +
                    "\n",
                    max_stack,
                    max_locals,
                    code_length,
                    exception_table_length,
                    Arrays.stream(instructions).map(e -> "      " +e.toString()).collect(Collectors.joining("\n")),
                    Arrays.stream(attributes).map(e -> e.toString()).collect(Collectors.joining("\n")),
                    Arrays.stream(exception_table).map(e -> e.toString()).collect(Collectors.joining("\n"))
                );
        }

        public int readu2() {
            return Short.toUnsignedInt(codeBuffer.getShort());
        }

        public int readu4() {
            return codeBuffer.getInt();
        }

        public short readShort() {
            return codeBuffer.getShort();
        }
    }

    public static class line_number_table_item{
        private final int start_pc;
        private final int line_number;
        public line_number_table_item(int start_pc, int line_number) {
            this.start_pc = start_pc;
            this.line_number = line_number;
        }

        public int getStartPc(){
            return start_pc;
        }

        public int getLineNumber(){
            return line_number;
        }

        @Override
        public String toString() {
            return String.format("Line: %d -> %d", line_number, start_pc);
        }
    }

    public static class LineNumberTable_attribute extends attribute_info{
        public int getLineNumberTableLength() {
            return line_number_table_length;
        }
        private int line_number_table_length;
        private line_number_table_item[] line_number_table;
        public LineNumberTable_attribute(ClassImage classImage, int attribute_name_index, int attribute_length) {
            super(classImage, attribute_name_index, attribute_length);
        }
        public line_number_table_item[] getLineNumberTable(){
            return line_number_table;
        }
        @Override
        public void read() {
            line_number_table_length = classImage.readu2();
            line_number_table = new line_number_table_item[line_number_table_length];
            for(int i = 0; i < line_number_table_length; i++){
                int start_pc = classImage.readu2();
                int line_number = classImage.readu2();
                line_number_table[i] = new line_number_table_item(start_pc, line_number);
            }
        }

        @Override
        public String toString() {
            return "LineNumberTable_attribute: \n" +
                    Arrays.stream(line_number_table).map(e -> "     " + e.toString()).collect(Collectors.joining("\n")
            );
        }
    }
    public static class stack_map_frame{

    }

    /**
     * <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.7.9">Ref</a>
     */
    public static class Signature_attribute extends attribute_info{
        private int signature_index;
        public Signature_attribute(ClassImage classImage, int attribute_name_index, int attribute_length) {
            super(classImage, attribute_name_index, attribute_length);
        }

        @Override
        public void read() {
            signature_index = classImage.readu2();
        }

        public int getSignatureIndex(){
            return signature_index;
        }

        public String getSignature(){
            return classImage.getUtf8At(signature_index);
        }
    }

    /**
     * <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.7.5">Ref</a>
     */
    public static class Exceptions_attribute  extends attribute_info{
        private int number_of_exceptions;
        private int exception_index_table[];
        public Exceptions_attribute (ClassImage classImage, int attribute_name_index, int attribute_length) {
            super(classImage, attribute_name_index, attribute_length);
        }

        @Override
        public void read() {
            number_of_exceptions = classImage.readu2();
            exception_index_table = new int[number_of_exceptions];
            for(int i = 0; i < number_of_exceptions; i++){
                exception_index_table[i] = classImage.readu2();
            }
        }

        public int getNumberOfExceptions() {
            return number_of_exceptions;
        }

        public int[] getExceptionIndexTable() {
            return exception_index_table;
        }
    }

    /**
     * <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.7.2">ref</a></a>
     */
    public static class Deprecated_attribute  extends attribute_info{
        public Deprecated_attribute(ClassImage classImage, int attribute_name_index, int attribute_length) {
            super(classImage, attribute_name_index, attribute_length);
        }
        @Override
        public void read() {
            // Do nothing
        }

    }

    @Slf4j
    public static class Unknown_attribute extends attribute_info{
        private byte[] value;
        public Unknown_attribute(ClassImage classImage, int attribute_name_index, int attribute_length) {
            super(classImage, attribute_name_index, attribute_length);
            log.info("Unknown attributes: " + getAttributeName());
        }

        @Override
        public void read() {
            value = classImage.readBytes(attribute_length);
        }

        public String getValue(){
            return Hex.encodeHexString(value);
        }

    }

    public static class SourceFile_attribute  extends attribute_info{
        private short sourceFileNameIndex;
        public SourceFile_attribute(ClassImage classImage, int attribute_name_index, int attribute_length) {
            super(classImage, attribute_name_index, attribute_length);
        }

        @Override
        public void read() {
            sourceFileNameIndex = classImage.readShort();
        }

        public String getSourceFileName(){
            return classImage.getUtf8At(sourceFileNameIndex);
        }
        public short getSourceFileNameIndex(){
            return sourceFileNameIndex;
        }

    }

    /**
     * <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.7.16">ref</a>
     */
    public static class RuntimeVisibleAnnotations_attribute  extends attribute_info{
        private int num_annotations;
        private annotation annotations[];
        public RuntimeVisibleAnnotations_attribute (ClassImage classImage, int attribute_name_index, int attribute_length) {
            super(classImage, attribute_name_index, attribute_length);
        }

        public int getNumAnnotations() {
            return num_annotations;
        }

        public annotation[] getAnnotations() {
            return annotations;
        }

        @Override
        public void read() {
            // TODO implement
            classImage.readBytes(attribute_length);
        }
    }
    @Data
    public static class annotation{
        private int type_index;
        private int num_element_value_pairs;
        private element_value_pair element_value_pairs[];

    }
    @Data
    public static class element_value_pair{
        private int element_name_index;
        private element_value value;
    }

    /**
     * <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.7.16.1>ref</a>
     */
    @Data
    public static class element_value{
        private char tag;
        private int const_value_index;
        private enum_const_value enum_const_value;
        private int class_info_index;
        private annotation annotation_value;
        private array_value array_value;
    }
    @Data
    public static class array_value{
        private int num_values;
        private element_value values[];
    }
    @Data
    public static class enum_const_value{
        private int type_name_index;
        private int const_name_index;
    }

    public static class field_info{
        private ClassImage classImage;
        public short getNameIndex(){
            return name_index;
        }
        public String getName(){
            return classImage.getUtf8At(name_index);
        }
        public short getDescriptorIndex(){
            return descriptor_index;
        }
        public String getDescriptor(){
            return classImage.getUtf8At(descriptor_index);
        }
        public attribute_info[] getAttributes(){
            return attributes;
        }
        public List<field_access_flag> getAccessFlags(){
            return access_flags;
        }
        private attribute_info[] attributes;
        private List<field_access_flag> access_flags;
        private short name_index;
        private short descriptor_index;
        private short attributes_count;

        public short getAttributesCount() {
            return attributes_count;
        }

        public field_info(ClassImage classImage, List<field_access_flag> access_flags,
                          short name_index,
                          short descriptor_index,
                          short attributes_count,
                          attribute_info[] attributes
        ) {
            this.classImage = classImage;
            this.access_flags = access_flags;
            this.name_index = name_index;
            this.descriptor_index = descriptor_index;
            this.attributes_count = attributes_count;
            this.attributes = attributes;
        }

        @Override
        public String toString() {
            return String.format(
                    "name@%d descriptor@%",
                    name_index,
                    descriptor_index
            );
        }
    }

}
