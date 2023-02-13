package com.carrot.usercenter.common;
public enum ErrorCode {
    SUCCESS(0,"成功",""),
    PARAMS_ERROR(40000,"请求参数错误",""),
    NULL_ERROR(40001,"请求数据为空",""),
    NOT_LOGIN(40100,"未登录",""),
    NO_AUTH(40101,"无权限","");
    ;

    /**
     * 成员变量
     *
     */
    //错误码
    private final int code;
    //消息
    private final String message;
    //描述
    private final String description;
    //构造方法
    private ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
