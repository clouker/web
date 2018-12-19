package org.clc.common;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 统一返回JSON
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result {

    private int code;
    private String msg;
    private Object data;

    enum Code {
        SUCCESS(0, "success."),
        ERROR(1, "error");
        private int code;
        private String msg;

        Code(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }

    private Result(Code code, Object data) {
        this.code = code.code;
        this.msg = code.msg;
        this.data = data;
    }

    public static Result success() {
        return success(null, null);
    }

    public static Result success(Object data) {
        return success(data, null);
    }

    public static Result success(Object data, String msg) {
        if (msg != null)
            Code.SUCCESS.msg = msg;
        return new Result(Code.SUCCESS, data);
    }

    public static Result error() {
        return error(null, null);
    }

    public static Result error(String errorMsg) {
        return error(errorMsg, null);
    }

    public static Result error(String errorMsg, Object errorInfo) {
        if (errorMsg != null)
            Code.ERROR.msg = "";
        return new Result(Code.ERROR, errorInfo);
    }

    //-------------------------get/set-------------------------//
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }
    //-------------------------get/set-------------------------//
}
