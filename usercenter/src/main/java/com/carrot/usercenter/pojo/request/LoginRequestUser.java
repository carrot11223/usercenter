package com.carrot.usercenter.pojo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录
 *
 * @author carrot
 */
@Data
public class LoginRequestUser implements Serializable {
    private String userAccount;
    private String userPassword;
}
