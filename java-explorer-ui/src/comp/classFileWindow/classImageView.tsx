import {ClassImage, cp_info, method_info} from "../../api/ApiClient";
import {ClassImageItem, ClassImageItemGroup} from "./ClassImageItem";
import {Icon} from "@blueprintjs/core";
import React, {ReactElement, ReactNode} from "react";

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

    itemGroups = itemGroups.concat(classImage.attributes.map((a, i): ClassImageItemGroup => {
        const ao: any = a
        return {
        groupName: "Attribute #" + i,
        rows: Object.keys(a).filter(k => !k.endsWith("Index")).map(k => {
            return {
                key: k,
                value: ao[k]
            }
        })
    }}))
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
                        value: getValue(c)
                    }
                }

            })
        }
    ]

    return <ClassImageItem title={"Constant Pool"} icon={<Icon icon="build" />} itemGroups={itemGroups} />
}

const  methodInfo = (method: method_info, i: number) => {
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
            rows: method.attributes.filter(a => a.attributeName == "Code")[0].instructions.map(i =>
                {
                    return {key: i.opMnemonic, value: i.opCode + ""}
                }
            )
        }
    ]
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