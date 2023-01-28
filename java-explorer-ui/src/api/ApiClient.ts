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
export interface attribute_info{
    attributeNameIndex: number
    attributeName: string
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
    // unhanded, hex string
    value: string
}
export interface ClassImage{
    accessFlags: class_access_flag[]
    attributeCount: number
    attributes: attribute_info[]
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