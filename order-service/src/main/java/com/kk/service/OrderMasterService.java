package com.kk.service;

import com.kk.entity.OrderMaster;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kk.order.OrderMasterForm;
import com.kk.vo.ResultVO;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author admin
 * @since 2022-10-10
 */
public interface OrderMasterService extends IService<OrderMaster> {
    String create(OrderMasterForm orderMasterForm);
    ResultVO list(Integer buyerId, Integer page, Integer size);
    ResultVO detail(Integer buyerId, String orderId);
    ResultVO cancel(Integer buyerId, String orderId);
    ResultVO finish(String orderId);
    ResultVO pay(Integer buyerId, String orderId);
    ResultVO list(Integer page, Integer size);
    ResultVO cancel(String orderId);
}
