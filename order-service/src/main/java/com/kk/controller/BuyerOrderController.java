package com.kk.controller;

import com.kk.order.OrderMasterForm;
import com.kk.service.OrderMasterService;
import com.kk.util.ResultVOUtil;
import com.kk.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 订单详情表 前端控制器
 * </p>
 *
 * @author admin
 * @since 2022-10-10
 */
@RestController
@RequestMapping("/buyer/order")
public class BuyerOrderController {

    @Autowired
    private OrderMasterService orderMasterService;

    @PostMapping("/create")
    public ResultVO createOrder(@RequestBody OrderMasterForm orderMasterForm) {
        String orderId = this.orderMasterService.create(orderMasterForm);
        Map<String, String> map = new HashMap<>();
        map.put("orderId", orderId);
        return ResultVOUtil.success(map);
    }

    @GetMapping("/list/{buyerId}/{page}/{size}")
    public ResultVO list(
            @PathVariable("buyerId") Integer buyerId,
            @PathVariable("page") Integer page,
            @PathVariable("size") Integer size
    ) {
        return this.orderMasterService.list(buyerId, page, size);
    }

    @GetMapping("/detail/{buyerId}/{orderId}")
    public ResultVO detail(
            @PathVariable("buyerId") Integer buyerId,
            @PathVariable("orderId") String orderId
    ) {
        return this.orderMasterService.detail(buyerId, orderId);
    }

    @PutMapping("/cancel/{buyerId}/{orderId}")
    public ResultVO cancel(
            @PathVariable("buyerId") Integer buyerId,
            @PathVariable("orderId") String orderId
    ) {
        return this.orderMasterService.cancel(buyerId, orderId);
    }

    @PutMapping("/finish/{orderId}")
    public ResultVO finish(@PathVariable("orderId") String orderId) {
        return this.orderMasterService.finish(orderId);
    }

    @PutMapping("/pay/{buyerId}/{orderId}")
    public ResultVO pay(
            @PathVariable("buyerId") Integer buyerId,
            @PathVariable("orderId") String orderId
    ) {
        return this.orderMasterService.pay(buyerId, orderId);
    }

}

