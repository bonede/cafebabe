package org.javaexplorer.disassembler.disassembler;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.Deprecated;
import org.apache.bcel.classfile.*;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.javaexplorer.error.ApiException;
import org.javaexplorer.model.classfile.DisassembledClassFile;
import org.javaexplorer.model.classfile.annotation.*;
import org.javaexplorer.model.classfile.attribute.MethodParameter;
import org.javaexplorer.model.classfile.attribute.*;
import org.javaexplorer.model.classfile.constant.*;
import org.javaexplorer.model.classfile.field.FieldInfo;
import org.javaexplorer.model.classfile.flag.*;
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
                getConstantUtf8Info(constantPool.getConstant(constantClass.getNameIndex())).getValue()
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
                unmaskAccFlag(method.getAccessFlags()),
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
                getConstantNameAndTypeInfo(constantPool, constantPool.getConstant(constantInterfaceMethodref.getNameAndTypeIndex())).toString()
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
                getConstantUtf8Info(constantPool.getConstant(constantMethodType.getDescriptorIndex())).getValue()
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
    private FieldInfo getField(JavaClass javaClass, Field field){
        List<AttributeInfo> attributes = Arrays.stream(field.getAttributes())
                .map(a -> getAttributeInfo(javaClass, a))
                .collect(Collectors.toList());
        return new FieldInfo(
                unmaskAccFlag(field.getAccessFlags()),
                field.getNameIndex(),
                field.getName(),
                field.getSignatureIndex(),
                field.getSignature(),
                attributes
        );
    }
    private void getFields(JavaClass javaClass, DisassembledClassFile disassembledClassFile){
        List<FieldInfo> fields = Arrays.stream(javaClass.getFields())
                .map(f -> getField(javaClass, f))
                .collect(Collectors.toList());
        disassembledClassFile.setFields(fields);
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

    private List<AnnotationInfo> getAnnotations(JavaClass javaClass, AnnotationEntry[] annotationEntries){
        return Arrays.stream(annotationEntries)
                .map(e -> getAnnotationInfo(javaClass, e))
                .collect(Collectors.toList());
    }

    public AttributeRuntimeInvisibleAnnotations getRuntimeInvisibleAnnotationsAttribute(JavaClass javaClass, Attribute attribute){
        RuntimeInvisibleAnnotations runtimeInvisibleAnnotations = (RuntimeInvisibleAnnotations) attribute;
        List<AnnotationInfo> annotations = getAnnotations(javaClass, runtimeInvisibleAnnotations.getAnnotationEntries());
        return new AttributeRuntimeInvisibleAnnotations(
                runtimeInvisibleAnnotations.getNameIndex(),
                runtimeInvisibleAnnotations.getName(),
                runtimeInvisibleAnnotations.getLength(),
                annotations
        );
    }
    public AttributeRuntimeInvisibleParameterAnnotations getRuntimeInvisibleParameterAnnotationsAttribute(JavaClass javaClass, Attribute attribute){
        RuntimeInvisibleParameterAnnotations runtimeInvisibleParameterAnnotations = (RuntimeInvisibleParameterAnnotations) attribute;
        List<ParameterAnnotationInfo> annotations = Arrays.stream(runtimeInvisibleParameterAnnotations.getParameterAnnotationTable())
                .map(p -> new ParameterAnnotationInfo(getAnnotations(javaClass, p.getAnnotationEntries())))
                .collect(Collectors.toList());
        return new AttributeRuntimeInvisibleParameterAnnotations(
                runtimeInvisibleParameterAnnotations.getNameIndex(),
                runtimeInvisibleParameterAnnotations.getName(),
                runtimeInvisibleParameterAnnotations.getLength(),
                annotations
        );
    }

    public AttributeRuntimeVisibleParameterAnnotations getRuntimeVisibleParameterAnnotationsAttribute(JavaClass javaClass, Attribute attribute){
        RuntimeVisibleParameterAnnotations runtimeInvisibleParameterAnnotations = (RuntimeVisibleParameterAnnotations) attribute;
        List<ParameterAnnotationInfo> annotations = Arrays.stream(runtimeInvisibleParameterAnnotations.getParameterAnnotationTable())
                .map(p -> new ParameterAnnotationInfo(getAnnotations(javaClass, p.getAnnotationEntries())))
                .collect(Collectors.toList());
        return new AttributeRuntimeVisibleParameterAnnotations(
                runtimeInvisibleParameterAnnotations.getNameIndex(),
                runtimeInvisibleParameterAnnotations.getName(),
                runtimeInvisibleParameterAnnotations.getLength(),
                annotations
        );
    }

    private AttributeInfo getSignatureAttribute(JavaClass javaClass, Attribute attribute){
        Signature signature = (Signature) attribute;
        return new AttributeSignature(
                signature.getNameIndex(),
                signature.getName(),
                signature.getLength(),
                signature.getSignatureIndex(),
                signature.getSignature()
        );
    }

    public AttributeAnnotationDefaultInfo getAnnotationDefaultAttribute(JavaClass javaClass, Attribute attribute){
        AnnotationDefault annotationDefault = (AnnotationDefault) attribute;
        return new AttributeAnnotationDefaultInfo(
                annotationDefault.getNameIndex(),
                annotationDefault.getName(),
                annotationDefault.getLength(),
                getElementValue(javaClass, annotationDefault.getDefaultValue())
        );
    }
    public AttributeStackMapTable getStackMapTableAttribute(JavaClass javaClass, Attribute attribute){
        StackMap stackMap = (StackMap) attribute;
        List<StackMapFrame> stackFrames = Arrays.stream(stackMap.getStackMap()).map(
                e -> {
                    List<StackMapTypeInfo> typeOfLocals = Arrays.stream(e.getTypesOfLocals())
                            .map(t -> new StackMapTypeInfo(t.getType(), t.getIndex()))
                            .collect(Collectors.toList());
                    List<StackMapTypeInfo> typeOfStackItems = Arrays.stream(e.getTypesOfStackItems())
                            .map(t -> new StackMapTypeInfo(t.getType(), t.getIndex()))
                            .collect(Collectors.toList());
                    return new StackMapFrame(
                            e.getFrameType(),
                            e.getByteCodeOffset(),
                            typeOfLocals,
                            typeOfStackItems
                    );
                }).collect(Collectors.toList());
        return new AttributeStackMapTable(
                attribute.getNameIndex(),
                attribute.getName(),
                attribute.getLength(),
                stackFrames
        );
    }
    public AttributeExceptions getStackExceptionTableAttribute(JavaClass javaClass, Attribute attribute){
        ExceptionTable exceptionTable = (ExceptionTable) attribute;
        List<ConstantClassInfo> classInfos = Arrays.stream(exceptionTable.getExceptionIndexTable())
                .mapToObj(i -> getConstantClassInfo(javaClass.getConstantPool(), javaClass.getConstantPool().getConstant(i)))
                .collect(Collectors.toList());
        return new AttributeExceptions(
                exceptionTable.getNameIndex(),
                exceptionTable.getName(),
                exceptionTable.getLength(),
                classInfos
        );
    }

    private InnerClassInfo getInnerClassInfo(JavaClass javaClass, InnerClass innerClass){
        String innerClassName = getConstantClassInfo(
                javaClass.getConstantPool(),
                javaClass.getConstantPool().getConstant(innerClass.getInnerClassIndex())
        ).getName();
        String outerClassName = innerClass.getOuterClassIndex() == 0 ? null :
                getConstantClassInfo(
                        javaClass.getConstantPool(),
                        javaClass.getConstantPool().getConstant(innerClass.getOuterClassIndex())
                ).getName();
        String innerName = innerClass.getOuterClassIndex() == 0 ? null :
                getConstantClassInfo(
                        javaClass.getConstantPool(),
                        javaClass.getConstantPool().getConstant(innerClass.getOuterClassIndex())
                ).getName();
        return new InnerClassInfo(
                innerClass.getInnerClassIndex(),
                innerClassName,
                innerClass.getOuterClassIndex(),
                outerClassName,
                innerClass.getInnerNameIndex(),
                innerName,
                unmaskAccFlag(innerClass.getInnerAccessFlags())
        );
    }

    public AttributeInnerClasses getInnerClassesAttribute(JavaClass javaClass, Attribute attribute){
        InnerClasses innerClasses = (InnerClasses) attribute;
        List<InnerClassInfo> innerClassInfos = Arrays.stream(innerClasses.getInnerClasses())
                .map(i -> getInnerClassInfo(javaClass, i))
                .collect(Collectors.toList());
        return new AttributeInnerClasses(
                innerClasses.getNameIndex(),
                innerClasses.getName(),
                innerClasses.getLength(),
                innerClassInfos
        );
    }

    public AttributeEnclosingMethod getEnclosingMethodAttribute(JavaClass javaClass, Attribute attribute){
        EnclosingMethod enclosingMethod = (EnclosingMethod) attribute;
        return new AttributeEnclosingMethod(
               enclosingMethod.getNameIndex(),
                enclosingMethod.getName(),
                enclosingMethod.getLength(),
                enclosingMethod.getEnclosingClassIndex(),
                getConstantClassInfo(javaClass.getConstantPool(), javaClass.getConstantPool().getConstant(((EnclosingMethod) attribute).getEnclosingClassIndex())).getName(),
                enclosingMethod.getEnclosingMethodIndex(),
                getConstantNameAndTypeInfo(javaClass.getConstantPool(), javaClass.getConstantPool().getConstant(enclosingMethod.getEnclosingMethodIndex())).toString()
        );
    }

    public AttributeSynthetic getSyntheticAttribute(JavaClass javaClass, Attribute attribute){
        Synthetic synthetic = (Synthetic) attribute;
        return new AttributeSynthetic(
                synthetic.getNameIndex(),
                synthetic.getName(),
                synthetic.getLength()
        );
    }
    public LocalVariableInfo getLocalVariable(JavaClass javaClass, LocalVariable localVariable){
        return new LocalVariableInfo(
                localVariable.getStartPC(),
                localVariable.getLength(),
                localVariable.getNameIndex(),
                localVariable.getName(),
                localVariable.getSignatureIndex(),
                localVariable.getSignature(),
                localVariable.getIndex()
        );
    }

    public AttributeLocalVariableTable getLocalVariableTableAttribute(JavaClass javaClass, Attribute attribute){
        LocalVariableTable localVariableTable = (LocalVariableTable) attribute;
        List<LocalVariableInfo> localVariableInfos = Arrays.stream(localVariableTable.getLocalVariableTable())
                .map(l -> getLocalVariable(javaClass, l))
                .collect(Collectors.toList());
        return new AttributeLocalVariableTable(
                localVariableTable.getNameIndex(),
                localVariableTable.getName(),
                localVariableTable.getLength(),
                localVariableInfos
        );
    }

    public LocalVariableTypeInfo getLocalVariableType(JavaClass javaClass, LocalVariable localVariable){
        return new LocalVariableTypeInfo(
                localVariable.getStartPC(),
                localVariable.getLength(),
                localVariable.getNameIndex(),
                localVariable.getName(),
                localVariable.getSignatureIndex(),
                localVariable.getSignature(),
                localVariable.getIndex()
        );
    }
    public AttributeLocalVariableTypeTable getLocalVariableTypeTableAttribute(JavaClass javaClass, Attribute attribute){
        LocalVariableTypeTable localVariableTypeTable = (LocalVariableTypeTable) attribute;
        List<LocalVariableTypeInfo> localVariableInfos = Arrays.stream(localVariableTypeTable.getLocalVariableTypeTable())
                .map(l -> getLocalVariableType(javaClass, l))
                .collect(Collectors.toList());
        return new AttributeLocalVariableTypeTable(
                localVariableTypeTable.getNameIndex(),
                localVariableTypeTable.getName(),
                localVariableTypeTable.getLength(),
                localVariableInfos
        );
    }
    private BootstrapMethodInfo getBootStrapMethod(JavaClass javaClass, BootstrapMethod bootstrapMethod){
        return new BootstrapMethodInfo(
                bootstrapMethod.getBootstrapMethodRef(),
                bootstrapMethod.getBootstrapArguments()
        );
    }
    public AttributeBootstrapMethods getBootstrapMethodsAttribute(JavaClass javaClass, Attribute attribute){
        BootstrapMethods bootstrapMethods = (BootstrapMethods) attribute;
        List<BootstrapMethodInfo> bootstrapMethodInfos = Arrays.stream(bootstrapMethods.getBootstrapMethods())
                .map(m -> getBootStrapMethod(javaClass, m))
                .collect(Collectors.toList());
        return new AttributeBootstrapMethods(
                bootstrapMethods.getNameIndex(),
                bootstrapMethods.getName(),
                bootstrapMethods.getLength(),
                bootstrapMethodInfos
        );
    }
    public MethodParameter getMethodParameter(JavaClass javaClass, org.apache.bcel.classfile.MethodParameter methodParameter){
        return new MethodParameter(
                methodParameter.getNameIndex(),
                methodParameter.getParameterName(javaClass.getConstantPool()),
                unmaskAccFlag(methodParameter.getAccessFlags())
        );
    }

    public AttributeMethodParameters getMethodParametersAttribute(JavaClass javaClass, Attribute attribute){
        MethodParameters methodParameters = (MethodParameters) attribute;
        List<MethodParameter> bootstrapMethodInfos = Arrays.stream(methodParameters.getParameters())
                .map(m -> getMethodParameter(javaClass, m))
                .collect(Collectors.toList());
        return new AttributeMethodParameters(
                methodParameters.getNameIndex(),
                methodParameters.getName(),
                methodParameters.getLength(),
                bootstrapMethodInfos
        );
    }

    List<ModFlag> unmaskModFlags(int flags){
        List<ModFlag> modFlags = new ArrayList<>();
        for(ModFlag modFlag : ModFlag.values()){
            if((flags & modFlag.getValue()) > 0){
                modFlags.add(modFlag);
            }
        }
        return modFlags;
    }

    List<ExpFlag> unmaskExpFlags(int flags){
        List<ExpFlag> expFlags = new ArrayList<>();
        for(ExpFlag expFlag : ExpFlag.values()){
            if((flags & expFlag.getValue()) > 0){
                expFlags.add(expFlag);
            }
        }
        return expFlags;
    }

    List<OpenFlag> unmaskOpenFlags(int flags){
        List<OpenFlag> openFlags = new ArrayList<>();
        for(OpenFlag openFlag : OpenFlag.values()){
            if((flags & openFlag.getValue()) > 0){
                openFlags.add(openFlag);
            }
        }
        return openFlags;
    }

    List<ReqFlag> unmaskReqFlags(int flags){
        List<ReqFlag> reqFlags = new ArrayList<>();
        for(ReqFlag reqFlag : ReqFlag.values()){
            if((flags & reqFlag.getValue()) > 0){
                reqFlags.add(reqFlag);
            }
        }
        return reqFlags;
    }

    RequireInfo getRequireInfo(JavaClass javaClass, ModuleRequires moduleRequires) {
        try {
            int requiresIndex = (int) FieldUtils.readField(moduleRequires, "requiresIndex", true);
            int requiresFlags = (int) FieldUtils.readField(moduleRequires, "requiresFlags", true);
            int requiresVersionIndex = (int) FieldUtils.readField(moduleRequires, "requiresVersionIndex", true);
            return new RequireInfo(
                    requiresIndex,
                    getConstantModuleInfo(javaClass.getConstantPool(), javaClass.getConstantPool().getConstant(requiresIndex)).getName(),
                    requiresVersionIndex,
                    getConstantUtf8Info(javaClass.getConstantPool().getConstant(requiresVersionIndex)).getValue(),
                    unmaskReqFlags(requiresFlags)
            );
        }catch (IllegalAccessException e){
            throw ApiException.error("Invalid BCEL: " + e.getMessage());
        }
    }

    ExportInfo getExportInfo(JavaClass javaClass, ModuleExports moduleExports) {
        try {
            int exportsIndex = (int) FieldUtils.readField(moduleExports, "exportsIndex", true);
            int exportsFlags = (int) FieldUtils.readField(moduleExports, "exportsFlags", true);
            int[] exportsTo = (int[]) FieldUtils.readField(moduleExports, "exportsToIndex", true);
            return new ExportInfo(
                    exportsIndex,
                    getConstantPackageInfo(javaClass.getConstantPool(), javaClass.getConstantPool().getConstant(exportsIndex)).getName(),
                    unmaskExpFlags(exportsFlags),
                    exportsTo
            );
        }catch (IllegalAccessException e){
            throw ApiException.error("Invalid BCEL: " + e.getMessage());
        }
    }

    OpenInfo getOpenInfo(JavaClass javaClass, ModuleOpens moduleOpens) {
        try {
            int opensIndex = (int) FieldUtils.readField(moduleOpens, "opensIndex", true);
            int opensFlags = (int) FieldUtils.readField(moduleOpens, "opensFlags", true);
            int[] opensTo = (int[]) FieldUtils.readField(moduleOpens, "opensTo", true);
            return new OpenInfo(
                    opensIndex,
                    getConstantPackageInfo(javaClass.getConstantPool(), javaClass.getConstantPool().getConstant(opensIndex)).getName(),
                    unmaskOpenFlags(opensFlags),
                    opensTo
            );
        }catch (IllegalAccessException e){
            throw ApiException.error("Invalid BCEL: " + e.getMessage());
        }
    }

    ProvideInfo getProvideInfo(JavaClass javaClass, ModuleProvides moduleProvides) {
        try {
            int providesIndex = (int) FieldUtils.readField(moduleProvides, "providesIndex", true);
            int[] providesWith = (int[]) FieldUtils.readField(moduleProvides, "providesWith", true);
            return new ProvideInfo(
                    providesIndex,
                    getConstantClassInfo(javaClass.getConstantPool(), javaClass.getConstantPool().getConstant(providesIndex)).getName(),
                    providesWith
            );
        }catch (IllegalAccessException e){
            throw ApiException.error("Invalid BCEL: " + e.getMessage());
        }
    }


    public AttributeModule getModuleAttribute(JavaClass javaClass, Attribute attribute){
        Module module = (Module) attribute;
        try {
            int moduleNameIndex = (int) FieldUtils.readField(module, "moduleNameIndex", true);
            int moduleFlags = (int) FieldUtils.readField(module, "moduleFlags", true);
            int moduleVersionIndex = (int) FieldUtils.readField(module, "moduleVersionIndex", true);
            String moduleVersion = moduleVersionIndex > 0 ?
                    getConstantUtf8Info(javaClass.getConstantPool().getConstant(moduleVersionIndex)).getValue()
                    : "";
            int[] usesIndices = (int[]) FieldUtils.readField(module, "usesIndex", true);
            List<RequireInfo> requireInfos = Arrays.stream(module.getRequiresTable())
                    .map(r -> getRequireInfo(javaClass, r))
                    .collect(Collectors.toList());
            List<ExportInfo> exportInfos = Arrays.stream(module.getExportsTable())
                    .map(r -> getExportInfo(javaClass, r))
                    .collect(Collectors.toList());
            List<OpenInfo> openInfos = Arrays.stream(module.getOpensTable())
                    .map(r -> getOpenInfo(javaClass, r))
                    .collect(Collectors.toList());
            List<ProvideInfo> provideInfos = Arrays.stream(module.getProvidesTable())
                    .map(r -> getProvideInfo(javaClass, r))
                    .collect(Collectors.toList());
            return new AttributeModule(
                    module.getNameIndex(),
                    module.getName(),
                    module.getLength(),
                    moduleNameIndex,
                    getConstantModuleInfo(javaClass.getConstantPool(), javaClass.getConstantPool().getConstant(moduleNameIndex)).getName(),
                    unmaskModFlags(moduleFlags),
                    moduleVersionIndex,
                    moduleVersion,
                    requireInfos,
                    exportInfos,
                    openInfos,
                    usesIndices,
                    provideInfos
            );
        } catch (IllegalAccessException e) {
            throw ApiException.error("Invalid BCEL: " + e.getMessage());
        }

    }
    private AttributeNestMembers getNestMembersAttribute(JavaClass javaClass, Attribute attribute){
        NestMembers nestMembers = (NestMembers) attribute;
        try{
            int[] classes = (int[]) FieldUtils.readField(nestMembers, "classes", true);
            return new AttributeNestMembers(
                    nestMembers.getNameIndex(),
                    nestMembers.getName(),
                    nestMembers.getLength(),
                    classes
            );
        } catch (IllegalAccessException e) {
            throw ApiException.error("Invalid BCEL: " + e.getMessage());
        }
    }

    private AttributeNestHost getNestHostAttribute(JavaClass javaClass, Attribute attribute){
        NestHost nestHost = (NestHost) attribute;
        return new AttributeNestHost(
                nestHost.getNameIndex(),
                nestHost.getName(),
                nestHost.getLength(),
                nestHost.getHostClassIndex(),
                getConstantClassInfo(javaClass.getConstantPool(), javaClass.getConstantPool().getConstant(nestHost.getHostClassIndex())).getName()
        );
    }

    private AttributeModuleMainClass getModuleMainClassAttribute(JavaClass javaClass, Attribute attribute){
        ModuleMainClass moduleMainClass = (ModuleMainClass) attribute;
        return new AttributeModuleMainClass(
                moduleMainClass.getNameIndex(),
                moduleMainClass.getName(),
                moduleMainClass.getLength(),
                moduleMainClass.getHostClassIndex(),
                getConstantClassInfo(javaClass.getConstantPool(), javaClass.getConstantPool().getConstant(moduleMainClass.getHostClassIndex())).getName()
        );
    }

    private AttributeModulePackages getModulePackagesAttribute(JavaClass javaClass, Attribute attribute){
        ModulePackages modulePackages = (ModulePackages) attribute;
        return new AttributeModulePackages(
                modulePackages.getNameIndex(),
                modulePackages.getName(),
                modulePackages.getLength(),
                modulePackages.getPackageIndexTable()
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
            // TODO handle RuntimeVisibleTypeAnnotations and RuntimeInvisibleTypeAnnotations
            // SPEC: https://docs.oracle.com/javase/specs/jvms/se11/html/jvms-4.html#jvms-4.7.20

            // TODO handle SourceDebugExtension
            // SPEC: https://docs.oracle.com/javase/specs/jvms/se11/html/jvms-4.html#jvms-4.7.11
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
        if(attribute instanceof RuntimeInvisibleParameterAnnotations){
            return getRuntimeInvisibleParameterAnnotationsAttribute(javaClass, attribute);
        }
        if(attribute instanceof RuntimeVisibleParameterAnnotations){
            return getRuntimeVisibleParameterAnnotationsAttribute(javaClass, attribute);
        }
        if(attribute instanceof Signature){
            return getSignatureAttribute(javaClass, attribute);
        }
        if(attribute instanceof AnnotationDefault){
            return getAnnotationDefaultAttribute(javaClass, attribute);
        }
        if(attribute instanceof StackMap){
            return getStackMapTableAttribute(javaClass, attribute);
        }
        if(attribute instanceof ExceptionTable){
            return getStackExceptionTableAttribute(javaClass, attribute);
        }
        if(attribute instanceof InnerClasses){
            return getInnerClassesAttribute(javaClass, attribute);
        }
        if(attribute instanceof EnclosingMethod){
            return getEnclosingMethodAttribute(javaClass, attribute);
        }
        if(attribute instanceof Synthetic){
            return getSyntheticAttribute(javaClass, attribute);
        }
        if(attribute instanceof LocalVariableTable){
            return getLocalVariableTableAttribute(javaClass, attribute);
        }
        if(attribute instanceof LocalVariableTypeTable){
            return getLocalVariableTypeTableAttribute(javaClass, attribute);
        }
        if(attribute instanceof BootstrapMethods){
            return getBootstrapMethodsAttribute(javaClass, attribute);
        }
        if(attribute instanceof MethodParameters){
            return getMethodParametersAttribute(javaClass, attribute);
        }
        if(attribute instanceof Module){
            return getModuleAttribute(javaClass, attribute);
        }
        if(attribute instanceof NestMembers){
            return getNestMembersAttribute(javaClass, attribute);
        }
        if(attribute instanceof NestHost){
            return getNestHostAttribute(javaClass, attribute);
        }
        if(attribute instanceof ModuleMainClass){
            return getModuleMainClassAttribute(javaClass, attribute);
        }
        if(attribute instanceof ModulePackages){
            return getModulePackagesAttribute(javaClass, attribute);
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
        disassembledClassFile.setAccessFlags(unmaskAccFlag(javaClass.getAccessFlags()));
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

    public List<AccFlag> unmaskAccFlag(int accessFlag){
        List<AccFlag> accFlags = new ArrayList<>();
        for(AccFlag accFlag : AccFlag.values()){
            if((accessFlag & accFlag.getValue()) > 0){
                accFlags.add(accFlag);
            }
        }
        return accFlags;
    }
}
