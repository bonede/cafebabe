import {attribute_info, ClassImage, cp_info, method_info} from "../../api/ApiClient";
import {ClassImageItem, ClassImageItemGroup} from "./ClassImageItem";
import {Icon} from "@blueprintjs/core";
import React, {ReactElement, ReactNode} from "react";


const attributeInfoItem = (attributeInfo: attribute_info, i: number): ClassImageItemGroup => {
    const attributeName = attributeInfo.attributeName;
    let rows = []
    switch (attributeName){
        case "SourceFile": rows.push({key: "Source file", value: attributeInfo.sourceFileName}); break;
        case "Signature": rows.push({key: "Source file", value: attributeInfo.signature}); break;
        case "Exceptions":
            rows.push({key: "Exceptions", value: attributeInfo.numberOfExceptions + ""});
            rows.push({key: "Exception index table", value: attributeInfo.exceptionIndexTable + ""}); break;
        case "LineNumberTable":
            attributeInfo.lineNumberTable.forEach( l => {
                rows.push({key: "Line #" + l.lineNumber, value: "Pc " + l.startPc })
            }); break;
        default:
            rows.push({key: "Value", value: attributeInfo.value});
    }
    return {
        groupName: "Attribute " + attributeInfo.attributeName,
        rows: rows
    }
}
const attributeInfoList = (attributeInfos: attribute_info[]): ClassImageItemGroup[] => {
    return attributeInfos.map(attributeInfoItem)
}

const classInfo = (classImage: ClassImage): ReactElement => {
    let itemGroups: ClassImageItemGroup[] = [
        {
            groupName: "Basic",
            rows: [
                {key: "Name", value: classImage.className},
                {key: "Super class", value: classImage.superClassName},
                {key: "Major Version", value: classImage.majorVersion + ""},
                {key: "Minor Version", value: classImage.minorVersion + ""},
                {key: "Flags", value: classImage.accessFlags.join("/")},
            ]
        }
    ]
    itemGroups = itemGroups.concat(classImage.fields.map((f, i): ClassImageItemGroup => {return {
        groupName: "Field #" + i,
        rows: [
            {key: "FieldName", value: f.name},
            {key: "Descriptor", value: f.descriptor},
            {key: "Flags", value: f.accessFlags.join("/")},
        ]
    }}))

    itemGroups = itemGroups.concat(attributeInfoList(classImage.attributes))
    return <ClassImageItem title={"Class Info"} icon={<Icon icon="build" />} itemGroups={itemGroups} />
}

const cpInfo = (classImage: ClassImage): ReactElement => {
    const getValue = (cpInfo: cp_info) => {
        switch (cpInfo.tag){
            case "CONSTANT_Class": return cpInfo.name
            case "CONSTANT_Fieldref": return cpInfo.className + "." + cpInfo.fieldName
            case "CONSTANT_Methodref": return cpInfo.className + "." + cpInfo.methodName
            case "CONSTANT_InterfaceMethodref" : return cpInfo.className
            case "CONSTANT_String": return cpInfo.string
            case "CONSTANT_Integer": return cpInfo.value
            case "CONSTANT_Float": return cpInfo.value
            case "CONSTANT_Long" : return cpInfo.value
            case "CONSTANT_Double": return cpInfo.value
            case "CONSTANT_NameAndType" : return cpInfo.name + cpInfo.descriptor
            case "CONSTANT_Utf8": return cpInfo.value
            case "CONSTANT_MethodHandle" : return cpInfo.className
            case "CONSTANT_MethodType": return cpInfo.className
            case "CONSTANT_InvokeDynamic": return cpInfo.className
            default: return ""
        }
    }
    let itemGroups: ClassImageItemGroup[] = [
        {
            groupName: "Pool Size: " + classImage.constantPool.length,
            rows: classImage.constantPool.map((c, i) => {
                if(c == null){
                    return {
                        key: i + " " + "null",
                        value: "null"
                    }
                }else{
                    return {
                        key: i + " "  + c.tag.replace("CONSTANT_", ""),
                        value: getValue(c) + ""
                    }
                }

            })
        }
    ]

    return <ClassImageItem title={"Constant Pool"} icon={<Icon icon="build" />} itemGroups={itemGroups} />
}

const  methodInfo = (method: method_info, i: number) => {
    let codeOffset = 0
    let codeAttribute = method.attributes.filter(a => a.attributeName == "Code")[0]
    let nonCodeAttribute = method.attributes.filter(a => a.attributeName != "Code")
    let itemGroups: ClassImageItemGroup[] = [
        {
            groupName: "Basic",
            rows: [
                {key: "Name", value: method.name},
                {key: "Descriptor", value: method.descriptor},
                {key: "Flags", value: method.accessFlags.join("/")},
                {key: "Stack", value: method.maxStack + ""},
                {key: "Locals", value: method.maxLocals + ""},
            ]
        },
        {
            groupName: "Code",
            rows: codeAttribute.instructions.map(i =>
                {

                    const result = {key: codeOffset + " " + i.opMnemonic + (i.index === undefined ? "" : " #" + i.index) + (i.value === undefined ? "" : " $" + i.value), value: i.opCode + ""}
                    codeOffset += i.size
                    return result
                }
            )
        }
    ]
    itemGroups = [...itemGroups, ...attributeInfoList(codeAttribute.attributes), ...attributeInfoList(nonCodeAttribute)]
    return <ClassImageItem title={`Method #` + i} icon={<Icon icon="build" />} itemGroups={itemGroups} />
}
const methodInfoList = (classImage: ClassImage): ReactNode => {
    return <div className="class-image-view-method-list">{classImage.methods.map(methodInfo)}</div>
}
export interface ClassImageViewProps{
    classImage: ClassImage
}
export const ClassImageView = (props: ClassImageViewProps) => {
    if(!props.classImage){
        return null
    }
    return <div className="class-image-view">
        {methodInfoList(props.classImage)}
        {cpInfo(props.classImage)}
        {classInfo(props.classImage)}
    </div>
}