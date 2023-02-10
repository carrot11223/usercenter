package com.carrot.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.carrot.usercenter.pojo.User;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务接口
 *
 * @author carrot
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册校验
     *
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param checkPassword 用户校验密码
     * @return 新用户注册的id
     */
    Long userRegister(String userAccount, String userPassword, String checkPassword,String planetCode);

    /**
     * 用户登录校验接口
     *
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @return 脱敏之后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户注销
     * @param request
     */
    int userLogout(HttpServletRequest request);


}
