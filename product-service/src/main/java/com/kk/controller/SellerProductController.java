package com.kk.controller;


import com.alibaba.excel.EasyExcel;
import com.kk.entity.ProductInfo;
import com.kk.form.ProductInfoForm;
import com.kk.form.ProductInfoUpdateForm;
import com.kk.handler.CustomCellWriteHandler;
import com.kk.result.ResponseEnum;
import com.kk.service.ProductCategoryService;
import com.kk.service.ProductInfoService;
import com.kk.util.ResultVOUtil;
import com.kk.vo.ProductExcelVO;
import com.kk.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author admin
 * @since 2022-10-09
 */
@RestController
@RequestMapping("/seller/product")
public class SellerProductController {

    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private ProductInfoService productInfoService;

    @GetMapping("/findAllProductCategory")
    public ResultVO findAllProductCategory() {
        return productCategoryService.findAllProductCategory();
    }

    @PostMapping("/add")
    public ResultVO add(@RequestBody ProductInfoForm productInfoForm) {
        return this.productInfoService.add(productInfoForm);
    }

    @GetMapping("/list/{page}/{size}")
    public ResultVO list(
            @PathVariable("page") Integer page,
            @PathVariable("size") Integer size
    ) {
        return this.productInfoService.list(page, size);
    }

    @GetMapping("/like/{keyWord}/{page}/{size}")
    public ResultVO like(
            @PathVariable("keyWord") String keyWord,
            @PathVariable("page") Integer page,
            @PathVariable("size") Integer size
    ) {
        return this.productInfoService.like(keyWord, page, size);
    }

    // Restful 风格 url 传参不能通过 @PathVariable 添加 require=false 使参数变为非必须，这里重载该方法，处理未传 keyWord 的情况
    // 这样可以使用 service 中同一个方法实现
    @GetMapping("/like/{page}/{size}")
    public ResultVO like(
            @PathVariable("page") Integer page,
            @PathVariable("size") Integer size
    ) {
        return this.productInfoService.like(null, page, size);
    }

    @GetMapping("/findByCategory/{categoryType}/{page}/{size}")
    public ResultVO findByCategory(
            @PathVariable("categoryType") Integer categoryType,
            @PathVariable("page") Integer page,
            @PathVariable("size") Integer size
    ) {
        return this.productInfoService.findByCategory(categoryType, page, size);
    }

    @GetMapping("/findById/{id}")
    public ResultVO findById(@PathVariable("id") Integer id) {
        return this.productInfoService.findById(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResultVO delete(@PathVariable("id") Integer id) {
        boolean remove = this.productInfoService.removeById(id);
        if (remove) {
            return ResultVOUtil.success(null);
        }
        return ResultVOUtil.fail(ResponseEnum.PRODUCT_DELETE_FAIL.getMsg());
    }

    @PutMapping("/updateStatus/{id}/{status}")
    public ResultVO updateStatus(
            @PathVariable("id") Integer id,
            @PathVariable("status") Boolean status
    ) {
        return this.productInfoService.updateStatus(id, status);
    }

    @PutMapping("/update")
    public ResultVO update(@RequestBody ProductInfoUpdateForm form) {
        return this.productInfoService.update(form);
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            String fileName = URLEncoder.encode("商品信息", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            List<ProductExcelVO> productExcelVOList = this.productInfoService.productExcelVOList();
            EasyExcel.write(response.getOutputStream(), ProductExcelVO.class)
                    .registerWriteHandler(new CustomCellWriteHandler())
                    .sheet("商品信息")
                    .doWrite(productExcelVOList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/import")
    public ResultVO importData(@RequestParam("file") MultipartFile file){
        List<ProductInfo> productInfoList = null;
        try {
            productInfoList = this.productInfoService.excelToProductInfoList(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (productInfoList == null) {
            return ResultVOUtil.fail("导入Excel失败！");
        }
        // 批量插入
        boolean result = this.productInfoService.saveBatch(productInfoList);
        if (result) {
            return ResultVOUtil.success(null);
        }
        return ResultVOUtil.fail("导入Excel失败！");
    }

}

