package com.carrot.usercenter.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.carrot.usercenter.common.BaseResponse;
import com.carrot.usercenter.common.ErrorCode;
import com.carrot.usercenter.exception.BusinessException;
import com.carrot.usercenter.pojo.User;
import com.carrot.usercenter.pojo.request.LoginRequestUser;
import com.carrot.usercenter.pojo.request.RegisterRequestUser;
import com.carrot.usercenter.service.UserService;
import com.carrot.usercenter.utils.ResultUtils;
import com.carrot.usercenter.utils.RoleUtils;
import com.carrot.usercenter.utils.SafetyUserUtils;
import org.apache.commons.lang3.StringUtils;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public BaseResponse<Long> userRegister(@RequestBody RegisterRequestUser registerRequestUser) {
        if (registerRequestUser == null) {
            //return ResultUtils.error(ErrorCode.NULL_ERROR);
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = registerRequestUser.getUserAccount();
        String userPassword = registerRequestUser.getUserPassword();
        String checkPassword = registerRequestUser.getCheckPassword();
        String planetCode = registerRequestUser.getPlanetCode();
        //做基础判断，若为空，则无需进入业务层
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword,planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(id);
    }

    /**
     * 用户登录
     *
     * @param loginRequestUser 登录用户
     * @return 脱敏的登录用户
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody LoginRequestUser loginRequestUser, HttpServletRequest request) {
        if (loginRequestUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = loginRequestUser.getUserAccount();
        String userPassword = loginRequestUser.getUserPassword();
        //做基础判断，若为空，则无需进入业务层
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    /**
     * 用户注销
     * @param request
     * @return 返回为1 注销成功
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int i = userService.userLogout(request);
        //注销成功
        return ResultUtils.success("账号注销成功");
    }

    /**
     * 获取当前用户信息
     * @param request
     * @return从数据库查到的用户信息
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATUS);
        User user = (User)userObj;
        if (user==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //对于频繁更新的数据，尽量去数据库里面查，从session里面获取的东西都是不经常变动的
        User safeUser = userService.getById(user.getId());
        User safetyUser = SafetyUserUtils.getSafetyUser(safeUser);
        return ResultUtils.success(safetyUser);
        //return safetyUser;
        //return new BaseResponse(0,safetyUser,"ok");
    }

    /**
     * 用户管理： 根据用户名查询用户
     *
     * @param username 用户名
     * @return User集合
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> userSelect(String username,HttpServletRequest request){
        boolean result = RoleUtils.isAdmin(request);
        if (!result) {
          throw new BusinessException(ErrorCode.NO_AUTH);
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        //
        if (StringUtils.isNotBlank(username)) {
            wrapper.like("username",username);
        }
        List<User> list = userService.list(wrapper);
        List<User> collect = list.stream().map(user ->
                SafetyUserUtils.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(collect);
        //return new BaseResponse(0,userList,"ok");
    }

    /**
     * 用户管理 ： 根据用户id删除用户
     *
     * @param id 用户id
     * @return 是否删除成功
     */
    @DeleteMapping("/")
    public BaseResponse<Boolean> userDelete(@RequestParam Long id,HttpServletRequest request){
        boolean result = RoleUtils.isAdmin(request);
        if (!result) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (id < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean res = userService.removeById(id);
        return ResultUtils.success(res);
        //return new BaseResponse(0,b,"ok");
    }
    
}
