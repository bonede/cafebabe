package org.javaexplorer.error;

import org.javaexplorer.model.ApiResp;

public class ApiException extends RuntimeException{
    private ApiResp apiResp;

    public static ApiException error(String msg){
        return new ApiException(ApiResp.error(msg));
    }

    public static ApiException error(String msg, String... args){
        return new ApiException(ApiResp.error(msg, args));
    }

    public ApiException(ApiResp apiResp) {
        super(apiResp.getMsg());
        this.apiResp = apiResp;
    }

    public ApiResp getApiResp() {
        return apiResp;
    }
}
