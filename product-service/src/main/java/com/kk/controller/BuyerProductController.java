package com.kk.controller;


import com.kk.entity.ProductInfo;
import com.kk.service.ProductInfoService;
import com.kk.vo.ProductCategoryVO;
import com.kk.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 类目表 前端控制器
 * </p>
 *
 * @author admin
 * @since 2022-10-09
 */
@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductInfoService productInfoService;

    @GetMapping("/list")
    public ResultVO<List<ProductCategoryVO>> list() {
        return this.productInfoService.productList();
    }

    @GetMapping("/findPriceById/{id}")
    public BigDecimal findPriceById(@PathVariable("id") Integer id) {
        return this.productInfoService.getPriceById(id);
        /*
        * 直接使用 api 会查询表中所有字段，效率较低
        * */
//        return this.productInfoService.getById(id).getProductPrice();
    }

    @GetMapping("/findById/{id}")
    public ProductInfo findProductById(@PathVariable("id") Integer id) {
        return this.productInfoService.getById(id);
    }

    @PutMapping("/subStockById/{id}/{quantity}")
    public Boolean subStockById(@PathVariable("id") Integer id, @PathVariable("quantity") Integer quantity) {
        return this.productInfoService.subStockById(id, quantity);
    }

}

