package org.javaexplorer.disassembler.disassembler;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.Deprecated;
import org.apache.bcel.classfile.*;
import org.javaexplorer.model.classfile.DisassembledClassFile;
import org.javaexplorer.model.classfile.annotation.*;
import org.javaexplorer.model.classfile.attribute.*;
import org.javaexplorer.model.classfile.constant.*;
import org.javaexplorer.model.classfile.flag.AccFlag;
import org.javaexplorer.model.classfile.method.MethodInfo;

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

    private MethodInfo getMethodInfo(JavaClass javaClass, Method method){
        List<AttributeInfo> attributes = Arrays.stream(method.getAttributes())
                .map(a -> getAttributeInfo(javaClass, a))
                .collect(Collectors.toList());
        return new MethodInfo(
                unmaskFlag(method.getAccessFlags()),
                method.getNameIndex(),
                method.getName(),
                method.getSignatureIndex(),
                method.getSignature(),
                attributes
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

    private ConstantModuleInfo getConstantModuleInfo(ConstantPool constantPool, Constant constant){
        ConstantModule constantModule = (ConstantModule) constant;
        return new ConstantModuleInfo(
                constantModule.getNameIndex(),
                constantModule.getBytes(constantPool)
        );
    }

    private ConstantPackageInfo getConstantPackageInfo(ConstantPool constantPool, Constant constant){
        ConstantPackage constantPackage = (ConstantPackage) constant;
        return new ConstantPackageInfo(
                constantPackage.getNameIndex(),
                constantPackage.getBytes(constantPool)
        );
    }

    private ConstantInfo constantInfo(ConstantPool constantPool, Constant constant){
        if(constant == null){
            return null;
        }
        int tagValue = byteValue(constant.getTag());
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
            case CONSTANT_Module:
                return getConstantModuleInfo(constantPool, constant);
            case CONSTANT_Package:
                return getConstantPackageInfo(constantPool, constant);
        }
        throw new RuntimeException("Invalid constant " + tagValue);
    }

    private int byteValue(byte b){
        return b & 0xFF;
    }

    private void getConstantPool(JavaClass javaClass, DisassembledClassFile disassembledClassFile){
        List<ConstantInfo> constantPool = Arrays.stream(javaClass.getConstantPool().getConstantPool())
                .map(c -> constantInfo(javaClass.getConstantPool(), c))
                .collect(Collectors.toList());
        disassembledClassFile.setConstantPool(constantPool);

    }

    private void getMethods(JavaClass javaClass, DisassembledClassFile disassembledClassFile){
        List<MethodInfo> methodInfos = Arrays.stream(javaClass.getMethods())
                .map(m -> getMethodInfo(javaClass, m))
                .collect(Collectors.toList());
        disassembledClassFile.setMethods(methodInfos);
    }

    private void getFields(JavaClass javaClass, DisassembledClassFile disassembledClassFile){

    }

    private AttributeInfo getConstantValueAttribute(JavaClass javaClass, Attribute attribute){
        int constantIndex = ((ConstantValue) attribute).getConstantValueIndex();
        ConstantInfo constantInfo = constantInfo(javaClass.getConstantPool(), javaClass.getConstantPool().getConstant(constantIndex));
        return new AttributeConstantValue(
                attribute.getNameIndex(),
                attribute.getName(),
                attribute.getLength(),
                constantIndex,
                constantInfo
        );
    }

    private ExceptionHandler getExceptionHandler(JavaClass javaClass, CodeException codeException){
        return new ExceptionHandler(
                codeException.getStartPC(),
                codeException.getEndPC(),
                codeException.getHandlerPC(),
                codeException.getCatchType(),
                getConstantClassInfo(
                        javaClass.getConstantPool(),
                        javaClass.getConstantPool().getConstant(codeException.getCatchType())
                )
        );
    }

    private AttributeInfo getCodeAttribute(JavaClass javaClass, Attribute attribute){
        Code codeAttribute = (Code) attribute;
        codeAttribute.getAttributes();
        List<AttributeInfo> attributes = Arrays.stream(codeAttribute.getAttributes())
                .map(a -> getAttributeInfo(javaClass, a))
                .collect(Collectors.toList());
        List<ExceptionHandler> exceptionHandlers = Arrays.stream(codeAttribute.getExceptionTable())
                .map(e -> getExceptionHandler(javaClass, e))
                .collect(Collectors.toList());
        int[] code = new int[codeAttribute.getCode().length];
        for(int i = 0; i < code.length; i++){
            code[i] = byteValue(codeAttribute.getCode()[i]);
        }
        return new AttributeCode(
                attribute.getNameIndex(),
                attribute.getName(),
                attribute.getLength(),
                codeAttribute.getMaxStack(),
                codeAttribute.getMaxLocals(),
                code,
                attributes,
                exceptionHandlers
        );
    }

    private AttributeInfo getLineNumberTableAttribute(JavaClass javaClass, Attribute attribute){
        LineNumberTable lineNumberTableAttribute = (LineNumberTable) attribute;
        List<LineNumberInfo> lineNumberTable = Arrays.stream(lineNumberTableAttribute.getLineNumberTable())
                .map(l -> new LineNumberInfo(l.getStartPC(), l.getLineNumber()))
                .collect(Collectors.toList());
        return new AttributeLineNumberTable(
                attribute.getNameIndex(),
                attribute.getName(),
                attribute.getLength(),
                lineNumberTable
        );
    }

    private AttributeInfo getUnknownAttribute(JavaClass javaClass, Attribute attribute){
        Unknown unknownAttribute = (Unknown) attribute;
        int[] bytes = new int[unknownAttribute.getBytes().length];
        for(int i = 0; i < bytes.length; i++){
            bytes[i] = byteValue(unknownAttribute.getBytes()[i]);
        }
        return new AttributeUnknown(
                unknownAttribute.getNameIndex(),
                unknownAttribute.getName(),
                unknownAttribute.getLength(),
                bytes
        );
    }

    private AttributeInfo getSourceFileAttribute(JavaClass javaClass, Attribute attribute){
        SourceFile sourceFile = (SourceFile) attribute;
        return new AttributeSourceFile(
                sourceFile.getNameIndex(),
                sourceFile.getName(),
                sourceFile.getLength(),
                sourceFile.getSourceFileIndex(),
                sourceFile.getSourceFileName()
        );
    }

    public AttributeDeprecated getDeprecatedAttribute(JavaClass javaClass, Attribute attribute){
        Deprecated deprecated = (Deprecated) attribute;
        return new AttributeDeprecated(
                deprecated.getNameIndex(),
                deprecated.getName(),
                deprecated.getLength()
        );
    }
    private ElementValueInfo getElementValue(JavaClass javaClass, ElementValue elementValue){
        if(elementValue instanceof ClassElementValue){
            ClassElementValue classElement = (ClassElementValue) elementValue;
            return new ElementValueClass(
                    classElement.getIndex(),
                    classElement.getClassString()
            );
        }
        if(elementValue instanceof ArrayElementValue){
            ArrayElementValue arrayElement = (ArrayElementValue) elementValue;
            return new ElementValueArray(
                    Arrays.stream(arrayElement.getElementValuesArray())
                            .map(e -> getElementValue(javaClass, e))
                            .collect(Collectors.toList())
            );
        }
        if(elementValue instanceof AnnotationElementValue){
            AnnotationElementValue annotationElement = (AnnotationElementValue) elementValue;
            return new ElementValueAnnotation(
                    getAnnotationInfo(javaClass, annotationElement.getAnnotationEntry())
            );
        }
        if(elementValue instanceof EnumElementValue){
            EnumElementValue enumElement = (EnumElementValue) elementValue;
            return new ElementValueEnum(
                enumElement.getTypeIndex(),
                enumElement.getEnumTypeString(),
                enumElement.getValueIndex(),
                enumElement.getEnumValueString()
            );
        }
        if(elementValue instanceof SimpleElementValue){
            SimpleElementValue simpleElementValue = (SimpleElementValue) elementValue;
            switch (simpleElementValue.getElementValueType()){
                case ElementValue.PRIMITIVE_BYTE: return new ElementValueByte(
                        simpleElementValue.getIndex(),
                        simpleElementValue.getValueByte()
                );
                case ElementValue.PRIMITIVE_CHAR: return new ElementValueChar(
                        simpleElementValue.getIndex(),
                        simpleElementValue.getValueChar()
                );
                case ElementValue.PRIMITIVE_DOUBLE: return new ElementValueDouble(
                        simpleElementValue.getIndex(),
                        simpleElementValue.getValueDouble()
                );
                case ElementValue.PRIMITIVE_FLOAT: return new ElementValueFloat(
                        simpleElementValue.getIndex(),
                        simpleElementValue.getValueFloat()
                );
                case ElementValue.PRIMITIVE_INT: return new ElementValueInt(
                        simpleElementValue.getIndex(),
                        simpleElementValue.getValueInt()
                );
                case ElementValue.PRIMITIVE_LONG: return new ElementValueLong(
                        simpleElementValue.getIndex(),
                        simpleElementValue.getValueLong()
                );
                case ElementValue.PRIMITIVE_SHORT: return new ElementValueShort(
                        simpleElementValue.getIndex(),
                        simpleElementValue.getValueShort()
                );
                case ElementValue.PRIMITIVE_BOOLEAN: return new ElementValueBool(
                        simpleElementValue.getIndex(),
                        simpleElementValue.getValueBoolean()
                );
                case ElementValue.STRING: return new ElementValueString(
                        simpleElementValue.getIndex(),
                        simpleElementValue.getValueString()
                );
            }
            throw new RuntimeException("element value type: " + simpleElementValue.getElementValueType());
        }
        throw new RuntimeException("Invalid element value type: " + elementValue.getElementValueType());
    }
    public ElementValuePairInfo getElementValuePair(JavaClass javaClass, ElementValuePair elementValuePair){
        return new ElementValuePairInfo(
                elementValuePair.getNameIndex(),
                elementValuePair.getNameString(),
                getElementValue(javaClass, elementValuePair.getValue())
        );
    }

    AnnotationInfo getAnnotationInfo(JavaClass javaClass, AnnotationEntry annotationEntry){
        List<ElementValuePairInfo> elementValuePairs = Arrays.stream(annotationEntry.getElementValuePairs())
                .map(a -> getElementValuePair(javaClass, a))
                .collect(Collectors.toList());
        return new AnnotationInfo(
                annotationEntry.getTypeIndex(),
                annotationEntry.getAnnotationType(),
                elementValuePairs
        );
    }

    public AttributeRuntimeVisibleAnnotations getRuntimeVisibleAnnotationsAttribute(JavaClass javaClass, Attribute attribute){
        RuntimeVisibleAnnotations runtimeVisibleAnnotations = (RuntimeVisibleAnnotations) attribute;
        List<AnnotationInfo> annotations = Arrays.stream(runtimeVisibleAnnotations.getAnnotationEntries())
                .map(a -> getAnnotationInfo(javaClass, a))
                .collect(Collectors.toList());
        return new AttributeRuntimeVisibleAnnotations(
                runtimeVisibleAnnotations.getNameIndex(),
                runtimeVisibleAnnotations.getName(),
                runtimeVisibleAnnotations.getLength(),
                annotations
        );
    }

    public AttributeRuntimeInvisibleAnnotations getRuntimeInvisibleAnnotationsAttribute(JavaClass javaClass, Attribute attribute){
        RuntimeInvisibleAnnotations runtimeInvisibleAnnotations = (RuntimeInvisibleAnnotations) attribute;
        List<AnnotationInfo> annotations = Arrays.stream(runtimeInvisibleAnnotations.getAnnotationEntries())
                .map(a -> getAnnotationInfo(javaClass, a))
                .collect(Collectors.toList());
        return new AttributeRuntimeInvisibleAnnotations(
                runtimeInvisibleAnnotations.getNameIndex(),
                runtimeInvisibleAnnotations.getName(),
                runtimeInvisibleAnnotations.getLength(),
                annotations
        );
    }

    public AttributeInfo getAttributeInfo(JavaClass javaClass, Attribute attribute){
        if(attribute instanceof ConstantValue){
            return getConstantValueAttribute(javaClass, attribute);
        }
        if(attribute instanceof Code){
            return getCodeAttribute(javaClass, attribute);
        }
        if(attribute instanceof Unknown){
            return getUnknownAttribute(javaClass, attribute);
        }
        if(attribute instanceof SourceFile){
            return getSourceFileAttribute(javaClass, attribute);
        }
        if(attribute instanceof LineNumberTable){
            return getLineNumberTableAttribute(javaClass, attribute);
        }
        if(attribute instanceof Deprecated){
            return getDeprecatedAttribute(javaClass, attribute);
        }
        if(attribute instanceof RuntimeVisibleAnnotations){
            return getRuntimeVisibleAnnotationsAttribute(javaClass, attribute);
        }
        if(attribute instanceof RuntimeInvisibleAnnotations){
            return getRuntimeInvisibleAnnotationsAttribute(javaClass, attribute);
        }
        throw new RuntimeException("Invalid attribute " + attribute.getName());
    }

    private void getAttributes(JavaClass javaClass, DisassembledClassFile disassembledClassFile){
        List<AttributeInfo> attributes = Arrays.stream(javaClass.getAttributes())
                .map(a -> getAttributeInfo(javaClass, a))
                .collect(Collectors.toList());
        disassembledClassFile.setAttributes(attributes);
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
