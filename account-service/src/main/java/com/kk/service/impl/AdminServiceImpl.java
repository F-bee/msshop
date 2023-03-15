package com.kk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kk.entity.Admin;
import com.kk.exception.ShopException;
import com.kk.form.AdminLoginForm;
import com.kk.mapper.AdminMapper;
import com.kk.result.ResponseEnum;
import com.kk.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kk.util.JwtUtil;
import com.kk.util.ResultVOUtil;
import com.kk.vo.AdminVO;
import com.kk.vo.ResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Resource
    private AdminMapper adminMapper;

    @Override
    public ResultVO login(AdminLoginForm form) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", form.getUsername());
        Admin one = this.adminMapper.selectOne(queryWrapper);
        if (one == null) {
            throw new ShopException(ResponseEnum.ADMIN_USERNAME_NULL.getMsg());
        }
        if (!one.getPassword().equals(form.getPassword())) {
            throw new ShopException(ResponseEnum.USER_PASSWORD_ERROR.getMsg());
        }
        AdminVO adminVO = new AdminVO();
        BeanUtils.copyProperties(one, adminVO);
        adminVO.setToken(JwtUtil.createToken(one.getAdminId(), one.getUsername()));
        return ResultVOUtil.success(adminVO);
    }
}
