package com.kk.controller;

import com.kk.service.OrderDetailService;
import com.kk.service.OrderMasterService;
import com.kk.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author admin
 * @since 2022-10-10
 */
@RestController
@RequestMapping("/seller/order")
public class SellerOrderController {

    @Autowired
    private OrderMasterService orderMasterService;
    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping("/list/{page}/{size}")
    public ResultVO list(
            @PathVariable("page") Integer page,
            @PathVariable("size") Integer size
    ) {
        return this.orderMasterService.list(page, size);
    }

    @PutMapping("/cancel/{orderId}")
    public ResultVO cancel(@PathVariable("orderId") String orderId) {
        return this.orderMasterService.cancel(orderId);
    }

    @PutMapping("/finish/{orderId}")
    public ResultVO finish(@PathVariable("orderId") String orderId) {
        return this.orderMasterService.finish(orderId);
    }

    @GetMapping("/barSale")
    public ResultVO barSale() {
        return this.orderDetailService.barData();
    }

    @GetMapping("/basicLineSale")
    public ResultVO basicLineSale() {
        return this.orderDetailService.lineData();
    }

    @GetMapping("/stackedLineSale")
    public ResultVO stackedLineSale() {
        return this.orderDetailService.lineData2();
    }

}

