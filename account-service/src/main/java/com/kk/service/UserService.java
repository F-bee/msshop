package com.kk.service;

import com.kk.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kk.form.UserLoginForm;
import com.kk.form.UserRegisterForm;
import com.kk.vo.ResultVO;
import org.springframework.validation.BindingResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2022-10-18
 */
public interface UserService extends IService<User> {
    ResultVO register(UserRegisterForm userRegisterForm);
    ResultVO login(UserLoginForm userLoginForm);
}
