import {annotation, attribute_info, ClassImage, cp_info, element_value, method_info} from "../../api/ApiClient";
import {ClassImageItem, ClassImageItemGroup, ClassImageItemGroupRow} from "./ClassImageItem";
import {Icon} from "@blueprintjs/core";
import React, {ReactElement, ReactNode} from "react";

const annotationItemStringValue = (element_value: element_value): string | undefined =>{
    switch (element_value.tag){
        case 'B':
        case 'C':
        case 'D':
        case 'F':
        case 'I':
        case 'J':
        case 'S':
        case 'Z':
        case 's':
            return "#" + element_value.const_value_index + "";
        case 'c':
            return "#" + element_value.class_info_index + "";
        case 'e':
            return "#" + element_value.enum_const_value.type_name_index + " " +  "#" + element_value.enum_const_value.const_name_index;
        default:
            return undefined
    }
}

const annotationItemValue = (element_name: string, element_value: element_value): ClassImageItemGroupRow =>{
    const stringValue = annotationItemStringValue(element_value)
    if(stringValue){
        return {
            key: element_name,
            value: stringValue
        }
    }else if(element_value.tag == 'e'){
        return {
            key: element_name,
            value: "Detail",
            more: {
                rows: [
                    {
                        key: "Type name index",
                        value: element_value.enum_const_value.type_name_index + ""
                    },
                    {
                        key: "Const index",
                        value: element_value.enum_const_value.const_name_index + ""
                    }
                ]
            }
        }
    }else if(element_value.tag == '['){
        return {
            key: element_name,
            value: "Detail",
            more: {
                rows: element_value.values.map((e, i) => {
                    return annotationItemValue(element_name + "[" + i + "]", e)
                })
            }
        }
    }else{
        throw new Error("unknown tag " + element_value.tag)
    }
}

const annotationItemRow = (annotation: annotation): ClassImageItemGroupRow => {
    let moreRows: ClassImageItemGroupRow[] = []
    if(annotation.num_element_value_pairs > 0){
        moreRows = annotation.element_value_pairs.map((e) => annotationItemValue(e.elementName, e.value))
    }
    return {
        key: "Type",
        value: annotation.typeName,
        more: annotation.num_element_value_pairs == 0 ? undefined : {
            groupName: "Annotation parameters",
            rows: moreRows
        }
    }
}
const attributeInfoItem = (attributeInfo: attribute_info, i: number): ClassImageItemGroup => {
    const attributeName = attributeInfo.attributeName;
    let rows: ClassImageItemGroupRow[] = [{key: "Name", value: attributeName }]
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
        case "RuntimeVisibleAnnotations":
            attributeInfo.annotations.map( a => rows.push(annotationItemRow(a))); break;
        case "RuntimeInvisibleAnnotations":
            attributeInfo.annotations.map( a => rows.push(annotationItemRow(a))); break;
        case "Deprecated": break;
        default:
            rows.push({key: "Value", value: attributeInfo.value});
    }
    return {
        groupName: "Attribute",
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
        }
    ]
    if(codeAttribute){
        itemGroups.push({
            groupName: "Code",
            rows: codeAttribute.instructions.map(i =>
                {

                    const result = {key: codeOffset + " " + i.opMnemonic + (i.index === undefined ? "" : " #" + i.index) + (i.value === undefined ? "" : " $" + i.value), value: i.opCode + ""}
                    codeOffset += i.size
                    return result
                }
            )
        })
    }
    const codeAttributes = codeAttribute ? attributeInfoList(codeAttribute.attributes) : []
    itemGroups = [...itemGroups, ...codeAttributes, ...attributeInfoList(nonCodeAttribute)]
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