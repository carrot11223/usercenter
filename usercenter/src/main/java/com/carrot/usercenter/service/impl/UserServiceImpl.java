package com.carrot.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.carrot.usercenter.common.ErrorCode;
import com.carrot.usercenter.exception.BusinessException;
import com.carrot.usercenter.pojo.User;
import com.carrot.usercenter.service.UserService;
import com.carrot.usercenter.mapper.UserMapper;
import com.carrot.usercenter.utils.SafetyUserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
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
    public Long userRegister(String userAccount, String userPassword, String checkPassword,String planetCode) {
        //1.判断是否为空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword,planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户输入为空");
        }
        //判断账户长度是否小于4
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户长度小于4");

        }
        //判断密码是否小于8
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码小于8");

        }
        //星球编号长度的限制
        if (planetCode.length() > 5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球编号长度大于5");

        }
        //星球编号长度的限制
        if (Integer.parseInt(planetCode)< 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球编号小于0");

        }
        //判断账户是否包含特殊字符，使用正则表达式
        String validPatern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPatern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户当中包含特殊字符");
        }
        //判断密码和校验
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次输入密码不一致");

        }
        //判断是否账户重复
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userAccount", userAccount);
        long count = this.count(wrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户已存在，请重新登录");

        }

        //判断是否星球编号重复
         wrapper = new QueryWrapper<>();
        wrapper.eq("planetCode", planetCode);
        count = this.count(wrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球编号已存在，编号重复");
        }
        //2.对密码进行加密，存储到数据库当中
        String newPassword = DigestUtils.md5DigestAsHex((toComplex + userPassword).getBytes());
        //3.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(newPassword);
        user.setPlanetCode(planetCode);
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
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"登录账户或密码为空");
        }
        //判断账户长度是否小于4
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户长度小于4");
        }
        //判断密码是否小于8
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户密码小于8");
        }
        //判断账户是否包含特殊字符，使用正则表达式
        String validPatern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPatern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户当中包含特殊字符");
        }
        //对密码进行加密，在数据库中查询
        String newPassword = DigestUtils.md5DigestAsHex((toComplex + userPassword).getBytes());
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userAccount", userAccount);
        wrapper.eq("userPassword", newPassword);
        User resultUser = this.getOne(wrapper);
        if (resultUser == null) {
            //日志打印错误信息，方便后期定位
            //log.info("login failed,cause userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号密码不匹配");
        }
        //对用户进行脱敏操作
        User safetyUser = SafetyUserUtils.getSafetyUser(resultUser);
        //记录用户登录态，session
        request.getSession().setAttribute(USER_LOGIN_STATUS, safetyUser);
        return safetyUser;
    }

    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public int userLogout(HttpServletRequest request) {
       request.getSession().removeAttribute(USER_LOGIN_STATUS);
       return 0;
    }
}




