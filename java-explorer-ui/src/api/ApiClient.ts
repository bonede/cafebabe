export interface CompilerInfo{
    name: string
    example: string
}

export interface ApiResp<T>{
    code: string
    msg: string
    data: T
}

export interface AppInfo{
    compilers: CompilerInfo[]
}

type HttpMethod = "get" | "post"

export class ApiClient{
    public getApiUrl(): string{
        if(window.location.href.includes("")){
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

    public getAppInfo(): Promise<ApiResp<AppInfo>>{
        return this.request("/app/info", "get")
    }
}