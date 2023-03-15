package com.kk.service;

import com.kk.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kk.form.AdminLoginForm;
import com.kk.vo.ResultVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2022-10-18
 */
public interface AdminService extends IService<Admin> {
    ResultVO login(AdminLoginForm form);
}
