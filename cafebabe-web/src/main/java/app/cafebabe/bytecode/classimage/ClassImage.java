package app.cafebabe.bytecode.classimage;


import app.cafebabe.bytecode.classimage.attribute.*;
import app.cafebabe.bytecode.classimage.constant.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.nio.ByteBuffer;
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
                case "RuntimeVisibleAnnotations":
                case "RuntimeInvisibleAnnotations":
                    attribute_info = new RuntimeVisibleAnnotations_attribute(this, name_index, length); break;
                case "RuntimeInvisibleParameterAnnotations":
                case "RuntimeVisibleParameterAnnotations":
                    attribute_info = new RuntimeVisibleParameterAnnotations_attribute(this, name_index, length); break;
                case "StackMapTable": attribute_info = new StackMapTable_attribute(this, name_index, length); break;
                case "InnerClasses": attribute_info = new InnerClasses_attribute(this, name_index, length); break;
                case "NestHost": attribute_info = new NestHost_attribute(this, name_index, length); break;
                case "NestMembers": attribute_info = new NestMembers_attribute(this, name_index, length); break;
                case "LocalVariableTable": attribute_info = new LocalVariableTable_attribute(this, name_index, length); break;
                case "BootstrapMethods": attribute_info = new BootstrapMethods_attribute(this, name_index, length); break;
                case "MethodParameters": attribute_info = new MethodParameters_attribute(this, name_index, length); break;
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

    public static element_value readAnnotationValue(ClassImage classImage){
        element_value element_value = new element_value();
        element_value.tag = (char) classImage.readByte();
        switch (element_value.tag) {
            case 'B':
            case 'C':
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 'Z':
            case 's':
                element_value.const_value_index = classImage.readu2();
                break;
            case 'c':
                element_value.class_info_index = classImage.readu2();
                break;
            case 'e':
                element_value.enum_const_value = new enum_const_value();
                element_value.enum_const_value.type_name_index = classImage.readu2();
                element_value.enum_const_value.const_name_index = classImage.readu2();
                break;
            case '@':
                element_value.annotation_value = readAnnotation(classImage);
            case '[':
                element_value.num_values = classImage.readu2();
                element_value.values = new element_value[element_value.num_values];
                for(int i= 0; i < element_value.num_values; i++){
                    element_value.values[i] = readAnnotationValue(classImage);
                }
                break;
            default:
                throw new RuntimeException("Unknown annotation tag: " + element_value.tag);
        }
        return element_value;
    }
    public static annotation readAnnotation(ClassImage classImage){
        annotation annotation = new annotation();
        annotation.type_index = classImage.readu2();
        annotation.num_element_value_pairs = classImage.readu2();
        annotation.typeName = classImage.getUtf8At(annotation.type_index);
        annotation.element_value_pairs = new element_value_pair[annotation.num_element_value_pairs];
        for(int i = 0; i < annotation.element_value_pairs.length; i++){
            annotation.element_value_pairs[i] = new element_value_pair();
            annotation.element_value_pairs[i].element_name_index = classImage.readu2();
            annotation.element_value_pairs[i].elementName = classImage.getUtf8At(annotation.element_value_pairs[i].element_name_index);
            annotation.element_value_pairs[i].value = readAnnotationValue(classImage);
        }
        return annotation;
    }

    public static verification_type_info readVerificationTypeInfo(ClassImage classImage){
        verification_type_info verification_type_info = new verification_type_info();
        verification_type_info.tag = classImage.readu1();
        switch (verification_type_info.tag){
            case 0:
            case 6:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                break;
            case 7: verification_type_info.cpool_index = classImage.readu2(); break;
            case 8: verification_type_info.offset = classImage.readu2(); break;
        }
        return verification_type_info;
    }
    public static stack_map_frame readStackMapFrame(ClassImage classImage){
        stack_map_frame stack_map_frame = new stack_map_frame();
        stack_map_frame.frame_type = classImage.readu1();
        int frame_type = stack_map_frame.frame_type;
        if(frame_type >= 0 && frame_type <= 63){
            // do nothing
        }else if(frame_type >= 64 && frame_type <= 127){
            stack_map_frame.stack = new verification_type_info[]{readVerificationTypeInfo(classImage)};
        }else if(frame_type == 247){
            stack_map_frame.offset_delta = classImage.readu2();
            stack_map_frame.stack = new verification_type_info[]{readVerificationTypeInfo(classImage)};
        }else if(frame_type >= 248 && frame_type <= 250){
            stack_map_frame.offset_delta = classImage.readu2();
        }else if(frame_type == 251){
            stack_map_frame.offset_delta = classImage.readu2();
        }else if(frame_type >= 252 && frame_type <= 254){
            stack_map_frame.offset_delta = classImage.readu2();
            stack_map_frame.locals = new verification_type_info[stack_map_frame.frame_type - 251];
            for(int i = 0; i < stack_map_frame.locals.length; i++){
                verification_type_info verification_type_info = readVerificationTypeInfo(classImage);
                stack_map_frame.locals[i] = verification_type_info;
            }
        }else if(frame_type == 255){
            stack_map_frame.offset_delta = classImage.readu2();
            stack_map_frame.number_of_locals = classImage.readu2();
            stack_map_frame.locals = new verification_type_info[stack_map_frame.number_of_locals];
            for(int i = 0; i < stack_map_frame.locals.length; i++){
                verification_type_info verification_type_info = readVerificationTypeInfo(classImage);
                stack_map_frame.locals[i] = verification_type_info;
            }
            stack_map_frame.number_of_stack_items = classImage.readu2();
            stack_map_frame.stack = new verification_type_info[stack_map_frame.number_of_stack_items];
            for(int i = 0; i < stack_map_frame.stack.length; i++){
                verification_type_info verification_type_info = readVerificationTypeInfo(classImage);
                stack_map_frame.stack[i] = verification_type_info;
            }
        }
        return stack_map_frame;
    }


}
