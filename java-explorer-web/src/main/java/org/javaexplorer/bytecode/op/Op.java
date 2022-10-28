package org.javaexplorer.bytecode.op;

import org.javaexplorer.bytecode.op.DescriptorParser.FieldTag;
import org.javaexplorer.bytecode.op.DescriptorParser.FieldType;
import org.javaexplorer.bytecode.op.DescriptorParser.MethodType;
import org.javaexplorer.bytecode.vm.ClassImage;
import org.javaexplorer.bytecode.vm.HeapObject;
import org.javaexplorer.bytecode.vm.Vm;

import java.util.ArrayList;
import java.util.List;

public class Op {
    public static abstract class OpCode{
        public static final int aconst_null = 1;
        public static final int bipush = 16;
        public static final int iconst_0 = 3;
        public static final int iconst_1 = 4;
        public static final int iconst_2 = 5;
        public static final int iconst_3 = 6;
        public static final int iconst_4 = 7;
        public static final int iconst_5 = 8;
        public static final int ldc2_w = 20;
        public static final int ldc = 18;
        public static final int sipush = 17;

        public static final int dconst_0 = 14;
        public static final int dconst_1 = 15;
        public static final int dup = 89;


        public static final int if_icmpeq = 159;
        public static final int if_icmpne = 160;
        public static final int if_icmplt = 161;
        public static final int if_icmpge = 162;
        public static final int goto_ = 167;
        public static final int ifnonnull = 199;

        public static final int iadd = 96;

        public static final int fconst_1 = 12;
        public static final int fconst_2 = 13;

        public static final int aload = 25;
        public static final int aload_0 = 42;
        public static final int aload_1 = 43;

        public static final int iload = 21;
        public static final int iload_0 = 26;
        public static final int iload_1 = 27;
        public static final int iload_2 = 28;

        public static final int astore = 58;
        public static final int astore_1 = 76;
        public static final int astore_2 = 77;
        public static final int istore = 54;
        public static final int istore_0 = 59;
        public static final int istore_1 = 60;
        public static final int istore_2 = 61;
        public static final int lstore_1 = 64;
        public static final int dstore_3 = 74;

        public static final int imul = 104;
        public static final int iinc = 132;

        public static final int invokespecial = 183;
        public static final int invokestatic = 184;
        public static final int invokevirtual = 182;
        public static final int putfield = 181;
        public static final int putstatic = 179;
        public static final int getstatic = 178;
        public static final int new_ = 187;
        public static final int getfield = 180;


        public static final int rtn = 177;
        public static final int ireturn = 172;
        public static final int areturn = 176;

        public static final int monitorenter = 194;
        public static final int monitorexit = 195;


    }
    public enum Descriptor {
        BYTE,
        CHAR,
        DOUBLE,
        FLOAT,
        INT,
        LONG,
        REF,
        SHORT,
        BOOL,
        ARRAY,
        VOID;

    }


    public static Instruction instructionOf(int opCode){
        switch (opCode){
            case OpCode.aconst_null: return new aconst_null();
            case OpCode.bipush: return new bipush();
            case OpCode.iconst_0: return new iconst_0();
            case OpCode.iconst_1: return new iconst_1();
            case OpCode.iconst_2: return new iconst_2();
            case OpCode.iconst_3: return new iconst_3();
            case OpCode.iconst_4: return new iconst_4();
            case OpCode.iconst_5: return new iconst_5();
            case OpCode.ldc2_w: return new ldc2_w();
            case OpCode.ldc: return new ldc();
            case OpCode.sipush: return new sipush();

            case OpCode.dconst_0: return new dconst_0();
            case OpCode.dconst_1: return new aconst_null();
            case OpCode.dup: return new dup();

            case OpCode.if_icmpeq: return new if_icmpeq();
            case OpCode.if_icmpne: return new if_icmpne();
            case OpCode.if_icmplt: return new if_icmplt();
            case OpCode.if_icmpge: return new if_icmpge();
            case OpCode.ifnonnull: return new ifnonnull();
            case OpCode.goto_: return new goto_();

            case OpCode.iadd: return new iadd();

            case OpCode.fconst_1: return new fconst_1();
            case OpCode.fconst_2: return new fconst_2();

            case OpCode.aload: return new aload();
            case OpCode.aload_0: return new aload_0();
            case OpCode.aload_1: return new aload_1();
            case OpCode.iload: return new iload();
            case OpCode.iload_0: return new iload_0();
            case OpCode.iload_1: return new iload_1();
            case OpCode.iload_2: return new iload_2();

            case OpCode.astore: return new astore();
            case OpCode.astore_1: return new astore_1();
            case OpCode.astore_2: return new astore_2();
            case OpCode.istore: return new istore();
            case OpCode.istore_0: return new istore_0();
            case OpCode.istore_1: return new istore_1();
            case OpCode.istore_2: return new istore_2();
            case OpCode.lstore_1: return new lstore_1();
            case OpCode.dstore_3: return new dstore_3();

            case OpCode.imul: return new imul();
            case OpCode.iinc: return new iinc();

            case OpCode.invokespecial: return new invokespecial();
            case OpCode.invokestatic: return new invokestatic();
            case OpCode.invokevirtual: return new invokevirtual();
            case OpCode.putfield: return new putfield();
            case OpCode.putstatic: return new putstatic();
            case OpCode.getstatic: return new getstatic();
            case OpCode.new_: return new new_();
            case OpCode.getfield: return new getfield();

            case OpCode.rtn: return new rtn();
            case OpCode.ireturn: return new ireturn();
            case OpCode.areturn: return new areturn();

            case OpCode.monitorenter: return new monitorenter();
            case OpCode.monitorexit: return new monitorexit();

        }
        throw new RuntimeException("Invalid opcode: " + opCode);
    }
    public static class aconst_null implements Instruction{
        @Override
        public void execute(Vm vm) {
            vm.pushOpNull();
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.aconst_null;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "aconst_null";
        }
    }

    public static class dup implements Instruction{
        @Override
        public void execute(Vm vm) {
            int value = vm.popOpInt();
            vm.pushOpInt(value);
            vm.pushOpInt(value);
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.dup;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "dup";
        }
    }

    public static class bipush implements Instruction{
        private byte value;

        public bipush(byte value) {
            this.value = value;
        }

        public bipush() {

        }

        @Override
        public void execute(Vm vm) {
            vm.pushOpByte(value);
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {
            this.value = code_attribute.readByte();
        }

        @Override
        public int getOpCode() {
            return OpCode.bipush;
        }

        @Override
        public int size() {
            return 2;
        }

        @Override
        public String toString() {
            return "bipush: " + value;
        }
    }

    public static class iconst_0 implements Instruction{
        @Override
        public void execute(Vm vm) {
            vm.pushOpInt(0);
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }


        @Override
        public int getOpCode() {
            return OpCode.iconst_0;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "iconst_0";
        }
    }

    public static class iconst_1 implements Instruction{
        @Override
        public void execute(Vm vm) {
            vm.pushOpInt(1);
            vm.increasePc(size());
        }
        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }
        @Override
        public int getOpCode() {
            return OpCode.iconst_1;
        }
        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "iconst_1";
        }
    }

    public static class iconst_2 implements Instruction{
        @Override
        public void execute(Vm vm) {
            vm.pushOpInt(2);
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.iconst_2;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "iconst_2";
        }
    }

    public static class iconst_3 implements Instruction{
        @Override
        public void execute(Vm vm) {
            vm.pushOpInt(3);
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.iconst_3;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "iconst_3";
        }
    }

    public static class iconst_4 implements Instruction{
        @Override
        public void execute(Vm vm) {
            vm.pushOpInt(4);
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }


        @Override
        public int getOpCode() {
            return OpCode.iconst_4;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "iconst_4";
        }
    }

    public static class iconst_5 implements Instruction{
        @Override
        public void execute(Vm vm) {
            vm.pushOpInt(5);
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.iconst_5;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "iconst_5";
        }
    }

    public static class dconst_0 implements Instruction{
        @Override
        public void execute(Vm vm) {
            vm.pushOpDouble(0d);
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.dconst_0;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "dconst_0";
        }
    }

    public static class dconst_1 implements Instruction{
        @Override
        public void execute(Vm vm) {
            vm.pushOpDouble(1d);
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.dconst_1;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "dconst_1";
        }
    }

    public static class ldc2_w implements Instruction{
        private int index;
        @Override
        public void execute(Vm vm) {
            ClassImage.cp_info c = vm.getConstant(index);
            if(c instanceof ClassImage.CONSTANT_Long_info){
                vm.pushOpLong(((ClassImage.CONSTANT_Long_info) c).getValue());
            }else if(c instanceof ClassImage.CONSTANT_Double_info){
                vm.pushOpDouble(((ClassImage.CONSTANT_Double_info) c).getValue());
            }else {
                throw new RuntimeException("Must be long or double constant");
            }
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {
            index = code_attribute.readu2();
        }

        @Override
        public int getOpCode() {
            return OpCode.ldc2_w;
        }

        @Override
        public int size() {
            return 3;
        }

        @Override
        public String toString() {
            return "ldc2_w @" + index;
        }
    }

    /**
     * <a href="https://docs.oracle.com/javase/specs/jvms/se11/html/jvms-6.html#jvms-6.5.ldc">Ref</a>
     */
    public static class ldc implements Instruction{
        private int index;
        @Override
        public void execute(Vm vm) {
            ClassImage.cp_info c = vm.getConstant(index);
            if(c instanceof ClassImage.CONSTANT_Integer_info){
                // int
                vm.pushOpInt(((ClassImage.CONSTANT_Integer_info) c).getValue());
            }else if(c instanceof ClassImage.CONSTANT_Float_info){
                // float
                vm.pushOpFloat(((ClassImage.CONSTANT_Float_info) c).getValue());
            }else if(c instanceof ClassImage.CONSTANT_String_info){
                // string
                vm.pushOpInt(((ClassImage.CONSTANT_String_info) c).getValue(vm.getCurrentClassImage()).hashCode());
            }else if(c instanceof ClassImage.CONSTANT_Class_info){
                String className = ((ClassImage.CONSTANT_Class_info) c).getName(vm.getCurrentClassImage());
                vm.pushOpRef(vm.getClassImageByName(className).hashCode());
            }else {
                // TODO implement push reference to Class, Method
                throw new RuntimeException("Must be int,float,short,string constant: " + c.getClass());
            }
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {
            index = code_attribute.readu1();
        }

        @Override
        public int getOpCode() {
            return OpCode.ldc;
        }

        @Override
        public int size() {
            return 2;
        }

        @Override
        public String toString() {
            return "ldc";
        }
    }

    public static class sipush implements Instruction{
        private short value;
        @Override
        public void execute(Vm vm) {
            vm.pushOpShort(value);
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {
            value = code_attribute.readShort();
        }

        @Override
        public int getOpCode() {
            return OpCode.sipush;
        }

        @Override
        public int size() {
            return 3;
        }

        @Override
        public String toString() {
            return "sipush " + value;
        }
    }

    public static class if_icmpeq implements Instruction{
        private int offset;
        public if_icmpeq(short offset) {
            this.offset = offset;
        }

        public if_icmpeq() {

        }

        @Override
        public void execute(Vm vm) {
            if(vm.popOpInt() == vm.popOpInt()){
                vm.increasePc(offset);
            }else {
                vm.increasePc(size());
            }
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {
            this.offset = code_attribute.readShort();
        }

        @Override
        public int getOpCode() {
            return OpCode.if_icmpeq;
        }

        @Override
        public int size() {
            return 3;
        }

        @Override
        public String toString() {
            return "if_icmpeq offset@" + offset;
        }
    }

    public static class if_icmpne implements Instruction{
        private int offset;
        public if_icmpne(short offset) {
            this.offset = offset;
        }

        public if_icmpne() {

        }

        @Override
        public void execute(Vm vm) {
            if(vm.popOpInt() != vm.popOpInt()){
                vm.increasePc(offset);
            }else {
                vm.increasePc(size());
            }
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {
            this.offset = code_attribute.readShort();
        }

        @Override
        public int getOpCode() {
            return OpCode.if_icmpne;
        }

        @Override
        public int size() {
            return 3;
        }

        @Override
        public String toString() {
            return "if_icmpne offset@" + offset;
        }
    }

    public static class if_icmplt implements Instruction{
        private int offset;
        public if_icmplt(short offset) {
            this.offset = offset;
        }

        public if_icmplt() {

        }

        @Override
        public void execute(Vm vm) {
            if(vm.popOpInt() > vm.popOpInt()){
                vm.increasePc(offset);
            }else {
                vm.increasePc(size());
            }
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {
            this.offset = code_attribute.readShort();
        }

        @Override
        public int getOpCode() {
            return OpCode.if_icmplt;
        }

        @Override
        public int size() {
            return 3;
        }

        @Override
        public String toString() {
            return "if_icmplt offset@" + offset;
        }
    }

    public static class if_icmpge implements Instruction{
        private int offset;

        public if_icmpge() {

        }

        @Override
        public void execute(Vm vm) {
            int value2 = vm.popOpInt();
            int value1 = vm.popOpInt();
            if(value1 >= value2){
                vm.increasePc(offset);
            }else {
                vm.increasePc(size());
            }
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {
            this.offset = code_attribute.readShort();
        }

        @Override
        public int getOpCode() {
            return OpCode.if_icmpge;
        }

        @Override
        public int size() {
            return 3;
        }

        @Override
        public String toString() {
            return "if_icmpge offset@" + offset;
        }
    }

    public static class ifnonnull implements Instruction{
        private int offset;

        public ifnonnull() {

        }

        @Override
        public void execute(Vm vm) {
            int ref = vm.popOpRef();
            if(vm.getHeap().contains(ref)){
                vm.increasePc(offset);
            }else {
                vm.increasePc(size());
            }
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {
            this.offset = code_attribute.readShort();
        }

        @Override
        public int getOpCode() {
            return OpCode.ifnonnull;
        }

        @Override
        public int size() {
            return 3;
        }

        @Override
        public String toString() {
            return "ifnonnull offset@" + offset;
        }
    }

    public static class goto_ implements Instruction{
        private int offset;
        public goto_(short offset) {
            this.offset = offset;
        }

        public goto_() {

        }

        @Override
        public void execute(Vm vm) {
            vm.increasePc(offset);
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {
            this.offset = code_attribute.readShort();
        }

        @Override
        public int getOpCode() {
            return OpCode.goto_;
        }

        @Override
        public int size() {
            return 3;
        }

        @Override
        public String toString() {
            return "goto offset@" + offset;
        }
    }

    public static class fconst_1 implements Instruction{
        @Override
        public void execute(Vm vm) {
            vm.pushOpFloat(1f);
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.fconst_1;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "fconst_1";
        }
    }

    public static class fconst_2 implements Instruction{
        @Override
        public void execute(Vm vm) {
            vm.pushOpFloat(2f);
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.fconst_2;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "fconst_2";
        }
    }

    public static class aload implements Instruction{
        private int index;
        public aload(int index) {
            this.index = index;
        }

        public aload() {

        }

        @Override
        public void execute(Vm vm) {
            vm.pushOpInt(vm.getLocalInt(index));
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {
            this.index = code_attribute.readu1();
        }

        @Override
        public int getOpCode() {
            return OpCode.aload;
        }

        @Override
        public int size() {
            return 2;
        }

        @Override
        public String toString() {
            return "aload @" + index;
        }
    }

    public static class aload_0 implements Instruction{
        public aload_0() {
        }

        @Override
        public void execute(Vm vm) {
            vm.pushOpInt(vm.getLocalInt(0));
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }
        @Override
        public int getOpCode() {
            return OpCode.aload_0;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "aload_0";
        }
    }


    public static class aload_1 implements Instruction{
        public aload_1() {
        }

        @Override
        public void execute(Vm vm) {
            vm.pushOpInt(vm.getLocalInt(1));
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }
        @Override
        public int getOpCode() {
            return OpCode.aload_1;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "aload_1";
        }
    }

    public static class iload implements Instruction{
        private int index;
        public iload(short index) {
            this.index = index;
        }

        public iload() {

        }

        @Override
        public void execute(Vm vm) {
            vm.pushOpInt(vm.getLocalInt(index));
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {
            this.index = code_attribute.readu1();
        }

        @Override
        public int getOpCode() {
            return OpCode.iload;
        }

        @Override
        public int size() {
            return 2;
        }

        @Override
        public String toString() {
            return "iload @" + index;
        }
    }

    public static class iadd implements Instruction{
        @Override
        public void execute(Vm vm) {
            vm.pushOpInt(vm.popOpInt() + vm.popOpInt());
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.iadd;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "iadd";
        }
    }

    public static class iload_0 implements Instruction{
        @Override
        public void execute(Vm vm) {
            vm.pushOpInt(vm.getLocalInt(0));
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.iload_0;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "iload_0";
        }
    }

    public static class iload_1 implements Instruction{
        @Override
        public void execute(Vm vm) {
            vm.pushOpInt(vm.getLocalInt(1));
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.iload_1;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "iload_1";
        }
    }

    public static class iload_2 implements Instruction{
        @Override
        public void execute(Vm vm) {
            vm.pushOpInt(vm.getLocalInt(2));
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.iload_2;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "iload_2";
        }
    }

    public static class astore implements Instruction{
        private int index;
        @Override
        public void execute(Vm vm) {
            vm.setLocalInt(index, vm.popOpInt());
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {
            this.index = code_attribute.readu1();
        }

        @Override
        public int getOpCode() {
            return OpCode.astore;
        }

        @Override
        public int size() {
            return 2;
        }

        @Override
        public String toString() {
            return "astore [" + index + "]";
        }
    }

    public static class astore_2 implements Instruction{

        @Override
        public void execute(Vm vm) {
            vm.setLocalInt(2, vm.popOpRef());
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.astore_2;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "astore_2";
        }
    }

    public static class astore_1 implements Instruction{

        @Override
        public void execute(Vm vm) {
            vm.setLocalInt(1, vm.popOpRef());
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.astore_1;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "astore_1";
        }
    }

    public static class istore implements Instruction{
        private int index;
        @Override
        public void execute(Vm vm) {
            vm.setLocalInt(index, vm.popOpInt());
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {
            this.index = code_attribute.readu1();
        }

        @Override
        public int getOpCode() {
            return OpCode.istore;
        }

        @Override
        public int size() {
            return 2;
        }

        @Override
        public String toString() {
            return "istore [" + index + "]";
        }
    }

    public static class istore_0 implements Instruction{
        @Override
        public void execute(Vm vm) {
            vm.setLocalInt(0, vm.popOpInt());
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }


        @Override
        public int getOpCode() {
            return OpCode.istore_0;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "istore_0";
        }
    }

    public static class istore_1 implements Instruction{
        @Override
        public void execute(Vm vm) {
            vm.setLocalInt(1, vm.popOpInt());
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.istore_1;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "istore_1";
        }
    }

    public static class istore_2 implements Instruction{
        @Override
        public void execute(Vm vm) {
            vm.setLocalInt(2, vm.popOpInt());
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.istore_2;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "istore_2";
        }
    }

    public static class lstore_1 implements Instruction{
        @Override
        public void execute(Vm vm) {
            vm.setLocalLong(1, vm.popOpLong());
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.lstore_1;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "lstore_1";
        }
    }

    public static class dstore_3 implements Instruction{
        @Override
        public void execute(Vm vm) {
            vm.setLocalDouble(3, vm.popOpDouble());
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.dstore_3;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "dstore_3";
        }
    }

    public static class imul implements Instruction{

        @Override
        public void execute(Vm vm) {
            int value2 = vm.popOpInt();
            int value1 = vm.popOpInt();
            vm.pushOpInt(value2 * value1);
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.imul;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "imul";
        }
    }

    public static class iinc implements Instruction{
        private int index;
        private byte value;
        @Override
        public void execute(Vm vm) {
            vm.setLocalInt(index, vm.getLocalInt(index) + value);
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {
            index = code_attribute.readu1();
            value = code_attribute.readByte();
        }

        @Override
        public int getOpCode() {
            return OpCode.iinc;
        }

        @Override
        public int size() {
            return 3;
        }

        @Override
        public String toString() {
            return "iinc @" + index + " " + value;
        }
    }

    public static class invokespecial implements Instruction{

        public invokespecial() {
        }

        private int index;
        @Override
        public void execute(Vm vm) {
            ClassImage.CONSTANT_Methodref_info method_ref = (ClassImage.CONSTANT_Methodref_info) vm.getConstant(index);
            String className = method_ref.getClassName(vm.getCurrentClassImage());
            ClassImage.CONSTANT_NameAndType_info nameAndType = method_ref.getNameAndType(vm.getCurrentClassImage());
            ClassImage classImage = vm.findClassImageByClassName(className);
            String methodName = nameAndType.getName(vm.getCurrentClassImage());
            String methodDescriptor = nameAndType.getDescriptor(vm.getCurrentClassImage());
            ClassImage.method_info method = classImage.getMethodByNameAndType(methodName, methodDescriptor);

            int ref = vm.popOpRef();
            DescriptorParser.MethodType methodType = (MethodType) DescriptorParser.parse(methodDescriptor);
            List<Object> params = new ArrayList<>();
            for(int i = 0; i < methodType.getParams().size(); i++){
                FieldType fieldType = methodType.getParams().get(0);
                switch (fieldType.getTag()){
                    case INT: params.add(vm.popOpInt()); continue;
                    case SHORT: params.add(vm.popOpShort()); continue;
                    case BYTE: params.add(vm.popOpByte()); continue;
                    case FLOAT: params.add(vm.popOpFloat()); continue;
                    case LONG: params.add(vm.popOpLong()); continue;
                    case BOOL: params.add(vm.popOpBool()); continue;
                    case DOUBLE: params.add(vm.popOpDouble()); continue;
                    case CHAR: params.add(vm.popOpChar()); continue;
                    case CLASS:
                    case ARRAY:
                        params.add(vm.popOpRef()); continue;
                }
            }
            vm.pushFrame(method.getIndex(), classImage.hashCode(), size());
            vm.setLocalRef(0, ref);
            for(int i = 0; i < params.size(); i++){
                Object p = params.get(i);
                int index = i + 1;
                if(p instanceof Integer){
                    vm.setLocalInt(index, (Integer) p);
                }else if(p instanceof Short){
                    vm.setLocalShort(index, (Short) p);
                }else if(p instanceof Byte){
                    vm.setLocalByte(index, (Byte) p);
                }else if(p instanceof Float){
                    vm.setLocalFloat(index, (Float) p);
                }else if(p instanceof Long){
                    vm.setLocalLong(index, (Long) p);
                }else if(p instanceof Boolean){
                    vm.setLocalBool(index, (Boolean) p);
                }else if(p instanceof Double){
                    vm.setLocalDouble(index, (Double) p);
                }else if(p instanceof Character){
                    vm.setLocalChar(index, (Character) p);
                }else {
                    vm.setLocalRef(index, (Integer) p);
                }
            }
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {
            index = code_attribute.readu2();
        }

        @Override
        public int getOpCode() {
            return OpCode.invokespecial;
        }

        @Override
        public int size() {
            return 3;
        }

        @Override
        public String toString() {
            return "invokespecial " + index;
        }
    }

    public static class invokestatic implements Instruction{

        public invokestatic() {
        }

        private int index;
        @Override
        public void execute(Vm vm) {

            ClassImage.CONSTANT_Methodref_info method_ref = (ClassImage.CONSTANT_Methodref_info) vm.getConstant(index);
            String className = method_ref.getClassName(vm.getCurrentClassImage());
            ClassImage.CONSTANT_NameAndType_info nameAndType = method_ref.getNameAndType(vm.getCurrentClassImage());
            ClassImage classImage = vm.findClassImageByClassName(className);
            String methodName = nameAndType.getName(vm.getCurrentClassImage());
            String methodDescriptor = nameAndType.getDescriptor(vm.getCurrentClassImage());
            ClassImage.method_info method = classImage.getMethodByNameAndType(methodName, methodDescriptor);
            DescriptorParser.MethodType methodType = (MethodType) DescriptorParser.parse(methodDescriptor);
            if(method.access_flags().contains(ClassImage.method_access_flag.ACC_NATIVE)){
                if(methodType.getReturnType().getTag() == FieldTag.V){
                    vm.increasePc(size());
                    return;
                }
                throw new RuntimeException("Native method not supported: " + className + "." + methodName);
            }
            List<Object> params = new ArrayList<>();
            for(int i = 0; i < methodType.getParams().size(); i++){
                FieldType fieldType = methodType.getParams().get(0);
                switch (fieldType.getTag()){
                    case INT: params.add(vm.popOpInt()); continue;
                    case SHORT: params.add(vm.popOpShort()); continue;
                    case BYTE: params.add(vm.popOpByte()); continue;
                    case FLOAT: params.add(vm.popOpFloat()); continue;
                    case LONG: params.add(vm.popOpLong()); continue;
                    case BOOL: params.add(vm.popOpBool()); continue;
                    case DOUBLE: params.add(vm.popOpDouble()); continue;
                    case CHAR: params.add(vm.popOpChar()); continue;
                    case CLASS:
                    case ARRAY:
                        params.add(vm.popOpRef()); continue;
                }
            }
            vm.pushFrame(method.getIndex(), classImage.hashCode(), size());
            for(int i = 0; i < params.size(); i++){
                Object p = params.get(i);
                int index = i;
                if(p instanceof Integer){
                    vm.setLocalInt(index, (Integer) p);
                }else if(p instanceof Short){
                    vm.setLocalShort(index, (Short) p);
                }else if(p instanceof Byte){
                    vm.setLocalByte(index, (Byte) p);
                }else if(p instanceof Float){
                    vm.setLocalFloat(index, (Float) p);
                }else if(p instanceof Long){
                    vm.setLocalLong(index, (Long) p);
                }else if(p instanceof Boolean){
                    vm.setLocalBool(index, (Boolean) p);
                }else if(p instanceof Double){
                    vm.setLocalDouble(index, (Double) p);
                }else if(p instanceof Character){
                    vm.setLocalChar(index, (Character) p);
                }else {
                    vm.setLocalRef(index, (Integer) p);
                }
            }
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {
            index = code_attribute.readu2();
        }

        @Override
        public int getOpCode() {
            return OpCode.invokespecial;
        }

        @Override
        public int size() {
            return 3;
        }

        @Override
        public String toString() {
            return "invokestatic " + index;
        }
    }

    public static class invokevirtual implements Instruction{

        public invokevirtual() {
        }

        private int index;
        @Override
        public void execute(Vm vm) {

            ClassImage.CONSTANT_Methodref_info method_ref = (ClassImage.CONSTANT_Methodref_info) vm.getConstant(index);
            String className = method_ref.getClassName(vm.getCurrentClassImage());
            ClassImage.CONSTANT_NameAndType_info nameAndType = method_ref.getNameAndType(vm.getCurrentClassImage());
            ClassImage classImage = vm.findClassImageByClassName(className);
            String methodName = nameAndType.getName(vm.getCurrentClassImage());
            String methodDescriptor = nameAndType.getDescriptor(vm.getCurrentClassImage());
            ClassImage.method_info method = classImage.getMethodByNameAndType(methodName, methodDescriptor);
            DescriptorParser.MethodType methodType = (MethodType) DescriptorParser.parse(methodDescriptor);
            if(method.access_flags().contains(ClassImage.method_access_flag.ACC_NATIVE)){
                if(methodType.getReturnType().getTag() == FieldTag.V){
                    vm.increasePc(size());
                    return;
                }
                throw new RuntimeException("Native method not supported: " + className + "." + methodName);
            }

            int ref = vm.popOpRef();
            List<Object> params = new ArrayList<>();
            for(int i = 0; i < methodType.getParams().size(); i++){
                FieldType fieldType = methodType.getParams().get(0);
                switch (fieldType.getTag()){
                    case INT: params.add(vm.popOpInt()); continue;
                    case SHORT: params.add(vm.popOpShort()); continue;
                    case BYTE: params.add(vm.popOpByte()); continue;
                    case FLOAT: params.add(vm.popOpFloat()); continue;
                    case LONG: params.add(vm.popOpLong()); continue;
                    case BOOL: params.add(vm.popOpBool()); continue;
                    case DOUBLE: params.add(vm.popOpDouble()); continue;
                    case CHAR: params.add(vm.popOpChar()); continue;
                    case CLASS:
                    case ARRAY:
                        params.add(vm.popOpRef()); continue;
                }
            }
            vm.pushFrame(method.getIndex(), classImage.hashCode(), size());
            vm.setLocalRef(0, ref);
            for(int i = 0; i < params.size(); i++){
                Object p = params.get(i);
                int index = i + 1;
                if(p instanceof Integer){
                    vm.setLocalInt(index, (Integer) p);
                }else if(p instanceof Short){
                    vm.setLocalShort(index, (Short) p);
                }else if(p instanceof Byte){
                    vm.setLocalByte(index, (Byte) p);
                }else if(p instanceof Float){
                    vm.setLocalFloat(index, (Float) p);
                }else if(p instanceof Long){
                    vm.setLocalLong(index, (Long) p);
                }else if(p instanceof Boolean){
                    vm.setLocalBool(index, (Boolean) p);
                }else if(p instanceof Double){
                    vm.setLocalDouble(index, (Double) p);
                }else if(p instanceof Character){
                    vm.setLocalChar(index, (Character) p);
                }else {
                    vm.setLocalRef(index, (Integer) p);
                }
            }
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {
            index = code_attribute.readu2();
        }

        @Override
        public int getOpCode() {
            return OpCode.invokevirtual;
        }

        @Override
        public int size() {
            return 3;
        }

        @Override
        public String toString() {
            return "invokevirtual " + index;
        }
    }

    public static class putfield implements Instruction{
        private int index;
        @Override
        public void execute(Vm vm) {
            vm.increasePc(size());
            throw new RuntimeException("TODO");
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {
            index = code_attribute.readu2();
        }

        @Override
        public int getOpCode() {
            return OpCode.putfield;
        }

        @Override
        public int size() {
            return 3;
        }

        @Override
        public String toString() {
            return "putfield";
        }
    }

    public static class putstatic implements Instruction{
        private int index;
        @Override
        public void execute(Vm vm) {
            ClassImage.cp_info c = vm.getConstant(index);
            if(!(c instanceof ClassImage.CONSTANT_Fieldref_info)){
                throw new RuntimeException("Not a field ref: @" + index);
            }
            ClassImage.CONSTANT_Fieldref_info field_ref = (ClassImage.CONSTANT_Fieldref_info) c;
            String className = vm.getCurrentClassImage().getClassInfoAt(field_ref.class_index()).getName(vm.getCurrentClassImage());
            ClassImage.CONSTANT_NameAndType_info nameAndType = (ClassImage.CONSTANT_NameAndType_info) vm.getConstant(field_ref.name_and_type_index());
            String fieldName = nameAndType.getName(vm.getCurrentClassImage());
            String fieldDescriptor = nameAndType.getDescriptor(vm.getCurrentClassImage());
            FieldType fieldType = (DescriptorParser.FieldType) DescriptorParser.parse(fieldDescriptor);

            switch (fieldType.getTag()){
                case BOOL:
                    vm.putStatic(className, fieldName, vm.popOpBool());
                    break;
                case INT:
                    vm.putStatic(className, fieldName, vm.popOpInt());
                    break;
                case LONG:
                    vm.putStatic(className, fieldName, vm.popOpLong());
                    break;
                case SHORT:
                    vm.putStatic(className, fieldName, vm.popOpShort());
                    break;
                case CHAR:
                    vm.putStatic(className, fieldName, vm.popOpChar());
                    break;
                case BYTE:
                    vm.putStatic(className, fieldName, vm.popOpByte());
                    break;
                case FLOAT:
                    vm.putStatic(className, fieldName, vm.popOpFloat());
                    break;
                case ARRAY:
                case CLASS:
                    vm.putStatic(className, fieldName, vm.popOpRef());
                    break;
                default:
                    throw new RuntimeException("Should not reach");
            }
            vm.increasePc(size());

        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {
            index = code_attribute.readu2();
        }

        @Override
        public int getOpCode() {
            return OpCode.putstatic;
        }

        @Override
        public int size() {
            return 3;
        }

        @Override
        public String toString() {
            return "putstatic @" + index;
        }
    }

    public static class getstatic implements Instruction{
        private int index;
        @Override
        public void execute(Vm vm) {
            ClassImage.cp_info c = vm.getConstant(index);
            if(!(c instanceof ClassImage.CONSTANT_Fieldref_info)){
                throw new RuntimeException("Not a field ref: @" + index);
            }
            ClassImage.CONSTANT_Fieldref_info field_ref = (ClassImage.CONSTANT_Fieldref_info) c;
            String className = vm.getCurrentClassImage().getClassInfoAt(field_ref.class_index()).getName(vm.getCurrentClassImage());
            ClassImage.CONSTANT_NameAndType_info nameAndType = (ClassImage.CONSTANT_NameAndType_info) vm.getConstant(field_ref.name_and_type_index());
            String fieldName = nameAndType.getName(vm.getCurrentClassImage());
            String fieldDescriptor = nameAndType.getDescriptor(vm.getCurrentClassImage());
            FieldType fieldType = (DescriptorParser.FieldType) DescriptorParser.parse(fieldDescriptor);

            switch (fieldType.getTag()){
                case BOOL:
                    vm.pushOpBool(vm.getStaticHeap().getBool(className, fieldName));
                    break;
                case INT:
                    vm.pushOpInt(vm.getStaticHeap().getInt(className, fieldName));
                    break;
                case LONG:
                    vm.pushOpLong(vm.getStaticHeap().getLong(className, fieldName));
                    break;
                case SHORT:
                    vm.pushOpShort(vm.getStaticHeap().getShort(className, fieldName));
                    break;
                case CHAR:
                    vm.pushOpChar(vm.getStaticHeap().getChar(className, fieldName));
                    break;
                case BYTE:
                    vm.pushOpByte(vm.getStaticHeap().getByte(className, fieldName));
                    break;
                case FLOAT:
                    vm.pushOpFloat(vm.getStaticHeap().getFloat(className, fieldName));
                    break;
                case ARRAY:
                case CLASS:
                    if(!vm.getStaticHeap().contains(className, fieldName)){
                        vm.findClassImageByClassName(className);
                    }
                    vm.pushOpRef(vm.getStaticHeap().getRef(className, fieldName));
                    break;
                default:
                    throw new RuntimeException("Should not reach");
            }
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {
            index = code_attribute.readu2();
        }

        @Override
        public int getOpCode() {
            return OpCode.putstatic;
        }

        @Override
        public int size() {
            return 3;
        }

        @Override
        public String toString() {
            return "getstatic @" + index;
        }
    }

    public static class new_ implements Instruction{
        private int index;

        @Override
        public void execute(Vm vm) {
            String className = vm.getCurrentClassImage().getClassInfoAt(index).getName(vm.getCurrentClassImage());
            int ref = vm.getHeap().createObject(className);
            vm.pushOpRef(ref);
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {
            index = code_attribute.readu2();
        }

        @Override
        public int getOpCode() {
            return OpCode.new_;
        }

        @Override
        public int size() {
            return 3;
        }

        @Override
        public String toString() {
            return "new_ @" + index;
        }
    }

    public static class getfield implements Instruction{
        private int index;

        @Override
        public void execute(Vm vm) {
            int ref = vm.popOpRef();
            HeapObject heapObject = vm.getHeap().get(ref);
            ClassImage.CONSTANT_Fieldref_info fieldRef = vm.getCurrentClassImage().getFieldrefAt(index);
            String className = fieldRef.getClassName(vm.getCurrentClassImage());
            String fieldName = fieldRef.getFieldName(vm.getCurrentClassImage());
            FieldType fieldType = fieldRef.getFileType(vm.getCurrentClassImage());
            if(heapObject == null){
                throw new RuntimeException("Invalid objectref: " + ref);
            }
            switch (fieldType.getTag()){
                case INT: vm.pushOpInt(heapObject.getInt(className, fieldName)); break;
                case FLOAT: vm.pushOpFloat(heapObject.getFloat(className, fieldName)); break;
                case SHORT: vm.pushOpShort(heapObject.getShort(className, fieldName)); break;
                case LONG: vm.pushOpLong(heapObject.getLong(className, fieldName)); break;
                case DOUBLE: vm.pushOpDouble(heapObject.getDouble(className, fieldName)); break;
                case CHAR: vm.pushOpChar(heapObject.getChar(className, fieldName)); break;
                case BOOL: vm.pushOpBool(heapObject.getBool(className, fieldName)); break;
                case BYTE: vm.pushOpByte(heapObject.getByte(className, fieldName)); break;
                case ARRAY: vm.pushOpRef(heapObject.getRef(className, fieldName)); break;
                case CLASS: vm.pushOpRef(heapObject.getRef(className, fieldName)); break;
                default: throw new RuntimeException("Invalid field tag: " + fieldType.getTag());
            }
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {
            index = code_attribute.readu2();
        }

        @Override
        public int getOpCode() {
            return OpCode.getfield;
        }

        @Override
        public int size() {
            return 3;
        }

        @Override
        public String toString() {
            return "getfield @" + index;
        }
    }

    public static class rtn implements Instruction{

        @Override
        public void execute(Vm vm) {
            vm.popFrame();
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.rtn;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "return";
        }
    }

    public static class ireturn implements Instruction{

        @Override
        public void execute(Vm vm) {
            int result = vm.popOpInt();
            vm.popFrame();
            vm.pushOpInt(result);
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.ireturn;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "ireturn";
        }
    }

    public static class areturn implements Instruction{

        @Override
        public void execute(Vm vm) {
            int result = vm.popOpRef();
            vm.popFrame();
            vm.pushOpRef(result);
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.areturn;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "areturn";
        }
    }

    public static class monitorenter implements Instruction{

        @Override
        public void execute(Vm vm) {
            // TODO implement enter monitor
            vm.popOpRef();
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.monitorenter;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "monitorenter";
        }
    }
    public static class monitorexit implements Instruction{

        @Override
        public void execute(Vm vm) {
            // TODO implement exit monitor
            vm.popOpRef();
            vm.increasePc(size());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.monitorexit;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return "monitorexit";
        }
    }
}

