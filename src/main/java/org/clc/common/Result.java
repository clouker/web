package org.clc.common;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) //过滤null属性
public class Result {

    private int code;
    private String msg;
    private Object data;

    enum Code {
        SUCCESS(-1, "请求无异常！！！"), ERROR(1, "程序报错！！！")
        , FAIL(2, "请求异常！！！");
        private int code;
        private String msg;

        Code(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }

    private Result(Code code) {
        this(code, null);
    }

    private Result(Code code, Object data) {
        this.code = code.code;
        this.msg = code.msg;
        this.data = data;
    }

    public static Result success() {
        return new Result(Code.SUCCESS);
    }

    public static Result success(Object data) {
        return new Result(Code.SUCCESS, data);
    }

    public static Result error() {
        return new Result(Code.ERROR);
    }

    public static Result fail(Object data) {
        return new Result(Code.FAIL);
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
