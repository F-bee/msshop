package com.kk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kk.entity.OrderDetail;
import com.kk.entity.OrderMaster;
import com.kk.entity.ProductInfo;
import com.kk.exception.ShopException;
import com.kk.feign.ProductFeign;
import com.kk.mapper.OrderDetailMapper;
import com.kk.mapper.OrderMasterMapper;
import com.kk.order.OrderDetailForm;
import com.kk.order.OrderMasterForm;
import com.kk.result.ResponseEnum;
import com.kk.service.OrderMasterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kk.util.ResultVOUtil;
import com.kk.vo.OrderDetailVO;
import com.kk.vo.OrderMasterVO;
import com.kk.vo.PageVO;
import com.kk.vo.ResultVO;
//import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.omg.CORBA.ORB;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author admin
 * @since 2022-10-10
 */
@Service
public class OrderMasterServiceImpl extends ServiceImpl<OrderMasterMapper, OrderMaster> implements OrderMasterService {

    @Autowired
    private ProductFeign productFeign;
    @Resource
    private OrderMasterMapper masterMapper;
    @Resource
    private OrderDetailMapper detailMapper;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 创建订单
     */
    @Override
    public String create(OrderMasterForm orderMasterForm) {
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setBuyerName(orderMasterForm.getName());
        orderMaster.setBuyerPhone(orderMasterForm.getPhone());
        orderMaster.setBuyerAddress(orderMasterForm.getAddress());
        orderMaster.setBuyerOpenid(orderMasterForm.getId());
        // 计算订单总价
        List<OrderDetailForm> items = orderMasterForm.getItems();
        BigDecimal amountTotal = new BigDecimal(0);
        for (OrderDetailForm item : items) {
            // 减库存
            Boolean aBoolean = this.productFeign.subStockById(item.getProductId(), item.getProductQuantity());
            System.out.println(aBoolean);
            if (!aBoolean) {
                throw new ShopException(ResponseEnum.CREATE_ORDER_ERROR.getMsg());
            }
            // 查询商品单价（调用product-service里的方法）
            System.out.println(item.getProductId());
            BigDecimal price = this.productFeign.findPriceById(item.getProductId());
            Integer quantity = item.getProductQuantity();
            BigDecimal amount = price.multiply(new BigDecimal(quantity));
            // 计算订单中所有商品的价格总和
            amountTotal = amountTotal.add(amount);
        }
        orderMaster.setOrderAmount(amountTotal);
        // 存主表（订单表）
        this.masterMapper.insert(orderMaster);
        // 存从表（订单详情表 数据数量和订单中商品种类数相同）
        for (OrderDetailForm item : items) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderMaster.getOrderId());
            orderDetail.setProductId(item.getProductId());
            orderDetail.setProductQuantity(item.getProductQuantity());
            ProductInfo productInfo = this.productFeign.findById(item.getProductId());
            orderDetail.setProductName(productInfo.getProductName());
            orderDetail.setProductIcon(productInfo.getProductIcon());
            orderDetail.setProductPrice(productInfo.getProductPrice());
            this.detailMapper.insert(orderDetail);
        }
        // 将消息存入 MQ
        this.rocketMQTemplate.convertAndSend("myTopic", "有新的订单");
        return orderMaster.getOrderId();
    }

    /**
     * 订单列表
     */
    @Override
    public ResultVO list(Integer buyerId, Integer page, Integer size) {
        Page<OrderMaster> orderMasterPage = new Page<>(page, size);
        QueryWrapper<OrderMaster> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("buyer_openid", buyerId);
        Page<OrderMaster> resultPage = this.masterMapper.selectPage(orderMasterPage, queryWrapper);
        return ResultVOUtil.success(resultPage.getRecords());
    }

    /**
     * 查询订单详情
     */
    @Override
    public ResultVO detail(Integer buyerId, String orderId) {
        QueryWrapper<OrderMaster> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("buyer_openId", buyerId)
                .eq("order_id", orderId);
        OrderMaster orderMaster = this.masterMapper.selectOne(queryWrapper);
        OrderMasterVO orderMasterVO = new OrderMasterVO();
        BeanUtils.copyProperties(orderMaster, orderMasterVO);
        // 通过 orderId 查询order_detail表中订单详情，填充 orderMasterVO 封装结果
        QueryWrapper<OrderDetail> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("order_id", orderId);
        List<OrderDetail> orderDetailList = this.detailMapper.selectList(queryWrapper1);
        List<OrderDetailVO> orderDetailVOList = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetailList) {
            OrderDetailVO orderDetailVO = new OrderDetailVO();
            BeanUtils.copyProperties(orderDetail, orderDetailVO);
            orderDetailVOList.add(orderDetailVO);
        }
        orderMasterVO.setOrderDetailVOList(orderDetailVOList);
        return ResultVOUtil.success(orderMasterVO);
    }

    /**
     * 取消订单
     */
    @Override
    public ResultVO cancel(Integer buyerId, String orderId) {
        Integer orderStatus = this.masterMapper.getOrderStatus(buyerId, orderId);
        if (orderStatus != 0) {
            throw new ShopException(ResponseEnum.ORDER_CANCEL_ERROR.getMsg());
        } else {
            this.masterMapper.updateOrderStatus(buyerId, orderId, 2);
        }
        return ResultVOUtil.success(null);
    }

    /**
     * 完成订单
     */
    @Override
    public ResultVO finish(String orderId) {
        Integer orderStatus = this.masterMapper.getOrderStatus2(orderId);
        if (orderStatus != 0) {
            throw new ShopException(ResponseEnum.ORDER_FINISH_ERROR.getMsg());
        } else {
            this.masterMapper.updateOrderStatus2(orderId, 1);
        }
        return ResultVOUtil.success(null);
    }

    /**
     * 支付订单
     */
    @Override
    public ResultVO pay(Integer buyerId, String orderId) {
        Integer orderStatus = this.masterMapper.getOrderStatus(buyerId, orderId);
        if (orderStatus != 0) {
            throw new ShopException(ResponseEnum.PAY_ERROR.getMsg());
        }
        Integer payStatus = this.masterMapper.getPayStatus(buyerId, orderId);
        if (payStatus == 1) {
            throw new ShopException(ResponseEnum.PAYED_ERROR.getMsg());
        }
        this.masterMapper.pay(buyerId, orderId);
        return ResultVOUtil.success(null);
    }

    @Override
    public ResultVO list(Integer page, Integer size) {
        Page<OrderMaster> orderMasterPage = new Page<>(page, size);
        Page<OrderMaster> resultPage = this.masterMapper.selectPage(orderMasterPage, null);
        PageVO pageVO = new PageVO();
        pageVO.setSize(resultPage.getSize());
        pageVO.setTotal(resultPage.getTotal());
        pageVO.setContent(resultPage.getRecords());
        return ResultVOUtil.success(pageVO);
    }

    @Override
    public ResultVO cancel(String orderId) {
        Integer orderStatus = this.masterMapper.getOrderStatus2(orderId);
        if (orderStatus != 0) {
            throw new ShopException(ResponseEnum.ORDER_CANCEL_ERROR.getMsg());
        } else {
            this.masterMapper.updateOrderStatus2(orderId, 2);
        }
        return ResultVOUtil.success(null);
    }

}
