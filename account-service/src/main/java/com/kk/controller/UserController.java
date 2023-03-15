package com.kk.controller;


import com.kk.exception.ShopException;
import com.kk.form.UserLoginForm;
import com.kk.form.UserRegisterForm;
import com.kk.result.ResponseEnum;
import com.kk.service.UserService;
import com.kk.util.JwtUtil;
import com.kk.util.ResultVOUtil;
import com.kk.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2022-10-18
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResultVO register(@Valid @RequestBody UserRegisterForm userRegisterForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ShopException(ResponseEnum.USER_INFO_NULL.getMsg());
        }
        return this.userService.register(userRegisterForm);
    }

    @GetMapping("/login")
    public ResultVO login(@Valid UserLoginForm userLoginForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ShopException(ResponseEnum.USER_INFO_NULL.getMsg());
        }
        return this.userService.login(userLoginForm);
    }

    @GetMapping("/checkToken")
    public ResultVO checkToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        boolean checkToken = JwtUtil.checkToken(token);
        if (!checkToken) {
            return ResultVOUtil.fail(ResponseEnum.TOKEN_ERROR.getMsg());
        }
        return ResultVOUtil.success(null);
    }
}

