package com.carrot.usercenter.service;

import com.carrot.usercenter.pojo.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 用户服务测试
 *
 * @author carrot
 */
@SpringBootTest
class UserServiceTest {
    @Autowired
    UserService userService;

    /**
     * 测试mapper是否导入成功，以及crud的基础使用是否成功
     */
    @Test
    void testAddUser(){
        User user = new User();
        user.setUsername("carrot");
        user.setUserAccount("123");
        user.setAvatarUrl("https://profile.csdnimg.cn/D/9/9/0_m0_67186869");
        user.setGender((byte)1);
        user.setUserPassword("123");
        user.setPhone("456");
        user.setEmail("789");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(true);
}
    /**
     * 测试注册用户
     */
    @Test
    void userRegister() {
        //测试账户不小于4位
        String userAccount = "ca";
        String userPassword = "12345678";
        String checkPassword = "12345678";
        String planetCode = "1";
        Long result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        //测试密码不小于8位
        userAccount = "carrot";
        userPassword = "123456";
        checkPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        //测试账户重复
        userAccount = "1234";
        userPassword = "123456789";
        checkPassword = "123456789";
        result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        //测试账户包含特殊字符
        userAccount = "123】do";
        userPassword = "123456789";
        checkPassword = "123456789";
        result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        //测试密码和校验密码不相同
        userAccount = "22333";
        userPassword = "123456789";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        //测试正确的数据
        userAccount = "carrotM";
        userPassword = "123456789";
        checkPassword = "123456789";
        result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertTrue(result>0);
    }
}