package com.carrot.usercenter.common;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
/**
 * 通用返回对象
 *
 * @Author carrot
 */
@Data
public class BaseResponse<T> {
    private int code;
    private T data;
    private String message;
    public static BaseResponse getErrorCode(ErrorCode errorCode){
        return new BaseResponse(errorCode.getCode(),null,errorCode.getMessage());
    }


}
