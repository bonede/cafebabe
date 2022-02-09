package org.javaexplorer.model;

import lombok.Data;

@Data
public class ApiResp<T> {
    private String code;
    private T data;
    private String msg;

    public static ApiResp error(String msg){
        ApiResp apiResp = new ApiResp();
        apiResp.setMsg(msg);
        apiResp.setCode("err");
        return apiResp;
    }

    public static ApiResp error(String msg, String... args){
        ApiResp apiResp = new ApiResp();
        apiResp.setMsg(String.format(msg, args));
        apiResp.setCode("err");
        return apiResp;
    }

    public  static <T> ApiResp ok(T data){
        ApiResp apiResp = new ApiResp();
        apiResp.setData(data);
        apiResp.setCode("ok");
        return apiResp;
    }
}
