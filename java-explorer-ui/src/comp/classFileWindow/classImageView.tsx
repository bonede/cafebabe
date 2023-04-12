import {
    annotation,
    attribute_info,
    ClassImage,
    cp_info,
    element_value,
    Instruction,
    method_info,
    stack_map_frame
} from "../../api/ApiClient";
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
        key: annotation.typeName,
        value: "",
        more: annotation.num_element_value_pairs == 0 ? undefined : {
            groupName: "Annotation parameters",
            rows: moreRows
        }
    }
}

const stackFrameItemRow = (stack_map_frame: stack_map_frame): ClassImageItemGroupRow => {
    let moreRows: ClassImageItemGroupRow[] = [
        {
            key: "Type",
            value: stack_map_frame.frame_type + ""
        }
    ]
    if(stack_map_frame.offset_delta !== null){
        moreRows.push({
            key: "Offset delta",
            value: stack_map_frame.offset_delta + ""
        })
    }

    if(stack_map_frame.stack && stack_map_frame.stack.length > 0){
        moreRows.push({
            key: "Stack",
            value: "",
            more: {
                groupName: "Detail",
                rows: stack_map_frame.stack.map(s => {
                    const offsetValue = s.offset != null ? "Offset "  + s.offset : ""
                    const indexValue = s.cpool_index != null ? "ConstPool #"  + s.cpool_index : ""
                    return {
                        key: s.tagName,
                        value: offsetValue || indexValue
                    }
                })
            }
        })
    }
    return {
        key: "Frame type",
        value: stack_map_frame.frameTypeName,
        more: {
            groupName: "Frame detail",
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
            attributeInfo.exceptionIndexTable.forEach((index, i) => {
                rows.push({key: "Exception " + i, value: "#" + index})
            });break;
        case "LineNumberTable":
            attributeInfo.lineNumberTable.forEach( l => {
                rows.push({key: "Line #" + l.lineNumber, value: "#" + l.startPc })
            }); break;
        case "RuntimeVisibleAnnotations":
            attributeInfo.annotations.forEach( a => rows.push(annotationItemRow(a))); break;
        case "RuntimeInvisibleAnnotations":
            attributeInfo.annotations.forEach( a => rows.push(annotationItemRow(a))); break;
        case "StackMapTable":
            attributeInfo.entries && attributeInfo.entries.forEach( s => rows.push(stackFrameItemRow(s))); break;
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
const selectedInstructionPcs = (instructions: Instruction[], lineNumberTable: attribute_info, selectLines?: number[]): number[] => {
    if(!lineNumberTable || !selectLines || selectLines.length == 0){
        return []
    }
    const lineNumbers = lineNumberTable.lineNumberTable
    const pcs: number[] = []
    const startEndPcs: number[][] = []
    for(let i = 0; i < lineNumbers.length; i++){
        const line = lineNumbers[i]
        if(!selectLines.includes(line.lineNumber)){
            continue
        }
        const endLine = lineNumbers[i + 1]
        startEndPcs.push( [line.startPc, endLine ? endLine.startPc : Infinity])
    }

    console.log(startEndPcs)
    for(let i = 0; i < instructions.length; i++){
        const instruction = instructions[i]
        startEndPcs.forEach(startEndPc => {
            if(instruction.pc >= startEndPc[0] && instruction.pc < startEndPc[1]){
                pcs.push(instruction.pc)
            }
        })
    }
    return pcs

}
const  methodInfo = (method: method_info, i: number, selectedLines?: number[]) => {

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
        const instructions = codeAttribute.instructions
        let pc = 0
        // calculate pc
        instructions.forEach(i => {
            i.pc = pc
            pc += i.size
        })
        let lineNumberTable = codeAttribute.attributes.filter(a => a.attributeName == "LineNumberTable")[0]
        const selectedPcs = selectedInstructionPcs(instructions, lineNumberTable, selectedLines)
        itemGroups.push({
            groupName: "Code",
            rows: instructions.map((instruction) =>
                {

                    return {
                        key: instruction.pc + " " + instruction.opMnemonic + (instruction.index === undefined ? "" : " #" + instruction.index) + (instruction.value === undefined ? "" : " $" + instruction.value),
                        value: instruction.opCode + "",
                        flash: selectedPcs.includes(instruction.pc)
                    }
                }
            )
        })
    }
    const codeAttributes = codeAttribute ? attributeInfoList(codeAttribute.attributes) : []
    itemGroups = [...itemGroups, ...codeAttributes, ...attributeInfoList(nonCodeAttribute)]
    return <ClassImageItem title={`Method #` + i} icon={<Icon icon="build" />} itemGroups={itemGroups} />
}
const methodInfoList = (classImage: ClassImage, selectedLines?: number[]): ReactNode => {
    return <div className="class-image-view-method-list">{classImage.methods.map((m,i ) => methodInfo(m, i, selectedLines))}</div>
}
export interface ClassImageViewProps{
    classImage: ClassImage
    selectedLines?: number[]
}
export const ClassImageView = (props: ClassImageViewProps) => {
    if(!props.classImage){
        return null
    }
    return <div className="class-image-view">
        {methodInfoList(props.classImage, props.selectedLines)}
        {cpInfo(props.classImage)}
        {classInfo(props.classImage)}
    </div>
}