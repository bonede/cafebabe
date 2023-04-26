import {
    annotation,
    AppInfo,
    attribute_info,
    ClassImage,
    cp_info,
    element_value,
    Instruction,
    JdkVersion,
    method_info,
    parameter_annotation,
    stack_map_frame
} from "../../api/ApiClient";
import {ClassImageItemGroup, ClassImageItemGroupRow, ClassImageSection} from "./ClassImageSection";
import React, {ReactElement, ReactNode, useContext, useState} from "react";
import {AppInfoContext} from "../app/App";


export interface ClassImageViewProps{
    file: string
    classImage: ClassImage
    selectedLines?: number[]
    onSelectLine?: (file: string, line?: number) => void
}

const COLOR_STRING = "#c2947a";
const COLOR_REF = "#6c9ad3";
export const ClassImageView = (props: ClassImageViewProps) => {
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
            default:
                return undefined
        }
    }

    const annotationItemValue = (element_name: string, element_value: element_value): ClassImageItemGroupRow =>{
        const stringValue = annotationItemStringValue(element_value)
        if(stringValue){
            return {
                key: element_name,
                value: stringValue,
                cpIndices: [element_value.const_value_index || element_value.class_info_index]
            }
        }else if(element_value.tag == 'e'){
            return {
                key: element_name,
                value: "Detail",
                more: {
                    rows: [
                        {
                            key: "Type name index",
                            value: element_value.enum_const_value.type_name_index + "",
                            cpIndices: [element_value.enum_const_value.const_name_index]
                        },
                        {
                            key: "Const index",
                            value: element_value.enum_const_value.const_name_index + "",
                            cpIndices: [element_value.enum_const_value.const_name_index]
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
            color: COLOR_REF,
            cpIndices: [annotation.type_index],
            more: annotation.num_element_value_pairs == 0 ? undefined : {
                groupName: "Annotation parameters",
                rows: moreRows
            }
        }
    }

    const parameterAnnotationItemRow = (annotation: parameter_annotation, i: number): ClassImageItemGroupRow => {

        return {
            key: "Parameter #" + i,
            value: "",
            more: {
                groupName: "Annotations",
                rows: annotation.annotations.map(a => annotationItemRow(a))
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
                        const indexValue = s.cpool_index != null ? "#"  + s.cpool_index : ""
                        return {
                            key: s.tagName,
                            value: offsetValue || indexValue,
                            cpIndices: s.cpool_index != null ? [s.cpool_index] : []
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
        let rows: ClassImageItemGroupRow[] = [{key: "Name", value: attributeName, cpIndices: [attributeInfo.attributeNameIndex], color: COLOR_STRING}]
        switch (attributeName){
            case "SourceFile": rows.push({key: "Source file", value: attributeInfo.sourceFileName, cpIndices: [attributeInfo.sourceFileNameIndex], color: COLOR_STRING}); break;
            case "Signature": rows.push({key: "Source file", value: attributeInfo.signature}); break;
            case "Exceptions":
                attributeInfo.exceptionIndexTable.forEach((index, i) => {
                    rows.push({key: "Exception " + i, value: "#" + index, cpIndices: [index]})
                });break;
            case "LineNumberTable":
                attributeInfo.lineNumberTable.forEach( l => {
                    rows.push({
                        key: "Line #" + l.lineNumber,
                        value: "#" + l.startPc,
                        color: COLOR_REF,
                        onMouseLeave: () => {
                            props.onSelectLine?.(props.file, undefined);
                        },
                        onMouseOver: () => {
                            props.onSelectLine?.(props.file, l.lineNumber);
                        }
                    })
                }); break;
            case "RuntimeVisibleAnnotations":
                attributeInfo.annotations.forEach( a => rows.push(annotationItemRow(a))); break;
            case "RuntimeInvisibleAnnotations":
                attributeInfo.annotations.forEach( a => rows.push(annotationItemRow(a))); break;
            case "RuntimeVisibleParameterAnnotations":
                attributeInfo.parameter_annotations.forEach( (a, i) => rows.push(parameterAnnotationItemRow(a, i))); break;
            case "RuntimeInvisibleParameterAnnotations":
                attributeInfo.parameter_annotations.forEach( (a, i) => rows.push(parameterAnnotationItemRow(a, i))); break;
            case "StackMapTable":
                attributeInfo.entries && attributeInfo.entries.forEach( s => rows.push(stackFrameItemRow(s))); break;
            case "InnerClasses":
                attributeInfo.classes.forEach( (c, i) => {
                    rows.push({
                        key: "Class " + i,

                        value: "#" + c.inner_class_info_index,
                        cpIndices: [c.inner_class_info_index],
                        color: COLOR_REF,
                        more: {
                            groupName: "Detail",
                            rows: [
                                {key: "Inner class", value: "#" + c.inner_class_info_index, color: COLOR_REF, cpIndices: [ c.inner_class_info_index]},
                                {key: "Inner name", value: "#" + c.inner_name_index, color: COLOR_STRING, cpIndices: [c.inner_name_index]},
                                {key: "Inner flags", value: c.inner_class_access_flags.join("/")},
                                {key: "Outer class", value: "#" + c.outer_class_info_index, color: COLOR_REF, cpIndices: [c.outer_class_info_index]}
                            ]
                        }
                    })
                }); break;
            case "NestHost": rows.push({key: "Host class", value: "#" + attributeInfo.host_class_index + "", color: COLOR_REF, cpIndices: [ attributeInfo.host_class_index]}); break;
            case "NestMembers":
                attributeInfo.class_indices.forEach( (c, i) => {
                    rows.push({
                        key: "Class " + i,
                        value: "#" + c,
                        cpIndices: [c],
                        color: COLOR_REF,
                    })
                }); break;
            case "LocalVariableTable":
                attributeInfo.local_variable_table.forEach( (l, i) => {
                    rows.push({
                        key: "Local variable " + i,
                        value: "",
                        color: COLOR_REF,
                        more: {
                            groupName: "Local variable",
                            rows: [
                                {
                                    key: "Pc",
                                    value: "" + l.start_pc,
                                },
                                {
                                    key: "Length",
                                    value: l.length + ""
                                },
                                {
                                    key: "Name index",
                                    value: "#" + l.name_index,
                                    cpIndices: [l.name_index],
                                    color: COLOR_REF
                                },
                                {
                                    key: "Descriptor index",
                                    value: "#" + l.descriptor_index,
                                    cpIndices: [l.descriptor_index],
                                    color: COLOR_REF
                                },
                                {
                                    key: "Index",
                                    value: l.index + "",
                                }
                            ]
                        }
                    })
                }); break;
            case "BootstrapMethods":
                attributeInfo.bootstrap_methods.forEach( (l, i) => {
                    rows.push({
                        key: "Bootstrap method " + i,
                        value: "",
                        more: {
                            groupName: "Bootstrap method",
                            rows: [
                                {
                                    key: "Method ref",
                                    value: "#"+ l.bootstrap_method_ref,
                                    color: COLOR_REF,
                                    cpIndices: [l.bootstrap_method_ref]
                                },
                                ...l.bootstrap_arguments ? l.bootstrap_arguments.map((a, k) => {
                                    return {
                                        key: "Argument " + k,
                                        value: "#" + a,
                                        color: COLOR_REF,
                                        cpIndices: [a]
                                    }
                                }) : []
                            ]
                        }
                    })
                }); break;
            case "MethodParameters":
                rows.push({
                    key: "Method parameters",
                    value: "",
                    more: {
                        groupName: "Method parameters",
                        rows: attributeInfo.parameters.map( p => {
                            return {
                                key: p.name,
                                value: p.access_flags.join("/"),
                                cpIndices: [p.name_index]
                            }

                        })
                    }
                }); break;
            case "Deprecated": break;
            default:
                rows.push({key: "Value", value: "0x" + attributeInfo.value});
        }
        return {
            groupName: "Attribute",
            rows: rows
        }
    }
    const attributeInfoList = (attributeInfos: attribute_info[]): ClassImageItemGroup[] => {
        return attributeInfos.map(attributeInfoItem)
    }

    const classInfo = (classImage: ClassImage, jdkVersion?: JdkVersion): ReactElement => {
        let itemGroups: ClassImageItemGroup[] = [
            {
                groupName: "Basic",
                rows: [
                    {key: "Name", value: classImage.className, cpIndices: [classImage.classNameIndex], color: COLOR_STRING},
                    {key: "Super class", value: classImage.superClassName, cpIndices: [classImage.superClassNameIndex], color: COLOR_REF},
                    {key: "Major Version", value: classImage.majorVersion + ""},
                    {key: "Minor Version", value: classImage.minorVersion + ""},
                    {key: "Jdk Version", value: jdkVersion?.jdkVersion || ""},
                    {key: "Flags", value: classImage.accessFlags.join("/")},
                ]
            }
        ]
        itemGroups = itemGroups.concat(classImage.fields.map((f, i): ClassImageItemGroup => {return {
            groupName: "Field #" + i,
            rows: [
                {key: "FieldName", value: f.name, cpIndices: [f.nameIndex], color: COLOR_STRING},
                {key: "Descriptor", value: f.descriptor, cpIndices: [f.descriptorIndex], color: COLOR_STRING},
                {key: "Flags", value: f.accessFlags.join("/")},
            ]
        }}))

        itemGroups = itemGroups.concat(attributeInfoList(classImage.attributes))
        return <ClassImageSection onSelectCpInfo={is => setCpIndices(is)} title={"Class Info"} itemGroups={itemGroups} />
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
                case "CONSTANT_MethodHandle" : return cpInfo.kind + " #" + cpInfo.referenceIndex
                case "CONSTANT_MethodType": return cpInfo.className
                case "CONSTANT_InvokeDynamic": return "#" + cpInfo.nameAndTypeIndex + " " + cpInfo.bootstrapMethodAttrIndex
                default: return ""
            }
        }
        const color = (c: cp_info): string | undefined => {
            if(c == null){
                return undefined
            }
            switch (c.tag){
                case "CONSTANT_String":
                case "CONSTANT_Utf8":
                    return COLOR_STRING
                case "CONSTANT_Class":
                case "CONSTANT_Methodref":
                case "CONSTANT_Fieldref":
                case "CONSTANT_NameAndType":
                    return COLOR_REF

            }
            return undefined
        }
        let itemGroups: ClassImageItemGroup[] = [
            {
                groupName: "Pool Size: " + classImage.constantPool.length,
                rows: classImage.constantPool.map((c, i) => {
                    return {
                        key: c == null ? i + " " + "null" : i + " "  + c.tag.replace("CONSTANT_", ""),
                        value: c == null ? "null" : getValue(c) + "",
                        color: color(c),
                        flash: cpIndices?.includes(i),
                        cpIndices: c == null ? [] : [c.nameIndex, c.classIndex, c.descriptorIndex, c.stringIndex, c.descriptorIndex, c.nameAndTypeIndex, c.referenceIndex]
                    }
                })
            }
        ]

        return <ClassImageSection onSelectCpInfo={is => setCpIndices(is)} title={"Constant Pool"} itemGroups={itemGroups} />
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
    const selectedLine = (pc: number, lineNumberTable: attribute_info): number | undefined =>{
        if(!lineNumberTable){
            return
        }
        const lineNumbers = lineNumberTable.lineNumberTable

        for(let i = 0; i < lineNumbers.length; i++){
            const line = lineNumbers[i]
            const endLine = lineNumbers[i + 1]
            const startPc = line.startPc
            const endPc = endLine ? endLine.startPc : Infinity
            if(pc >= startPc && pc < endPc){
                return line.lineNumber
            }
        }
        return undefined
    }
    const  methodInfo = (method: method_info, i: number, selectedLines?: number[], appInfo?: AppInfo, jdkVersion?: JdkVersion) => {
        let codeAttribute = method.attributes.filter(a => a.attributeName == "Code")[0]

        let nonCodeAttribute = method.attributes.filter(a => a.attributeName != "Code")
        let itemGroups: ClassImageItemGroup[] = [
            {
                groupName: "Basic",
                rows: [
                    {key: "Name", value: method.name, cpIndices: [method.nameIndex], color: COLOR_STRING},
                    {key: "Descriptor", value: method.descriptor, cpIndices: [method.descriptorIndex], color: COLOR_STRING},
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
                        const doc = appInfo?.instructionDocs[instruction.opMnemonic]
                        if(instruction.opMnemonic == "invokevirtual"){
                            console.log(instruction)
                        }
                        return {
                            key: instruction.pc + " " + instruction.opMnemonic,
                            help: {
                                groupName: instruction.opMnemonic,
                                rows: [
                                    {
                                        key: "Mnemonic",
                                        value: instruction.opMnemonic,
                                        link: doc?.specref && appInfo?.specUrl.replace("{version}", jdkVersion?.shortJdkVersion || "") + doc.specref
                                    },
                                    {
                                        key: "Opcode",
                                        value: instruction.opCode + "",
                                    },
                                    {
                                        key: "Description",
                                        value: doc?.shortdescr || ""
                                    }
                                ]
                            },
                            value: (instruction.index === undefined ? "" : " #" + instruction.index) + (instruction.value === undefined ? "" : " $" + instruction.value),
                            flash: selectedPcs.includes(instruction.pc),
                            onMouseOver: () => {
                                props.onSelectLine && props.onSelectLine!(props.file, selectedLine(instruction.pc, lineNumberTable))
                            },
                            onMouseLeave: () => {
                                props.onSelectLine && props.onSelectLine!(props.file, undefined)
                            },
                            color: instruction.index !== undefined ? COLOR_REF : COLOR_STRING,
                            cpIndices: [instruction.index]
                        }
                    }
                )
            })
        }
        const codeAttributes = codeAttribute ? attributeInfoList(codeAttribute.attributes) : []
        itemGroups = [...itemGroups, ...codeAttributes, ...attributeInfoList(nonCodeAttribute)]
        return <ClassImageSection key={i} onSelectCpInfo={is => setCpIndices(is)} title={`Method #` + i} itemGroups={itemGroups} />
    }
    const methodInfoList = (classImage: ClassImage, selectedLines?: number[], appInfo?: AppInfo, jdkVersion?: JdkVersion): ReactNode => {
        return <div className="class-image-view-method-list">{classImage.methods.map((m,i ) => methodInfo(m, i, selectedLines, appInfo, jdkVersion))}</div>
    }
    if(!props.classImage){
        return null
    }
    const [cpIndices, setCpIndices] = useState(undefined as number[] | undefined)
    const appInfo = useContext(AppInfoContext)
    const jdkVersion = appInfo?.versions[props.classImage.majorVersion + "." + props.classImage.minorVersion]
    return <div className="class-image-view">
        {methodInfoList(props.classImage, props.selectedLines, appInfo, jdkVersion)}
        {cpInfo(props.classImage)}
        {classInfo(props.classImage, jdkVersion)}
    </div>
}