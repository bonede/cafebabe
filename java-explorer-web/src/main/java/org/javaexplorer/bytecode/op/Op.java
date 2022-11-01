package org.javaexplorer.bytecode.op;

import org.javaexplorer.bytecode.op.DescriptorParser.FieldTag;
import org.javaexplorer.bytecode.op.DescriptorParser.FieldType;
import org.javaexplorer.bytecode.op.DescriptorParser.MethodType;
import org.javaexplorer.bytecode.vm.ClassImage;
import org.javaexplorer.bytecode.vm.HeapObject;
import org.javaexplorer.bytecode.vm.Vm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Ref https://docs.oracle.com/javase/specs/jvms/se11/html/jvms-6.html
 */
public class Op {
    public static abstract class OpCode{
        public static final int aconst_null = 1;
        public static final int bipush = 16;
        public static final int dconst_0 = 14;
        public static final int dconst_1 = 15;
        public static final int fconst_0 = 11;
        public static final int fconst_1 = 12;
        public static final int fconst_2 = 13;
        public static final int iconst_0 = 3;
        public static final int iconst_1 = 4;
        public static final int iconst_2 = 5;
        public static final int iconst_3 = 6;
        public static final int iconst_4 = 7;
        public static final int iconst_5 = 8;
        public static final int iconst_m1 = 2;
        public static final int lconst_0 = 9;
        public static final int lconst_1 = 10;
        public static final int ldc = 18;
        public static final int ldc_w = 19;
        public static final int ldc2_w = 20;
        public static final int sipush = 17;
        public static final int aload = 25;
        public static final int aload_0 = 42;
        public static final int aload_1 = 43;
        public static final int aload_2 = 44;
        public static final int aload_3 = 45;
        public static final int dload = 24;
        public static final int dload_0 = 38;
        public static final int dload_1 = 39;
        public static final int dload_2 = 40;
        public static final int dload_3 = 41;
        public static final int fload = 23;
        public static final int fload_0 = 34;
        public static final int fload_1 = 35;
        public static final int fload_2 = 36;
        public static final int fload_3 = 37;
        public static final int iload = 21;
        public static final int iload_0 = 26;
        public static final int iload_1 = 27;
        public static final int iload_2 = 28;
        public static final int iload_3 = 29;
        public static final int lload = 22;
        public static final int lload_0 = 30;
        public static final int lload_1 = 31;
        public static final int lload_2 = 32;
        public static final int lload_3 = 33;
        public static final int astore = 58;
        public static final int astore_0 = 75;
        public static final int astore_1 = 76;
        public static final int astore_2 = 77;
        public static final int astore_3 = 78;
        public static final int dstore = 57;
        public static final int dstore_0 = 71;
        public static final int dstore_1 = 72;
        public static final int dstore_2 = 73;
        public static final int dstore_3 = 74;
        public static final int fstore = 56;
        public static final int fstore_0 = 67;
        public static final int fstore_1 = 68;
        public static final int fstore_2 = 69;
        public static final int fstore_3 = 70;
        public static final int istore = 54;
        public static final int istore_0 = 59;
        public static final int istore_1 = 60;
        public static final int istore_2 = 61;
        public static final int istore_3 = 62;
        public static final int lstore = 55;
        public static final int lstore_0 = 63;
        public static final int lstore_1 = 64;
        public static final int lstore_2 = 65;
        public static final int lstore_3 = 66;
        public static final int dup = 89;
        public static final int dup_x1 = 90;
        public static final int dup_x2 = 91;
        public static final int dup2 = 92;
        public static final int dup2_x1 = 93;
        public static final int dup2_x2 = 94;
        public static final int pop = 87;
        public static final int pop2 = 88;
        public static final int swap = 95;
        public static final int dadd = 99;
        public static final int ddiv = 111;
        public static final int dmul = 107;
        public static final int dneg = 119;
        public static final int drem = 115;
        public static final int dsub = 103;
        public static final int fadd = 98;
        public static final int fdiv = 110;
        public static final int fmul = 106;
        public static final int fneg = 118;
        public static final int frem = 114;
        public static final int fsub = 102;
        public static final int iadd = 96;
        public static final int iand = 126;
        public static final int idiv = 108;
        public static final int iinc = 132;
        public static final int imul = 104;
        public static final int ineg = 116;
        public static final int ior = 128;
        public static final int irem = 112;
        public static final int ishl = 120;
        public static final int ishr = 122;
        public static final int isub = 100;
        public static final int iushr = 124;
        public static final int ixor = 130;
        public static final int ladd = 97;
        public static final int land = 127;
        public static final int ldiv = 109;
        public static final int lmul = 105;
        public static final int lneg = 117;
        public static final int lor = 129;
        public static final int lrem = 113;
        public static final int lshl = 121;
        public static final int lshr = 123;
        public static final int lsub = 101;
        public static final int lushr = 125;
        public static final int lxor = 131;
        public static final int d2f = 144;
        public static final int d2i = 142;
        public static final int d2l = 143;
        public static final int f2d = 141;
        public static final int f2i = 139;
        public static final int f2l = 140;
        public static final int i2b = 145;
        public static final int i2c = 146;
        public static final int i2d = 135;
        public static final int i2f = 134;
        public static final int i2l = 133;
        public static final int i2s = 147;
        public static final int l2d = 138;
        public static final int l2f = 137;
        public static final int l2i = 136;
        public static final int dcmpg = 152;
        public static final int dcmpl = 151;
        public static final int fcmpg = 150;
        public static final int fcmpl = 149;
        public static final int lcmp = 148;
        public static final int if_acmpeq = 165;
        public static final int if_acmpne = 166;
        public static final int if_icmpeq = 159;
        public static final int if_icmpge = 162;
        public static final int if_icmpgt = 163;
        public static final int if_icmple = 164;
        public static final int if_icmplt = 161;
        public static final int if_icmpne = 160;
        public static final int ifeq = 153;
        public static final int ifge = 156;
        public static final int ifgt = 157;
        public static final int ifle = 158;
        public static final int iflt = 155;
        public static final int ifne = 154;
        public static final int ifnonnull = 199;
        public static final int ifnull = 198;
        public static final int lookupswitch = 171;
        public static final int tableswitch = 170;
        public static final int checkcast = 192;
        public static final int getfield = 180;
        public static final int getstatic = 178;
        public static final int instanceof_ = 193;
        public static final int invokedynamic = 186;
        public static final int invokeinterface = 185;
        public static final int invokespecial = 183;
        public static final int invokestatic = 184;
        public static final int invokevirtual = 182;
        public static final int new_ = 187;
        public static final int putfield = 181;
        public static final int putstatic = 179;
        public static final int aaload = 50;
        public static final int aastore = 83;
        public static final int anewarray = 189;
        public static final int arraylength = 190;
        public static final int baload = 51;
        public static final int bastore = 84;
        public static final int caload = 52;
        public static final int castore = 85;
        public static final int daload = 49;
        public static final int dastore = 82;
        public static final int faload = 48;
        public static final int fastore = 81;
        public static final int iaload = 46;
        public static final int iastore = 79;
        public static final int laload = 47;
        public static final int lastore = 80;
        public static final int multianewarray = 197;
        public static final int newarray = 188;
        public static final int saload = 53;
        public static final int sastore = 86;
        public static final int areturn = 176;
        public static final int athrow = 191;
        public static final int dreturn = 175;
        public static final int freturn = 174;
        public static final int goto_ = 167;
        public static final int goto_w = 200;
        public static final int ireturn = 172;
        public static final int jsr = 168;
        public static final int jsr_w = 201;
        public static final int lreturn = 173;
        public static final int ret = 169;
        public static final int return_ = 177;
        public static final int monitorenter = 194;
        public static final int monitorexit = 195;
        public static final int nop = 0;
        public static final int wide = 196;
        public static final int breakpoint = 202;
        public static final int impdep1 = 254;
        public static final int impdep2 = 255;


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
            case OpCode.dconst_0: return new dconst_0();
            case OpCode.dconst_1: return new dconst_1();
            case OpCode.fconst_0: return new fconst_0();
            case OpCode.fconst_1: return new fconst_1();
            case OpCode.fconst_2: return new fconst_2();
            case OpCode.iconst_0: return new iconst_0();
            case OpCode.iconst_1: return new iconst_1();
            case OpCode.iconst_2: return new iconst_2();
            case OpCode.iconst_3: return new iconst_3();
            case OpCode.iconst_4: return new iconst_4();
            case OpCode.iconst_5: return new iconst_5();
            case OpCode.iconst_m1: return new iconst_m1();
            case OpCode.lconst_0: return new lconst_0();
            case OpCode.lconst_1: return new lconst_1();
            case OpCode.ldc: return new ldc();
            case OpCode.ldc_w: return new ldc_w();
            case OpCode.ldc2_w: return new ldc2_w();
            case OpCode.sipush: return new sipush();
            case OpCode.aload: return new aload();
            case OpCode.aload_0: return new aload_0();
            case OpCode.aload_1: return new aload_1();
            case OpCode.aload_2: return new aload_2();
            case OpCode.aload_3: return new aload_3();
            case OpCode.dload: return new dload();
            case OpCode.dload_0: return new dload_0();
            case OpCode.dload_1: return new dload_1();
            case OpCode.dload_2: return new dload_2();
            case OpCode.dload_3: return new dload_3();
            case OpCode.fload: return new fload();
            case OpCode.fload_0: return new fload_0();
            case OpCode.fload_1: return new fload_1();
            case OpCode.fload_2: return new fload_2();
            case OpCode.fload_3: return new fload_3();
            case OpCode.iload: return new iload();
            case OpCode.iload_0: return new iload_0();
            case OpCode.iload_1: return new iload_1();
            case OpCode.iload_2: return new iload_2();
            case OpCode.iload_3: return new iload_3();
            case OpCode.lload: return new lload();
            case OpCode.lload_0: return new lload_0();
            case OpCode.lload_1: return new lload_1();
            case OpCode.lload_2: return new lload_2();
            case OpCode.lload_3: return new lload_3();
            case OpCode.astore: return new astore();
            case OpCode.astore_0: return new astore_0();
            case OpCode.astore_1: return new astore_1();
            case OpCode.astore_2: return new astore_2();
            case OpCode.astore_3: return new astore_3();
            case OpCode.dstore: return new dstore();
            case OpCode.dstore_0: return new dstore_0();
            case OpCode.dstore_1: return new dstore_1();
            case OpCode.dstore_2: return new dstore_2();
            case OpCode.dstore_3: return new dstore_3();
            case OpCode.fstore: return new fstore();
            case OpCode.fstore_0: return new fstore_0();
            case OpCode.fstore_1: return new fstore_1();
            case OpCode.fstore_2: return new fstore_2();
            case OpCode.fstore_3: return new fstore_3();
            case OpCode.istore: return new istore();
            case OpCode.istore_0: return new istore_0();
            case OpCode.istore_1: return new istore_1();
            case OpCode.istore_2: return new istore_2();
            case OpCode.istore_3: return new istore_3();
            case OpCode.lstore: return new lstore();
            case OpCode.lstore_0: return new lstore_0();
            case OpCode.lstore_1: return new lstore_1();
            case OpCode.lstore_2: return new lstore_2();
            case OpCode.lstore_3: return new lstore_3();
            case OpCode.dup: return new dup();
            case OpCode.dup_x1: return new dup_x1();
            case OpCode.dup_x2: return new dup_x2();
            case OpCode.dup2: return new dup2();
            case OpCode.dup2_x1: return new dup2_x1();
            case OpCode.dup2_x2: return new dup2_x2();
            case OpCode.pop: return new pop();
            case OpCode.pop2: return new pop2();
            case OpCode.swap: return new swap();
            case OpCode.dadd: return new dadd();
            case OpCode.ddiv: return new ddiv();
            case OpCode.dmul: return new dmul();
            case OpCode.dneg: return new dneg();
            case OpCode.drem: return new drem();
            case OpCode.dsub: return new dsub();
            case OpCode.fadd: return new fadd();
            case OpCode.fdiv: return new fdiv();
            case OpCode.fmul: return new fmul();
            case OpCode.fneg: return new fneg();
            case OpCode.frem: return new frem();
            case OpCode.fsub: return new fsub();
            case OpCode.iadd: return new iadd();
            case OpCode.iand: return new iand();
            case OpCode.idiv: return new idiv();
            case OpCode.iinc: return new iinc();
            case OpCode.imul: return new imul();
            case OpCode.ineg: return new ineg();
            case OpCode.ior: return new ior();
            case OpCode.irem: return new irem();
            case OpCode.ishl: return new ishl();
            case OpCode.ishr: return new ishr();
            case OpCode.isub: return new isub();
            case OpCode.iushr: return new iushr();
            case OpCode.ixor: return new ixor();
            case OpCode.ladd: return new ladd();
            case OpCode.land: return new land();
            case OpCode.ldiv: return new ldiv();
            case OpCode.lmul: return new lmul();
            case OpCode.lneg: return new lneg();
            case OpCode.lor: return new lor();
            case OpCode.lrem: return new lrem();
            case OpCode.lshl: return new lshl();
            case OpCode.lshr: return new lshr();
            case OpCode.lsub: return new lsub();
            case OpCode.lushr: return new lushr();
            case OpCode.lxor: return new lxor();
            case OpCode.d2f: return new d2f();
            case OpCode.d2i: return new d2i();
            case OpCode.d2l: return new d2l();
            case OpCode.f2d: return new f2d();
            case OpCode.f2i: return new f2i();
            case OpCode.f2l: return new f2l();
            case OpCode.i2b: return new i2b();
            case OpCode.i2c: return new i2c();
            case OpCode.i2d: return new i2d();
            case OpCode.i2f: return new i2f();
            case OpCode.i2l: return new i2l();
            case OpCode.i2s: return new i2s();
            case OpCode.l2d: return new l2d();
            case OpCode.l2f: return new l2f();
            case OpCode.l2i: return new l2i();
            case OpCode.dcmpg: return new dcmpg();
            case OpCode.dcmpl: return new dcmpl();
            case OpCode.fcmpg: return new fcmpg();
            case OpCode.fcmpl: return new fcmpl();
            case OpCode.lcmp: return new lcmp();
            case OpCode.if_acmpeq: return new if_acmpeq();
            case OpCode.if_acmpne: return new if_acmpne();
            case OpCode.if_icmpeq: return new if_icmpeq();
            case OpCode.if_icmpge: return new if_icmpge();
            case OpCode.if_icmpgt: return new if_icmpgt();
            case OpCode.if_icmple: return new if_icmple();
            case OpCode.if_icmplt: return new if_icmplt();
            case OpCode.if_icmpne: return new if_icmpne();
            case OpCode.ifeq: return new ifeq();
            case OpCode.ifge: return new ifge();
            case OpCode.ifgt: return new ifgt();
            case OpCode.ifle: return new ifle();
            case OpCode.iflt: return new iflt();
            case OpCode.ifne: return new ifne();
            case OpCode.ifnonnull: return new ifnonnull();
            case OpCode.ifnull: return new ifnull();
            case OpCode.lookupswitch: return new lookupswitch();
            case OpCode.tableswitch: return new tableswitch();
            case OpCode.checkcast: return new checkcast();
            case OpCode.getfield: return new getfield();
            case OpCode.getstatic: return new getstatic();
            case OpCode.instanceof_: return new instanceof_();
            case OpCode.invokedynamic: return new invokedynamic();
            case OpCode.invokeinterface: return new invokeinterface();
            case OpCode.invokespecial: return new invokespecial();
            case OpCode.invokestatic: return new invokestatic();
            case OpCode.invokevirtual: return new invokevirtual();
            case OpCode.new_: return new new_();
            case OpCode.putfield: return new putfield();
            case OpCode.putstatic: return new putstatic();
            case OpCode.aaload: return new aaload();
            case OpCode.aastore: return new aastore();
            case OpCode.anewarray: return new anewarray();
            case OpCode.arraylength: return new arraylength();
            case OpCode.baload: return new baload();
            case OpCode.bastore: return new bastore();
            case OpCode.caload: return new caload();
            case OpCode.castore: return new castore();
            case OpCode.daload: return new daload();
            case OpCode.dastore: return new dastore();
            case OpCode.faload: return new faload();
            case OpCode.fastore: return new fastore();
            case OpCode.iaload: return new iaload();
            case OpCode.iastore: return new iastore();
            case OpCode.laload: return new laload();
            case OpCode.lastore: return new lastore();
            case OpCode.multianewarray: return new multianewarray();
            case OpCode.newarray: return new newarray();
            case OpCode.saload: return new saload();
            case OpCode.sastore: return new sastore();
            case OpCode.areturn: return new areturn();
            case OpCode.athrow: return new athrow();
            case OpCode.dreturn: return new dreturn();
            case OpCode.freturn: return new freturn();
            case OpCode.goto_: return new goto_();
            case OpCode.goto_w: return new goto_w();
            case OpCode.ireturn: return new ireturn();
            case OpCode.jsr: return new jsr();
            case OpCode.jsr_w: return new jsr_w();
            case OpCode.lreturn: return new lreturn();
            case OpCode.ret: return new ret();
            case OpCode.return_: return new return_();
            case OpCode.monitorenter: return new monitorenter();
            case OpCode.monitorexit: return new monitorexit();
            case OpCode.nop: return new nop();
            case OpCode.wide: return new wide();
            case OpCode.breakpoint: return new breakpoint();
            case OpCode.impdep1: return new impdep1();
            case OpCode.impdep2: return new impdep2();

        }
        throw new RuntimeException("Invalid opcode: " + opCode);
    }
    public static class aconst_null implements Instruction{
        private static final String mnemonic = "aconst_null";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        @Override
        public void execute(Vm vm) {
            vm.pushOpNull();
            vm.increasePc(getSize());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.aconst_null;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class dup implements Instruction{
        private static final String mnemonic = "dup";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }

        @Override
        public void execute(Vm vm) {
            int value = vm.popOpInt();
            vm.pushOpInt(value);
            vm.pushOpInt(value);
            vm.increasePc(getSize());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.dup;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class bipush implements Instruction{
        private static final String mnemonic = "bipush";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        private byte value;

        public int getValue(){
            return Byte.toUnsignedInt(value);
        }

        public bipush() {

        }

        @Override
        public void execute(Vm vm) {
            vm.pushOpByte(value);
            vm.increasePc(getSize());
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
        public int getSize() {
            return 2;
        }

        @Override
        public String toString() {
            return getOpMnemonic() + " : " + value;
        }
    }

    public static class iconst_0 implements Instruction{
        private static final String mnemonic = "iconst_0";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        @Override
        public void execute(Vm vm) {
            vm.pushOpInt(0);
            vm.increasePc(getSize());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }


        @Override
        public int getOpCode() {
            return OpCode.iconst_0;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class iconst_1 implements Instruction{
        private static final String mnemonic = "iconst_1";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }

        @Override
        public void execute(Vm vm) {
            vm.pushOpInt(1);
            vm.increasePc(getSize());
        }
        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }
        @Override
        public int getOpCode() {
            return OpCode.iconst_1;
        }
        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class iconst_2 implements Instruction{
        private static final String mnemonic = "iconst_2";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        @Override
        public void execute(Vm vm) {
            vm.pushOpInt(2);
            vm.increasePc(getSize());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.iconst_2;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class iconst_3 implements Instruction{
        private static final String mnemonic = "iconst_3";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        @Override
        public void execute(Vm vm) {
            vm.pushOpInt(3);
            vm.increasePc(getSize());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.iconst_3;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class iconst_4 implements Instruction{
        private static final String mnemonic = "iconst_4";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        @Override
        public void execute(Vm vm) {
            vm.pushOpInt(4);
            vm.increasePc(getSize());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }


        @Override
        public int getOpCode() {
            return OpCode.iconst_4;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class iconst_5 implements Instruction{
        private static final String mnemonic = "iconst_5";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        @Override
        public void execute(Vm vm) {
            vm.pushOpInt(5);
            vm.increasePc(getSize());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.iconst_5;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class dconst_0 implements Instruction{
        private static final String mnemonic = "dconst_0";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        @Override
        public void execute(Vm vm) {
            vm.pushOpDouble(0d);
            vm.increasePc(getSize());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.dconst_0;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class dconst_1 implements Instruction{
        private static final String mnemonic = "dconst_1";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        @Override
        public void execute(Vm vm) {
            vm.pushOpDouble(1d);
            vm.increasePc(getSize());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.dconst_1;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class ldc2_w implements Instruction{
        private static final String mnemonic = "ldc2_w";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        private int index;

        public int getIndex(){
            return index;
        }

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
            vm.increasePc(getSize());
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
        public int getSize() {
            return 3;
        }

        @Override
        public String toString() {
            return mnemonic + " #" + index;
        }
    }

    /**
     * <a href="https://docs.oracle.com/javase/specs/jvms/se11/html/jvms-6.html#jvms-6.5.ldc">Ref</a>
     */
    public static class ldc implements Instruction{
        private static final String mnemonic = "ldc";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        private int index;
        public int getIndex(){
            return index;
        }
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
                String className = ((ClassImage.CONSTANT_Class_info) c).getName();
                vm.pushOpRef(vm.getClassImageByName(className).hashCode());
            }else {
                // TODO implement push reference to Class, Method
                throw new RuntimeException("Must be int,float,short,string constant: " + c.getClass());
            }
            vm.increasePc(getSize());
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
        public int getSize() {
            return 2;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class sipush implements Instruction{
        private static final String mnemonic = "sipush";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        private short value;
        @Override
        public void execute(Vm vm) {
            vm.pushOpShort(value);
            vm.increasePc(getSize());
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
        public int getSize() {
            return 3;
        }

        @Override
        public String toString() {
            return mnemonic + " " + value;
        }
    }

    public static class if_icmpeq implements Instruction{
        private static final String mnemonic = "if_icmpeq";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        private int offset;
        public int getOffset(){
            return offset;
        }
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
                vm.increasePc(getSize());
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
        public int getSize() {
            return 3;
        }

        @Override
        public String toString() {
            return mnemonic + " " + offset;
        }
    }

    public static class if_icmpne implements Instruction{
        private static final String mnemonic = "if_icmpne";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        private int offset;
        public int getOffset(){
            return offset;
        }
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
                vm.increasePc(getSize());
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
        public int getSize() {
            return 3;
        }

        @Override
        public String toString() {
            return mnemonic + " " + offset;
        }
    }

    public static class if_icmplt implements Instruction{
        private static final String mnemonic = "if_icmplt";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        public int getOffset(){
            return offset;
        }
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
                vm.increasePc(getSize());
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
        public int getSize() {
            return 3;
        }

        @Override
        public String toString() {
            return mnemonic + " " + offset;
        }
    }

    public static class if_icmpge implements Instruction{
        private static final String mnemonic = "if_icmpge";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        public int getOffset(){
            return offset;
        }
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
                vm.increasePc(getSize());
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
        public int getSize() {
            return 3;
        }

        @Override
        public String toString() {
            return mnemonic + " " + offset;
        }
    }

    public static class ifnonnull implements Instruction{
        private static final String mnemonic = "ifnonnull";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        public int getOffset(){
            return offset;
        }
        private int offset;

        public ifnonnull() {

        }

        @Override
        public void execute(Vm vm) {
            int ref = vm.popOpRef();
            if(vm.getHeap().contains(ref)){
                vm.increasePc(offset);
            }else {
                vm.increasePc(getSize());
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
        public int getSize() {
            return 3;
        }

        @Override
        public String toString() {
            return  mnemonic + " " + offset;
        }
    }

    public static class goto_ implements Instruction{
        private static final String mnemonic = "goto";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        public int getOffset(){
            return offset;
        }
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
        public int getSize() {
            return 3;
        }

        @Override
        public String toString() {
            return mnemonic + " " + offset;
        }
    }

    public static class fconst_1 implements Instruction{
        private static final String mnemonic = "fconst_1";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        @Override
        public void execute(Vm vm) {
            vm.pushOpFloat(1f);
            vm.increasePc(getSize());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.fconst_1;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class fconst_2 implements Instruction{
        private static final String mnemonic = "fconst_2";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        @Override
        public void execute(Vm vm) {
            vm.pushOpFloat(2f);
            vm.increasePc(getSize());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.fconst_2;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class aload implements Instruction{
        private static final String mnemonic = "aload";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        private int index;
        public int getIndex(){
            return index;
        }
        public aload(int index) {
            this.index = index;
        }

        public aload() {

        }

        @Override
        public void execute(Vm vm) {
            vm.pushOpInt(vm.getLocalInt(index));
            vm.increasePc(getSize());
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
        public int getSize() {
            return 2;
        }

        @Override
        public String toString() {
            return mnemonic + " #" + index;
        }
    }

    public static class aload_0 implements Instruction{
        private static final String mnemonic = "aload_0";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        public aload_0() {
        }

        @Override
        public void execute(Vm vm) {
            vm.pushOpInt(vm.getLocalInt(0));
            vm.increasePc(getSize());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }
        @Override
        public int getOpCode() {
            return OpCode.aload_0;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }


    public static class aload_1 implements Instruction{
        private static final String mnemonic = "aload_1";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        public aload_1() {
        }

        @Override
        public void execute(Vm vm) {
            vm.pushOpInt(vm.getLocalInt(1));
            vm.increasePc(getSize());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }
        @Override
        public int getOpCode() {
            return OpCode.aload_1;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class iload implements Instruction{
        private static final String mnemonic = "iload";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }

        public int getIndex() {
            return index;
        }

        private int index;

        public iload() {

        }

        @Override
        public void execute(Vm vm) {
            vm.pushOpInt(vm.getLocalInt(index));
            vm.increasePc(getSize());
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
        public int getSize() {
            return 2;
        }

        @Override
        public String toString() {
            return mnemonic + " #" + index;
        }
    }

    public static class iadd implements Instruction{
        private static final String mnemonic = "iadd";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        @Override
        public void execute(Vm vm) {
            vm.pushOpInt(vm.popOpInt() + vm.popOpInt());
            vm.increasePc(getSize());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.iadd;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class iload_0 implements Instruction{
        private static final String mnemonic = "iload_0";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        @Override
        public void execute(Vm vm) {
            vm.pushOpInt(vm.getLocalInt(0));
            vm.increasePc(getSize());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.iload_0;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class iload_1 implements Instruction{
        private static final String mnemonic = "iload_1";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        @Override
        public void execute(Vm vm) {
            vm.pushOpInt(vm.getLocalInt(1));
            vm.increasePc(getSize());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.iload_1;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class iload_2 implements Instruction{
        private static final String mnemonic = "iload_2";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        @Override
        public void execute(Vm vm) {
            vm.pushOpInt(vm.getLocalInt(2));
            vm.increasePc(getSize());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.iload_2;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class astore implements Instruction{
        private static final String mnemonic = "astore";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }

        public int getIndex() {
            return index;
        }

        private int index;
        @Override
        public void execute(Vm vm) {
            vm.setLocalInt(index, vm.popOpInt());
            vm.increasePc(getSize());
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
        public int getSize() {
            return 2;
        }

        @Override
        public String toString() {
            return mnemonic + " #" + index;
        }
    }

    public static class astore_2 implements Instruction{
        private static final String mnemonic = "astore_2";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        @Override
        public void execute(Vm vm) {
            vm.setLocalInt(2, vm.popOpRef());
            vm.increasePc(getSize());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.astore_2;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class astore_1 implements Instruction{
        private static final String mnemonic = "astore_1";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        @Override
        public void execute(Vm vm) {
            vm.setLocalInt(1, vm.popOpRef());
            vm.increasePc(getSize());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.astore_1;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class istore implements Instruction{
        private static final String mnemonic = "istore";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }

        public int getIndex() {
            return index;
        }

        private int index;
        @Override
        public void execute(Vm vm) {
            vm.setLocalInt(index, vm.popOpInt());
            vm.increasePc(getSize());
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
        public int getSize() {
            return 2;
        }

        @Override
        public String toString() {
            return mnemonic + " #" + index;
        }
    }

    public static class istore_0 implements Instruction{
        private static final String mnemonic = "istore_0";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }

        @Override
        public void execute(Vm vm) {
            vm.setLocalInt(0, vm.popOpInt());
            vm.increasePc(getSize());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }


        @Override
        public int getOpCode() {
            return OpCode.istore_0;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class istore_1 implements Instruction{
        private static final String mnemonic = "istore_1";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        @Override
        public void execute(Vm vm) {
            vm.setLocalInt(1, vm.popOpInt());
            vm.increasePc(getSize());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.istore_1;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class istore_2 implements Instruction{
        private static final String mnemonic = "istore_2";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        @Override
        public void execute(Vm vm) {
            vm.setLocalInt(2, vm.popOpInt());
            vm.increasePc(getSize());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.istore_2;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class lstore_1 implements Instruction{
        private static final String mnemonic = "lstore_1";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        @Override
        public void execute(Vm vm) {
            vm.setLocalLong(1, vm.popOpLong());
            vm.increasePc(getSize());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.lstore_1;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class dstore_3 implements Instruction{
        private static final String mnemonic = "dstore_3";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        @Override
        public void execute(Vm vm) {
            vm.setLocalDouble(3, vm.popOpDouble());
            vm.increasePc(getSize());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.dstore_3;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class imul implements Instruction{
        private static final String mnemonic = "imul";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        @Override
        public void execute(Vm vm) {
            int value2 = vm.popOpInt();
            int value1 = vm.popOpInt();
            vm.pushOpInt(value2 * value1);
            vm.increasePc(getSize());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.imul;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class iinc implements Instruction{
        private static final String mnemonic = "iinc";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }

        public int getIndex() {
            return index;
        }

        public int getValue() {
            return Byte.toUnsignedInt(value);
        }

        private int index;
        private byte value;
        @Override
        public void execute(Vm vm) {
            vm.setLocalInt(index, vm.getLocalInt(index) + value);
            vm.increasePc(getSize());
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
        public int getSize() {
            return 3;
        }

        @Override
        public String toString() {
            return mnemonic + " #" + index + " " + value;
        }
    }

    public static class invokespecial implements Instruction{
        private static final String mnemonic = "invokespecial";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        public invokespecial() {
        }

        public int getIndex() {
            return index;
        }

        private int index;
        @Override
        public void execute(Vm vm) {
            ClassImage.CONSTANT_Methodref_info method_ref = (ClassImage.CONSTANT_Methodref_info) vm.getConstant(index);
            String className = method_ref.getClassName();
            ClassImage.CONSTANT_NameAndType_info nameAndType = method_ref.getNameAndType();
            ClassImage classImage = vm.findClassImageByClassName(className);
            String methodName = nameAndType.getName();
            String methodDescriptor = nameAndType.getDescriptor();
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
            vm.pushFrame(method.getIndex(), classImage.hashCode(), getSize());
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
        public int getSize() {
            return 3;
        }

        @Override
        public String toString() {
            return mnemonic + " #" + index;
        }
    }

    public static class invokestatic implements Instruction{
        private static final String mnemonic = "invokestatic";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        public invokestatic() {
        }

        public int getIndex() {
            return index;
        }

        private int index;
        @Override
        public void execute(Vm vm) {

            ClassImage.CONSTANT_Methodref_info method_ref = (ClassImage.CONSTANT_Methodref_info) vm.getConstant(index);
            String className = method_ref.getClassName();
            ClassImage.CONSTANT_NameAndType_info nameAndType = method_ref.getNameAndType();
            ClassImage classImage = vm.findClassImageByClassName(className);
            String methodName = nameAndType.getName();
            String methodDescriptor = nameAndType.getDescriptor();
            ClassImage.method_info method = classImage.getMethodByNameAndType(methodName, methodDescriptor);
            DescriptorParser.MethodType methodType = (MethodType) DescriptorParser.parse(methodDescriptor);
            if(method.access_flags().contains(ClassImage.method_access_flag.ACC_NATIVE)){
                if(methodType.getReturnType().getTag() == FieldTag.V){
                    vm.increasePc(getSize());
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
            vm.pushFrame(method.getIndex(), classImage.hashCode(), getSize());
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
        public int getSize() {
            return 3;
        }

        @Override
        public String toString() {
            return mnemonic + " #" + index;
        }
    }

    public static class invokevirtual implements Instruction{
        private static final String mnemonic = "invokevirtual";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        public invokevirtual() {
        }

        public int getIndex() {
            return index;
        }

        private int index;
        @Override
        public void execute(Vm vm) {

            ClassImage.CONSTANT_Methodref_info method_ref = (ClassImage.CONSTANT_Methodref_info) vm.getConstant(index);
            String className = method_ref.getClassName();
            ClassImage.CONSTANT_NameAndType_info nameAndType = method_ref.getNameAndType();
            ClassImage classImage = vm.findClassImageByClassName(className);
            String methodName = nameAndType.getName();
            String methodDescriptor = nameAndType.getDescriptor();
            ClassImage.method_info method = classImage.getMethodByNameAndType(methodName, methodDescriptor);
            DescriptorParser.MethodType methodType = (MethodType) DescriptorParser.parse(methodDescriptor);
            if(method.access_flags().contains(ClassImage.method_access_flag.ACC_NATIVE)){
                if(methodType.getReturnType().getTag() == FieldTag.V){
                    vm.increasePc(getSize());
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
            vm.pushFrame(method.getIndex(), classImage.hashCode(), getSize());
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
        public int getSize() {
            return 3;
        }

        @Override
        public String toString() {
            return mnemonic + " " + index;
        }
    }

    public static class putfield implements Instruction{
        private static final String mnemonic = "putfield";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }

        public int getIndex() {
            return index;
        }

        private int index;
        @Override
        public void execute(Vm vm) {
            vm.increasePc(getSize());
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
        public int getSize() {
            return 3;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class putstatic implements Instruction{
        private static final String mnemonic = "putstatic";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }

        public int getIndex() {
            return index;
        }

        private int index;
        @Override
        public void execute(Vm vm) {
            ClassImage.cp_info c = vm.getConstant(index);
            if(!(c instanceof ClassImage.CONSTANT_Fieldref_info)){
                throw new RuntimeException("Not a field ref: @" + index);
            }
            ClassImage.CONSTANT_Fieldref_info field_ref = (ClassImage.CONSTANT_Fieldref_info) c;
            String className = vm.getCurrentClassImage().getClassInfoAt(field_ref.class_index()).getName();
            ClassImage.CONSTANT_NameAndType_info nameAndType = (ClassImage.CONSTANT_NameAndType_info) vm.getConstant(field_ref.name_and_type_index());
            String fieldName = nameAndType.getName();
            String fieldDescriptor = nameAndType.getDescriptor();
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
            vm.increasePc(getSize());

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
        public int getSize() {
            return 3;
        }

        @Override
        public String toString() {
            return mnemonic + " #" + index;
        }
    }

    public static class getstatic implements Instruction{
        private static final String mnemonic = "getstatic";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }

        public int getIndex() {
            return index;
        }

        private int index;
        @Override
        public void execute(Vm vm) {
            ClassImage.cp_info c = vm.getConstant(index);
            if(!(c instanceof ClassImage.CONSTANT_Fieldref_info)){
                throw new RuntimeException("Not a field ref: @" + index);
            }
            ClassImage.CONSTANT_Fieldref_info field_ref = (ClassImage.CONSTANT_Fieldref_info) c;
            String className = vm.getCurrentClassImage().getClassInfoAt(field_ref.class_index()).getName();
            ClassImage.CONSTANT_NameAndType_info nameAndType = (ClassImage.CONSTANT_NameAndType_info) vm.getConstant(field_ref.name_and_type_index());
            String fieldName = nameAndType.getName();
            String fieldDescriptor = nameAndType.getDescriptor();
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
            vm.increasePc(getSize());
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
        public int getSize() {
            return 3;
        }

        @Override
        public String toString() {
            return mnemonic + " #" + index;
        }
    }

    public static class new_ implements Instruction{
        private static final String mnemonic = "new";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }

        public int getIndex() {
            return index;
        }

        private int index;

        @Override
        public void execute(Vm vm) {
            String className = vm.getCurrentClassImage().getClassInfoAt(index).getName();
            int ref = vm.getHeap().createObject(className);
            vm.pushOpRef(ref);
            vm.increasePc(getSize());
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
        public int getSize() {
            return 3;
        }

        @Override
        public String toString() {
            return mnemonic + " #" + index;
        }
    }

    public static class getfield implements Instruction{
        private static final String mnemonic = "getfield";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }

        public int getIndex() {
            return index;
        }

        private int index;

        @Override
        public void execute(Vm vm) {
            int ref = vm.popOpRef();
            HeapObject heapObject = vm.getHeap().get(ref);
            ClassImage.CONSTANT_Fieldref_info fieldRef = vm.getCurrentClassImage().getFieldrefAt(index);
            String className = fieldRef.getClassName();
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
            vm.increasePc(getSize());
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
        public int getSize() {
            return 3;
        }

        @Override
        public String toString() {
            return mnemonic + " " + index;
        }
    }

    public static class return_ implements Instruction{
        private static final String mnemonic = "return";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        @Override
        public void execute(Vm vm) {
            vm.popFrame();
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.return_;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class ireturn implements Instruction{
        private static final String mnemonic = "ireturn";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
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
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class areturn implements Instruction{
        private static final String mnemonic = "areturn";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
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
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    public static class monitorenter implements Instruction{
        private static final String mnemonic = "monitorenter";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        @Override
        public void execute(Vm vm) {
            // TODO implement enter monitor
            vm.popOpRef();
            vm.increasePc(getSize());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.monitorenter;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }
    public static class monitorexit implements Instruction{
        private static final String mnemonic = "monitorexit";

        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }
        @Override
        public void execute(Vm vm) {
            // TODO implement exit monitor
            vm.popOpRef();
            vm.increasePc(getSize());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.monitorexit;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    private static class nop implements Instruction {
        private static final String mnemonic = "nop";
        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }

        @Override
        public void execute(Vm vm) {
            vm.increasePc(getSize());
        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {

        }

        @Override
        public int getOpCode() {
            return OpCode.nop;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }

    /**
     * Ref https://docs.oracle.com/javase/specs/jvms/se11/html/jvms-6.html#jvms-6.5.wide
     */
    private static class wide implements Instruction {
        private static final String mnemonic = "wide";
        private int opCode;
        private int index;
        private int value;
        private int size;
        @Override
        public String getOpMnemonic() {
            return mnemonic;
        }

        @Override
        public void execute(Vm vm) {

        }

        @Override
        public void parse(ClassImage.Code_attribute code_attribute) {
            opCode = code_attribute.readu1();
            index = code_attribute.readu2();
            int[] ops = new int[]{
                    OpCode.iload,
                    OpCode.fload,
                    OpCode.aload,
                    OpCode.lload,
                    OpCode.dload,
                    OpCode.fstore,
                    OpCode.astore,
                    OpCode.lstore,
                    OpCode.dstore,
                    OpCode.ret,
            };
            if(opCode == OpCode.iinc){
                value = code_attribute.readu2();
                size = 6;
            }else if(Arrays.asList(ops).contains(opCode)) {
                size = 4;
            }
        }

        @Override
        public int getOpCode() {
            return OpCode.wide;
        }

        @Override
        public int getSize() {
            return 0;
        }
    }
}

