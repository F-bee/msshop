package com.kk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kk.entity.User;
import com.kk.exception.Assert;
import com.kk.exception.ShopException;
import com.kk.form.UserLoginForm;
import com.kk.form.UserRegisterForm;
import com.kk.mapper.UserMapper;
import com.kk.result.ResponseEnum;
import com.kk.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kk.util.JwtUtil;
import com.kk.util.MD5Util;
import com.kk.util.RegexValidateUtil;
import com.kk.util.ResultVOUtil;
import com.kk.vo.ResultVO;
import com.kk.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2022-10-18
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    public UserMapper userMapper;
    @Autowired
    public RedisTemplate<String, String> redisTemplate;

    @Override
    public ResultVO register(UserRegisterForm userRegisterForm) {
        Assert.isTrue(RegexValidateUtil.checkMobile(userRegisterForm.getMobile()), ResponseEnum.MOBILE_ERROR);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", userRegisterForm.getMobile());
        User user = this.userMapper.selectOne(queryWrapper);
        if (user != null) {
            throw new ShopException(ResponseEnum.USER_EXIST.getMsg());
        }
        // 校验码
//        String code = this.redisTemplate.opsForValue().get("msshop-sms-code-" + userRegisterForm.getMobile());
//        Assert.equals(code, userRegisterForm.getCode(), ResponseEnum.USER_CODE_ERROR);
        User user1 = new User();
        user1.setMobile(userRegisterForm.getMobile());
        user1.setPassword(MD5Util.getSaltMD5(userRegisterForm.getPassword()));
        this.userMapper.insert(user1);
        return ResultVOUtil.success(null);
    }

    @Override
    public ResultVO login(UserLoginForm userLoginForm) {
        Assert.isTrue(RegexValidateUtil.checkMobile(userLoginForm.getMobile()), ResponseEnum.MOBILE_ERROR);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", userLoginForm.getMobile());
        User user = this.userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new ShopException(ResponseEnum.MOBILE_NOT_EXIST.getMsg());
        }
        // 验证密码
        boolean saltVerifyMD5 = MD5Util.getSaltVerifyMD5(userLoginForm.getPassword(), user.getPassword());
        if (!saltVerifyMD5) {
            throw new ShopException(ResponseEnum.USER_PASSWORD_ERROR.getMsg());
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setToken(JwtUtil.createToken(user.getUserId(), user.getMobile()));
        return ResultVOUtil.success(userVO);
    }
}
