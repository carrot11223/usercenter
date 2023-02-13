package com.carrot.usercenter.utils;

import com.carrot.usercenter.common.BaseResponse;
import com.carrot.usercenter.common.ErrorCode;

/**
 * 封装一个工具类
 *
 * @author carrot
 */
public class ResultUtils {

    /**
     * 返回成功的通用对象
     *
     */
    public static <T>BaseResponse success(T data){
        //状态码 ： 0默认表示成功
        return new BaseResponse(0,data,"ok","");
    }


    /**
     * 返回错误的通用对象
     *
     */
    public static <T>BaseResponse error(ErrorCode errorCode){
        //状态码 ： 0默认表示成功
        return new BaseResponse(errorCode);
    }

    /**
     * 返回错误的通用对象
     *
     */
    public static <T>BaseResponse error(ErrorCode errorCode,String message,String description){
        //状态码 ： 0默认表示成功
        return new BaseResponse(errorCode.getCode(),message,description);
    }
    /**
     * 返回错误的通用对象
     *
     */
    public static <T>BaseResponse error(ErrorCode errorCode,String description){
        //状态码 ： 0默认表示成功
        return new BaseResponse(errorCode.getCode(),errorCode.getMessage(),description);
    }
    /**
     * 返回错误的通用对象
     *
     */
    public static <T>BaseResponse error(int errorCode,String message,String description){
        //状态码 ： 0默认表示成功
        return new BaseResponse(errorCode,message,description);
    }
}
