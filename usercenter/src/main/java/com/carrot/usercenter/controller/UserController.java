package com.carrot.usercenter.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.carrot.usercenter.pojo.User;
import com.carrot.usercenter.pojo.request.LoginRequestUser;
import com.carrot.usercenter.pojo.request.RegisterRequestUser;
import com.carrot.usercenter.service.UserService;
import com.carrot.usercenter.utils.RoleUtils;
import com.carrot.usercenter.utils.SafetyUserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.carrot.usercenter.constant.UserConstant.ROLE_ADMIN;
import static com.carrot.usercenter.constant.UserConstant.USER_LOGIN_STATUS;

/**
 * 用户服务controller
 *
 * @author carrot
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    /**
     * 用户注册
     *
     * @param registerRequestUser
     * @return 注册id
     */
    @PostMapping("/register")
    public Long userRegister(@RequestBody RegisterRequestUser registerRequestUser) {
        if (registerRequestUser == null) {
            return null;
        }
        String userAccount = registerRequestUser.getUserAccount();
        String userPassword = registerRequestUser.getUserPassword();
        String checkPassword = registerRequestUser.getCheckPassword();
        //做基础判断，若为空，则无需进入业务层
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    /**
     * 用户登录
     *
     * @param loginRequestUser 登录用户
     * @return 脱敏的登录用户
     */
    @PostMapping("/login")
    public User userLogin(@RequestBody LoginRequestUser loginRequestUser, HttpServletRequest request) {
        if (loginRequestUser == null) {
            return null;
        }
        String userAccount = loginRequestUser.getUserAccount();
        String userPassword = loginRequestUser.getUserPassword();
        //做基础判断，若为空，则无需进入业务层
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        return userService.userLogin(userAccount, userPassword, request);
    }

    /**
     * 用户管理： 根据用户名查询用户
     *
     * @param username 用户名
     * @return User集合
     */
    @GetMapping("/select")
    public List<User> userSelect(String username,HttpServletRequest request){
        boolean result = RoleUtils.isAdmin(request);
        if (!result) {
          return new ArrayList<>();
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        //
        if (StringUtils.isNotBlank(username)) {
            wrapper.like("username",username);
        }
        List<User> list = userService.list(wrapper);
        return list.stream().map(user->
        SafetyUserUtils.getSafetyUser(user)).collect(Collectors.toList());
    }

    /**
     * 用户管理 ： 根据用户id删除用户
     *
     * @param id 用户id
     * @return 是否删除成功
     */
    @DeleteMapping("/")
    public boolean userDelete(@RequestParam Long id,HttpServletRequest request){
        boolean result = RoleUtils.isAdmin(request);
        if (!result) {
            return false;
        }
        if (id < 0){
            return false;
        }
       return userService.removeById(id);
    }
}
