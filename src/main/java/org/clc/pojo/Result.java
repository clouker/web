package org.clc.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString(exclude = {"code"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result implements Serializable {

    private int code;
    private String msg;
    private Object data;

    enum Code {
        SUCCESS(0, "success."),
        ERROR(1, "error.");
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

    public static Result success(String msg) {
        return success(msg, null);
    }

    public static Result success(String msg, Object data) {
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
            Code.ERROR.msg = errorMsg;
        return new Result(Code.ERROR, errorInfo);
    }

}
