package com.carrot.usercenter.utils;

import com.carrot.usercenter.pojo.User;

public class SafetyUserUtils {
    public static User getSafetyUser(User resultUser){
        if (resultUser==null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(resultUser.getId());
        safetyUser.setUsername(resultUser.getUsername());
        safetyUser.setUserAccount(resultUser.getUserAccount());
        safetyUser.setAvatarUrl(resultUser.getAvatarUrl());
        safetyUser.setGender(resultUser.getGender());
        safetyUser.setPlanetCode(resultUser.getPlanetCode());
        // safetyUser.setUserPassword(""); 密码不能返回，脱敏的核心
        safetyUser.setRole(resultUser.getRole());
        safetyUser.setPhone(resultUser.getPhone());
        safetyUser.setEmail(resultUser.getEmail());
        safetyUser.setUserStatus(resultUser.getUserStatus());
        return safetyUser;
    }
}
