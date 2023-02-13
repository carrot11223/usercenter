package com.carrot.usercenter.common;
import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回对象
 *
 * @Author carrot
 */
@Data
public class BaseResponse<T> implements Serializable {
    //返回的自定义状态码
    private int code;
    //返回的具体的对象，这里用到了泛型，由于返回的可能是Long id，也有可能是User user 等等
    private T data;
    //返回的消息 例如‘ok’ ‘error’等等
    private String message;
    //描述
    private String description;

    /**
     * 有参构造方法
     * @param code 状态码
     * @param data 返回值
     * @param message 消息
     */
    public BaseResponse(int code, T data, String message,String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data,String description) {
        this(code,data,"","");
    }
    public BaseResponse(ErrorCode errorCode){
        this.code = errorCode.getCode();
        this.data = null;
        this.message= errorCode.getMessage();
        this.description = errorCode.getDescription();
    }
}
