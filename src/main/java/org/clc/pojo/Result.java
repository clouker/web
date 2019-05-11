package org.clc.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result extends HashMap<String, Object> implements Serializable {

    enum Code {
        OK(-1, "ok."),
        ERROR(1, "error."),
        FAIL(2, "fail.");
        private int code;
        private String msg;

        Code(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }

    private static Result result(Code code, Object data) {
        Result result = new Result();
        result.put("code", code.code);
        result.put("msg", code.msg);
        if (data != null)
            result.put("data", data);
        return result;
    }

    public static Result ok() {
        return ok("");
    }

    public static Result ok(String msg) {
        return ok(msg, null);
    }

    public static Result ok(String msg, Object data) {
        if (!msg.equals(""))
            Code.OK.msg = msg;
        return result(Code.OK, data);
    }

    public static Result $ok(Object data) {
        return result(Code.OK, data);
    }

    public static Result error() {
        return error(null);
    }

    public static Result error(String errorMsg) {
        return error(errorMsg, null);
    }

    public static Result error(String errorMsg, Object errorData) {
        if (errorMsg != null)
            Code.ERROR.msg = errorMsg;
        return result(Code.ERROR, errorData);
    }

    /**
     * 自定义返回对象
     */
    public static Result diy(Map<String, ?> map) {
        Result result = new Result();
        result.put("code", 0);
        result.put("msg", "hello.world");
        result.putAll(map);
        return result;
    }

    public static Result $diy(String key, Object value) {
        Result result = new Result();
        result.put("code", 0);
        result.put("msg", "hello.world");
        result.put(key, value);
        return result;
    }

    @Override
    public Result put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
