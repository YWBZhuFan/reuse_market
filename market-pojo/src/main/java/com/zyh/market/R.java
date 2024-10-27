package com.zyh.market;


import com.zyh.market.constants.AuthCode;
import com.zyh.market.constants.ResultCode;
import lombok.Data;

import java.io.Serializable;

@Data
public class R<T> implements Serializable {
    private Integer code;

    private String msg;

    private T result;

    private R(T result, Integer code, String msg) {
        this.result = result;
        this.code = code;
        this.msg = msg;
    }

    private R(T result, ResultCode resultCode) {
        this.result = result;
        this.msg = resultCode.getName();
        this.code = resultCode.getCode();
    }

    private R(T result, AuthCode authCode) {
        this.result = result;
        this.msg = authCode.getName();
        this.code = authCode.getCode();
    }
    public static R<String> ok() {
        return new R<>("", ResultCode.Success);
    }

    public static <T> R<T> ok(T data) {
        return new R<>(data, ResultCode.Success);
    }

    public static R<String> fail(ResultCode resultCode) {
        return new R<>("", resultCode);
    }

    public static R<String> fail(ResultCode resultCode, String msg) {
        return new R<>("", resultCode.getCode(), msg);
    }

    public static R<String> fail(AuthCode authCode) {
        return new R<>("", authCode);
    }

    public static <T> R<T> fail(T data) {
        return new R<>(data, ResultCode.Fail);
    }
    
    public static R<String> fail(AuthCode authCode,String msg) {
        return new R<>("", authCode.getCode(),msg);
    }
}
