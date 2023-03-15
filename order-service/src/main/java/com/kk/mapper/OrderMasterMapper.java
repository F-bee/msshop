package com.kk.mapper;

import com.kk.entity.OrderMaster;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author admin
 * @since 2022-10-10
 */

public interface OrderMasterMapper extends BaseMapper<OrderMaster> {
    Integer getOrderStatus(Integer buyerId, String orderId);
    Integer getOrderStatus2(String orderId);
    Integer getPayStatus(Integer buyerId, String orderId);
    void updateOrderStatus(Integer buyerId, String orderId, Integer status);
    void updateOrderStatus2(String orderId, Integer status);
    void pay(Integer buyerId, String orderId);
}
