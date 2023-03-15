package com.kk.controller;


import com.kk.exception.ShopException;
import com.kk.form.AdminLoginForm;
import com.kk.result.ResponseEnum;
import com.kk.service.AdminService;
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
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/login")
    public ResultVO login(@Valid AdminLoginForm adminLoginForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ShopException(ResponseEnum.ADMIN_INFO_NULL.getMsg());
        }
        return this.adminService.login(adminLoginForm);
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

