package org.javaexplorer.disassembler.disassembler;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.*;
import org.javaexplorer.model.classfile.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Disassembler {
    private static final String CLASS_FILE_NAME = "example.class";
    public DisassembledClassFile disassemble(byte[] classFileBinary) throws IOException {
        JavaClass javaClass = new ClassParser(
                new ByteArrayInputStream(classFileBinary),
                CLASS_FILE_NAME
        ).parse();
        DisassembledClassFile classFile = new DisassembledClassFile();
        getMetaData(javaClass, classFile);
        getConstantPool(javaClass, classFile);
        getMethods(javaClass, classFile);
        getFields(javaClass, classFile);
        getAttributes(javaClass, classFile);
        return classFile;
    }

    private ConstantClassInfo getConstantClassInfo(ConstantPool constantPool, Constant constant){
        ConstantClass constantClass = (ConstantClass) constant;
        return new ConstantClassInfo(
                constantClass.getNameIndex(),
                getConstantUtf8Info(constantPool.getConstant(constantClass.getNameIndex())).toString()
        );
    }

    private ConstantFieldrefInfo getConstantFieldRefInfo(ConstantPool constantPool, Constant constant){
        ConstantFieldref constantFieldref = (ConstantFieldref) constant;
        ConstantNameAndTypeInfo nameAndType = getConstantNameAndTypeInfo(constantPool, constantPool.getConstant(constantFieldref.getNameAndTypeIndex()));
        return new ConstantFieldrefInfo(
                constantFieldref.getClassIndex(),
                constantFieldref.getNameAndTypeIndex(),
                constantPool.getConstantString(constantFieldref.getClassIndex(), Const.CONSTANT_Class),
                nameAndType.toString()
            );
    }

    private ConstantMethodrefInfo getConstantMethodRefInfo(ConstantPool constantPool, Constant constant){
        ConstantMethodref constantMethodref = (ConstantMethodref) constant;
        ConstantNameAndTypeInfo nameAndType = getConstantNameAndTypeInfo(constantPool, constantPool.getConstant(constantMethodref.getNameAndTypeIndex()));
        return new ConstantMethodrefInfo(
                constantMethodref.getClassIndex(),
                constantMethodref.getNameAndTypeIndex(),
                constantPool.getConstantString(constantMethodref.getClassIndex(), Const.CONSTANT_Class),
                nameAndType.toString()
        );
    }

    private ConstantInterfaceMethodrefInfo getConstantInterfaceMethodRefInfo(ConstantPool constantPool, Constant constant){
        ConstantInterfaceMethodref constantInterfaceMethodref = (ConstantInterfaceMethodref) constant;
        return new ConstantInterfaceMethodrefInfo(
                constantInterfaceMethodref.getClassIndex(),
                constantInterfaceMethodref.getNameAndTypeIndex(),
                constantPool.getConstantString(constantInterfaceMethodref.getClassIndex(), Const.CONSTANT_Class),
                constantPool.getConstantString(constantInterfaceMethodref.getNameAndTypeIndex(), Const.CONSTANT_NameAndType)
        );
    }

    private ConstantStringInfo getConstantStringInfo(ConstantPool constantPool, Constant constant){
        ConstantString constantString = (ConstantString) constant;
        return new ConstantStringInfo(
                constantString.getStringIndex(),
                getConstantUtf8Info(constantPool.getConstant(constantString.getStringIndex())).getValue()
        );
    }

    private ConstantIntegerInfo getConstantIntegerInfo(Constant constant){
        ConstantInteger constantInteger = (ConstantInteger) constant;
        return new ConstantIntegerInfo(constantInteger.getBytes());
    }

    private ConstantFloatInfo getConstantFloatInfo(Constant constant){
        ConstantFloat constantFloat = (ConstantFloat) constant;
        return new ConstantFloatInfo(constantFloat.getBytes());
    }

    private ConstantLongInfo getConstantLongInfo(Constant constant){
        ConstantLong constantFloat = (ConstantLong) constant;
        return new ConstantLongInfo(constantFloat.getBytes());
    }

    private ConstantDoubleInfo getConstantDoubleInfo(Constant constant){
        ConstantDouble constantFloat = (ConstantDouble) constant;
        return new ConstantDoubleInfo(constantFloat.getBytes());
    }

    private ConstantNameAndTypeInfo getConstantNameAndTypeInfo(ConstantPool constantPool, Constant constant){
        ConstantNameAndType constantNameAndType = (ConstantNameAndType) constant;
        return new ConstantNameAndTypeInfo(
                constantNameAndType.getNameIndex(),
                constantNameAndType.getSignatureIndex(),
                getConstantUtf8Info(constantPool.getConstant(constantNameAndType.getNameIndex())).getValue(),
                getConstantUtf8Info(constantPool.getConstant(constantNameAndType.getSignatureIndex())).getValue()
        );
    }

    private ConstantUtf8Info getConstantUtf8Info(Constant constant){
        ConstantUtf8 constantUtf8 = (ConstantUtf8) constant;
        return new ConstantUtf8Info(constantUtf8.getBytes());
    }

    private ConstantMethodHandleInfo getConstantMethodHandleInfo(ConstantPool constantPool, Constant constant){
        ConstantMethodHandle constantMethodHandle = (ConstantMethodHandle) constant;
        return new ConstantMethodHandleInfo(
                constantMethodHandle.getReferenceKind(),
                constantMethodHandle.getReferenceIndex()
        );
    }

    private ConstantMethodTypeInfo getConstantMethodTypeInfo(ConstantPool constantPool, Constant constant){
        ConstantMethodType constantMethodType = (ConstantMethodType) constant;
        return new ConstantMethodTypeInfo(
                constantMethodType.getDescriptorIndex(),
                constantPool.getConstantString(constantMethodType.getDescriptorIndex(), Const.CONSTANT_Utf8)
        );
    }

    private ConstantInvokeDynamicInfo getConstantInvokeDynamicInfo(ConstantPool constantPool, Constant constant){
        ConstantInvokeDynamic constantInvokeDynamic = (ConstantInvokeDynamic) constant;
        Constant nameAndType = constantPool.getConstant(constantInvokeDynamic.getNameAndTypeIndex());
        ConstantNameAndTypeInfo constantNameAndTypeInfo = getConstantNameAndTypeInfo(constantPool, nameAndType);
        return new ConstantInvokeDynamicInfo(
                constantInvokeDynamic.getNameAndTypeIndex(),
                constantNameAndTypeInfo.toString(),
                constantInvokeDynamic.getBootstrapMethodAttrIndex()
        );
    }

    private ConstantInfo constantInfo(ConstantPool constantPool, Constant constant){
        if(constant == null){
            return null;
        }
        int tagValue = tagValue(constant.getTag());
        ConstantTag tag = ConstantTag.valueOf(tagValue);
        switch (tag){
            case CONSTANT_Class:
                return getConstantClassInfo(constantPool, constant);
            case CONSTANT_Fieldref:
                return getConstantFieldRefInfo(constantPool, constant);
            case CONSTANT_Methodref:
                return getConstantMethodRefInfo(constantPool, constant);
            case CONSTANT_InterfaceMethodref:
                return getConstantInterfaceMethodRefInfo(constantPool, constant);
            case CONSTANT_String:
                return getConstantStringInfo(constantPool, constant);
            case CONSTANT_Integer:
                return getConstantIntegerInfo(constant);
            case CONSTANT_Float:
                return getConstantFloatInfo(constant);
            case CONSTANT_Double:
                return getConstantDoubleInfo(constant);
            case CONSTANT_NameAndType:
                return getConstantNameAndTypeInfo(constantPool, constant);
            case CONSTANT_Utf8:
                return getConstantUtf8Info(constant);
            case CONSTANT_MethodHandle:
                return getConstantMethodHandleInfo(constantPool, constant);
            case CONSTANT_MethodType:
                return getConstantMethodTypeInfo(constantPool, constant);
            case CONSTANT_InvokeDynamic:
                return getConstantInvokeDynamicInfo(constantPool, constant);
        }
        throw new RuntimeException("Invalid constant " + tagValue);
    }

    private int tagValue(byte b){
        return b & 0xFF;
    }

    private void getConstantPool(JavaClass javaClass, DisassembledClassFile disassembledClassFile){
        List<ConstantInfo> constantPool = Arrays.stream(javaClass.getConstantPool().getConstantPool())
                .map(c -> constantInfo(javaClass.getConstantPool(), c))
                .collect(Collectors.toList());
        disassembledClassFile.setConstantPool(constantPool);

    }
    private void getMethods(JavaClass javaClass, DisassembledClassFile disassembledClassFile){

    }
    private void getFields(JavaClass javaClass, DisassembledClassFile disassembledClassFile){

    }
    private void getAttributes(JavaClass javaClass, DisassembledClassFile disassembledClassFile){

    }

    private void getMetaData(JavaClass javaClass, DisassembledClassFile disassembledClassFile){
        disassembledClassFile.setMajorVersion(javaClass.getMajor());
        disassembledClassFile.setMinorVersion(javaClass.getMinor());
        disassembledClassFile.setSourceFileName(javaClass.getSourceFileName());
        disassembledClassFile.setClassName(new ConstantClassInfo(javaClass.getClassNameIndex(), javaClass.getClassName()));
        disassembledClassFile.setAccessFlags(unmaskFlag(javaClass.getAccessFlags()));
        if(javaClass.getSuperclassName() != null){
            disassembledClassFile.setSuperClassName(new ConstantClassInfo(javaClass.getSuperclassNameIndex(), javaClass.getSuperclassName()));
        }

        disassembledClassFile.setInterfaces(new ArrayList<>());
        for(int i = 0; i < javaClass.getInterfaceIndices().length; i++){
            disassembledClassFile.getInterfaces().add(
                    new ConstantClassInfo(
                            javaClass.getInterfaceIndices()[i],
                            javaClass.getInterfaceNames()[i]
                    )
            );
        }
    }

    public List<AccFlag> unmaskFlag(int accessFlag){
        List<AccFlag> accFlags = new ArrayList<>();
        for(AccFlag accFlag : AccFlag.values()){
            if((accessFlag & accFlag.getValue()) > 0){
                accFlags.add(accFlag);
            }
        }
        return accFlags;
    }
}
