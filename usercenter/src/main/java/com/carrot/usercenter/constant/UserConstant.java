package com.carrot.usercenter.constant;

import com.carrot.usercenter.pojo.User;

/**
 * 用户常量
 *
 * @author carrot
 */
public interface UserConstant {
    //session当中存放的登录成功的用户的key
    String USER_LOGIN_STATUS = "userLoginStatus";
    //将密码加密时的搅屎棍
    String toComplex = "carrot";
    //--------权限--------
    /**
     * 普通用户
     */
    int ROLE_COMMON = 0;
    /**
     * 管理员
     */
    int ROLE_ADMIN = 1;
}
