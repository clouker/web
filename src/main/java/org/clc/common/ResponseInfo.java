package org.clc.common;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseInfo {
    private int code;
    private String msg;
    private Object data;

    private ResponseInfo() {
    }

    public static ResponseInfo returnMsg(int code, String msg) {
        ResponseInfo info = new ResponseInfo();
        info.code = code;
        info.msg = msg;
        return info;
    }

    public static ResponseInfo returnMsg(int code, String msg, Object data) {
        ResponseInfo info = new ResponseInfo();
        info.code = code;
        info.msg = msg;
        info.data = data;
        return info;
    }


}
