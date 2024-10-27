package com.zyh.market.constants;

public enum AuthCode {
    USER_LOGIN(1001001, "登录错误"),
    USER_LOGIN_NOT_EXIST(1001002, "用户不存在"),
    USER_LOGIN_EXIST(1001003, "用户已存在"),
    USER_LOGIN_PASSWORD_ERROR(1001004, "密码错误"),
    USER_LOGIN_WECHAT_EXIST(1001005, "微信用户已存在"),
    USER_PERMISSION(1001006, "访问权限异常"),
    USER_PERMISSION_UNAUTHENTICATED(1001007, "请登录"),
    USER_PERMISSION_EXPIRED(1001008, "请重新登录"),
    USER_PERMISSION_UNAUTHORIZED(1001009, "权限不足"),
    ;

    private final Integer code;
    private final String name;

    AuthCode(Integer code, String name) {
        this.code = code;
        this.name = name;
    }


    public Integer getCode() {
        return this.code;
    }


    public String getName() {
        return this.name;
    }
}
