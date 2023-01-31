export interface CompilerInfo{
    name: string
    example: string
    lang: string
    fileName: string
}

export interface ApiResp<T>{
    code: string
    msg: string
    data: T
}

export interface AppInfo{
    compilers: CompilerInfo[]
}

export interface CompileResult{
    classImages: ClassImage[]
    returnCode: number
    stdout: string
    stderr: string
}
type class_access_flag = "ACC_PUBLIC" |
    "ACC_FINAL" |
    "ACC_SUPER" |
    "ACC_INTERFACE" |
    "ACC_ABSTRACT" |
    "ACC_SYNTHETIC" |
    "ACC_ANNOTATION" |
    "ACC_ENUM";

export interface line_number_table_item{
    startPc: number;
    lineNumber: number;
}

export interface annotation{
    type_index: number
    num_element_value_pairs: number
    element_value_pairs: element_value_pair[]
}

export interface element_value_pair{
    element_name_index: number
    value: element_value
}

export interface enum_const_value{
    type_name_index: number
    const_name_index: number
}
export interface array_value{
    num_values: number
    values: element_value[]
}

export interface element_value{
    tag: string
    const_value_index: number
    enum_const_value: enum_const_value
    class_info_index: number
    annotation_value: annotation
    array_value: array_value
}
export interface exception_table_item{
    startPc: number
    endPc: number
    handlerPc: number
    catchType: number
}
export interface Instruction{
    opMnemonic: string
    opCode: number
    size: number
}
export interface attribute_info{
    attributeNameIndex: number
    attributeName: string
    // Code
    instructions: Instruction[]
    maxStack: number
    maxLocals: number
    codeLength: number
    attributesCount: number
    attributes: attribute_info[]
    exceptionTableLength: number
    exception_table: exception_table_item[]
    // SourceFile
    sourceFileName: string
    sourceFileNameIndex: number

    // Signature
    signatureIndex: number
    signature: string
    // ConstantValue
    constantValueIndex: number
    // Exceptions
    numberOfExceptions: number
    exceptionIndexTable: number
    // LineNumberTable
    lineNumberTableLength: number
    LineNumberTable: line_number_table_item[]
    // RuntimeVisibleAnnotations
    numAnnotations: number
    annotations: annotation[]

    // unhanded, hex string
    value: string
}
type cp_info_tag = "CONSTANT_Class" |
    "CONSTANT_Fieldref" |
    "CONSTANT_Methodref" |
    "CONSTANT_InterfaceMethodref" |
    "CONSTANT_String" |
    "CONSTANT_Integer" |
    "CONSTANT_Float" |
    "CONSTANT_Long" |
    "CONSTANT_Double" |
    "CONSTANT_NameAndType" |
    "CONSTANT_Utf8" |
    "CONSTANT_MethodHandle" |
    "CONSTANT_MethodType" |
    "CONSTANT_InvokeDynamic"

type method_handle_kind = "REF_getField" |
    "REF_getStatic" |
    "REF_putField" |
    "REF_putStatic" |
    "REF_invokeVirtual" |
    "REF_invokeStatic" |
    "REF_invokeSpecial" |
    "REF_newInvokeSpecial"
    "REF_invokeInterface"

export interface cp_info{
    tag: cp_info_tag
    // Class
    name: string
    nameIndex: number
    // double, float, integer, long, utf8
    value: number | string
    // method/field ref cp
    className: string
    classIndex: number
    nameAndTypeIndex: number
    fieldName: string
    fieldDescriptor: string
    methodName: string
    methodDescriptor: string
    // invoke dynamic
    bootstrapMethodAttrIndex: number
    // MethodHandle
    kind: method_handle_kind
    referenceIndex: number
    // MethodType
    descriptorIndex: number
    descriptor: number
    // NameAndType(name, descriptor)
    // String
    stringIndex: number
    string: string
    // utf8
    length: number
}
export interface ClassImage{
    accessFlags: class_access_flag[]
    attributeCount: number
    attributes: attribute_info[]
    className: string
    classNameIndex: string
    constantPool: cp_info[]
}

type HttpMethod = "get" | "post"

export class ApiClient{
    private static API_CLIENT = new ApiClient();

    public static getClient(): ApiClient{
        return ApiClient.API_CLIENT;
    }

    public getApiUrl(): string{
        const href = window.location.href;
        if(href.includes("localhost") || href.includes("127.0.0.1")){
            return "http://localhost:8080/api"
        }else {
            // TODO return production url
            return ""
        }
    }

    public request<T>(path: string, method: HttpMethod, params?: Record<string, any>): Promise<ApiResp<T>>{
        let url = this.getApiUrl() + path
        const init: RequestInit = {
            method: method,
        }
        const headers: HeadersInit = {}
        if(method == "post"){
            init.body = JSON.stringify(params)
            headers['Content-Type'] = 'application/json'
        }else if(method == "get" && params != null){
            const searchParams = new URLSearchParams(params);
            url = url + "?" + searchParams.toString()
        }
        init.headers = headers
        return fetch(url, init).then(r => r.json())
    }

    public async getAppInfo(): Promise<AppInfo> {
        let resp = await this.request<AppInfo>("/app/info", "get")
        if("ok" == resp.code){
            return resp.data;
        }
        throw new Error(resp.msg);
    }


}