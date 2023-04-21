export interface Compiler {
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

export interface InstructionDoc{
    mnemonic: string
    category: string
    shortdescr: string
    specref: string
}
export interface JdkVersion{
    jdkVersion: string
    shortJdkVersion: string
    bytecodeVersion: string
}
export interface AppInfo{
    instructionDocs: Record<string, InstructionDoc>
    versions: Record<string, JdkVersion>
    compilers: Compiler[]
    specUrl: string
}
export interface SrcFile{
    path: string
    content: string
}
export interface CompileResult{
    classFiles: ClassFile[]
    returnCode: number
    compiler: string
    compilerOptions: string
    stdout: string
    stderr: string
}
export interface ShareResp{
    id: string
    url: string
    deletingToken: string
}
export interface CompilerOps{
    compilerName: string
    debug: boolean
    optimize: boolean
}

export interface PubShareFile{
    id: string
    ops: CompilerOps
    srcFiles: SrcFile[]
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
    typeName: string
}

export interface parameter_annotation{
    num_annotations: number
    annotations: annotation[]
}

export interface element_value_pair{
    element_name_index: number
    elementName: string
    value: element_value
}

export interface enum_const_value{
    type_name_index: number
    const_name_index: number
}

export interface element_value{
    tag: string
    const_value_index: number
    enum_const_value: enum_const_value
    class_info_index: number
    annotation_value: annotation
    num_values: number
    values: element_value[]
}
export interface exception_table_item{
    startPc: number
    endPc: number
    handlerPc: number
    catchType: number
}
export interface Instruction{
    index: number
    value: number
    opMnemonic: string
    opCode: number
    size: number
    /** calculated in client side */
    pc: number
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
    exceptionIndexTable: number[]
    // LineNumberTable
    lineNumberTableLength: number
    lineNumberTable: line_number_table_item[]
    // RuntimeVisibleAnnotations / RuntimeInvisibleAnnotations
    numAnnotations: number
    annotations: annotation[]
    // RuntimeVisibleParameterAnnotations / RuntimeInvisibleParameterAnnotations
    num_parameters: number
    parameter_annotations: parameter_annotation[]
    // Stack frame map
    entries: stack_map_frame[]
    // Inner class
    number_of_classes: number
    classes: inner_class_info[]
    // NestHost
    host_class_index: number
    // NestMembers
    class_indices: number[]
    // LocalVariableTable
    local_variable_table_length: number
    local_variable_table: local_variable_info[]
    // Unhanded, hex string
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
type field_access_flag = "ACC_PUBLIC" |
    "ACC_PRIVATE" |
    "ACC_PROTECTED" |
    "ACC_STATIC" |
    "ACC_FINAL" |
    "ACC_VOLATILE" |
    "ACC_TRANSIENT" |
    "ACC_SYNTHETIC" |
    "ACC_ENUM"

type method_access_flag = "ACC_PUBLIC" |
    "ACC_PRIVATE" |
    "ACC_PROTECTED" |
    "ACC_STATIC" |
    "ACC_FINAL" |
    "ACC_SYNCHRONIZED" |
    "ACC_BRIDGE" |
    "ACC_VARARGS" |
    "ACC_NATIVE" |
    "ACC_ABSTRACT" |
    "ACC_STRICT" |
    "ACC_SYNTHETIC"

export interface field_info{
    name: string
    nameIndex: number
    descriptor: string
    descriptorIndex: number
    accessFlags: field_access_flag[]
    attributesCount: number
    attributes: attribute_info[]
}

export interface method_info{
    nameIndex: number
    name: string
    descriptorIndex: number
    descriptor: string
    accessFlags: method_access_flag[]
    attributesCount: number
    attributes: attribute_info[]
    maxLocals: number
    maxStack: number
}

export interface local_variable_info{
    start_pc: number
    length: number
    name_index: number
    descriptor_index: number
    index: number
}

export interface ClassFile extends Base64File{
    classImage: ClassImage
}

export interface Base64File{
    path: string
    content: string
}

export interface ClassImage{
    majorVersion: number
    minorVersion: number
    superClassNameIndex: number
    superClassName: string
    accessFlags: class_access_flag[]
    attributeCount:  number
    attributes: attribute_info[]
    className: string
    classNameIndex: number
    constantPool: cp_info[]
    constantPoolCount: number
    fields: field_info[]
    fieldsCount: number
    methodsCount: number
    methods: method_info[]
}

export interface stack_map_frame{
    frame_type: number
    offset_delta?: number
    frameTypeName: string
    stack?: verification_type_info[]
    locals?: verification_type_info[]
    number_of_locals?: number
    number_of_stack_items?: number
}

export interface verification_type_info{
    tag: number
    tagName: string
    offset?: number
    cpool_index?: number
}

export interface inner_class_info{
    inner_class_info_index: number
    outer_class_info_index: number
    inner_name_index: number
    inner_class_access_flags: class_access_flag[]
}

export interface AppState{
    debug?: boolean
    optimize?: boolean
    editorContent?: string
    compiler?: Compiler
}

type HttpMethod = "get" | "post" | "put" | "delete"

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

    public async request<T>(path: string, method: HttpMethod, params?: Record<string, any>): Promise<T> {
        let url = this.getApiUrl() + path
        const init: RequestInit = {
            method: method,
        }
        const headers: HeadersInit = {}
        if (method == "post" || method == "put" || method == "delete") {
            init.body = JSON.stringify(params)
            headers['Content-Type'] = 'application/json'
        } else if (method == "get" && params != null) {
            const searchParams = new URLSearchParams(params);
            url = url + "?" + searchParams.toString()
        }
        init.headers = headers
        const resp: ApiResp<T> = await fetch(url, init).then(r => r.json())
        if(resp.code != 'ok'){
            throw new Error(resp.msg)
        }
        return resp.data
    }

    public async getAppInfo(): Promise<AppInfo> {
        return this.request<AppInfo>("/app/info", "get")
    }

    public async compile(ops: CompilerOps, srcFiles: SrcFile[]): Promise<CompileResult> {
        return  await this.request<CompileResult>("/compile", "post", {
            ops,
            srcFiles
        })
    }

    public async createShare(ops: CompilerOps, srcFiles: SrcFile[], hoursToLive?: number): Promise<ShareResp> {
        return  await this.request<ShareResp>("/share", "post", {
            srcFiles,
            hoursToLive,
            ops
        })
    }

    public async getShare(id: string): Promise<PubShareFile>{
        return  await this.request<PubShareFile>("/share/" + id, "get")
    }

    public async deleteShare(url: string, deletingToken: string): Promise<ShareResp> {
        return  await this.request<ShareResp>("/share", "delete", {
            url,
            deletingToken,
        })
    }

    static APP_STATE_KEY = "appState"

    public saveAppState(appState: AppState){
        localStorage.setItem(ApiClient.APP_STATE_KEY, JSON.stringify(appState))
    }

    public getAppState(): AppState | null{
        const json = localStorage.getItem(ApiClient.APP_STATE_KEY)
        if(json){
            return JSON.parse(json)
        }
        return null
    }



}