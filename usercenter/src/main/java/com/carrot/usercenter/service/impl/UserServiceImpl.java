package com.carrot.usercenter.service.impl;

import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.carrot.usercenter.pojo.User;
import com.carrot.usercenter.service.UserService;
import com.carrot.usercenter.mapper.UserMapper;
import com.carrot.usercenter.utils.SafetyUserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.Wrapper;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.carrot.usercenter.constant.UserConstant.USER_LOGIN_STATUS;
import static com.carrot.usercenter.constant.UserConstant.toComplex;

/**
 * 用户服务接口
 *
 * @author carrot
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    /**
     * 用户注册实现service
     *
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param checkPassword 用户校验密码
     * @return 新用户注册的id
     */
    @Override
    public Long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1.判断是否为空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return -1L;
        }
        //判断账户长度是否小于4
        if (userAccount.length() < 4) {
            return -1L;
        }
        //判断密码是否小于8
        if (userPassword.length() < 8) {
            return -1L;
        }
        //判断账户是否包含特殊字符，使用正则表达式
        String validPatern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPatern).matcher(userAccount);
        if (matcher.find()) {
            return -1L;
        }
        //判断密码和校验
        if (!userPassword.equals(checkPassword)) {
            return -1L;
        }
        //判断是否账户重复
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userAccount", userAccount);
        long count = this.count(wrapper);
        if (count > 0) {
            return -1L;
        }
        //2.对密码进行加密，存储到数据库当中
        String newPassword = DigestUtils.md5DigestAsHex((toComplex + userPassword).getBytes());
        //3.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(newPassword);
        this.save(user);
        return user.getId();
    }

    /**
     * 用户登录校验实现类
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @return 脱敏之后的用户信息
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //判断是否为空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            //to do自定义异常
            return null;
        }
        //判断账户长度是否小于4
        if (userAccount.length() < 4) {
            return null;
        }
        //判断密码是否小于8
        if (userPassword.length() < 8) {
            return null;
        }
        //判断账户是否包含特殊字符，使用正则表达式
        String validPatern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPatern).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }
        //对密码进行加密，在数据库中查询
        String newPassword = DigestUtils.md5DigestAsHex((toComplex + userPassword).getBytes());
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userAccount", userAccount);
        wrapper.eq("userPassword", newPassword);
        User resultUser = this.getOne(wrapper);
        if (resultUser == null) {
            //日志打印错误信息，方便后期定位
            log.info("login failed,cause userAccount cannot match userPassword");
            return null;
        }
        //对用户进行脱敏操作
        User safetyUser = SafetyUserUtils.getSafetyUser(resultUser);
        //记录用户登录态，session
        request.getSession().setAttribute(USER_LOGIN_STATUS, safetyUser);
        return safetyUser;
    }
}




