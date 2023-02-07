package com.carrot.usercenter.utils;

import com.carrot.usercenter.pojo.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

import static com.carrot.usercenter.constant.UserConstant.ROLE_ADMIN;
import static com.carrot.usercenter.constant.UserConstant.USER_LOGIN_STATUS;

/**
 * 判断用户是否为管理员
 *
 * @author carrot
 */
public class RoleUtils {
    public static boolean isAdmin(HttpServletRequest request){
        HttpSession session = request.getSession();
        Object attribute = session.getAttribute(USER_LOGIN_STATUS);
        User user = (User) attribute;
        //如果不是管理员
        if (user==null || user.getRole()!=ROLE_ADMIN) {
            return false;
        }else {
            return true;
        }
    }
}
