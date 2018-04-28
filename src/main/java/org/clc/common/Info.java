package org.clc.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Info {
    private int code;
    private String msg;
    private Object data;

    private Info() {
    }

    public static Info returnMsg(int code, String msg) {
        Info info = new Info();
        info.code = code;
        info.msg = msg;
        return info;
    }

    public static Info returnMsg(int code, String msg, Object data) {
        Info info = new Info();
        info.code = code;
        info.msg = msg;
        info.data = data;
        return info;
    }


}
