package com.carrot.usercenter.pojo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 注册用户请求类，封装请求过来的json对象的值
 *
 * @author carrot
 */
@Data
public class RegisterRequestUser implements Serializable {
    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String planetCode;
}
