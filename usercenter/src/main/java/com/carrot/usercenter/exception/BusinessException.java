package com.carrot.usercenter.exception;

import com.carrot.usercenter.common.ErrorCode;

/**
 * 业务异常类
 *
 * @Author carrot
 */
public class BusinessException extends RuntimeException{
    private final int code;
    private final String description;

    public BusinessException(String message, int code,String description) {
        super(message);
        this.code = code;
        this.description = description;
    }
    //传入errorCode里面定义好的
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    //传入自定义的description
    public BusinessException(ErrorCode errorCode,String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description =description;
    }
   //生成对应的get方法
    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
