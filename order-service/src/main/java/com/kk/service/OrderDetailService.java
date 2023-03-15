package com.kk.service;

import com.kk.entity.OrderDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kk.vo.ResultVO;

/**
 * <p>
 * 订单详情表 服务类
 * </p>
 *
 * @author admin
 * @since 2022-10-10
 */
public interface OrderDetailService extends IService<OrderDetail> {
    ResultVO barData();
    ResultVO lineData();
    ResultVO lineData2();
}
