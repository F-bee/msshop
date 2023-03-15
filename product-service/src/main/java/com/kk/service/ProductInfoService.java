package com.kk.service;

import com.kk.entity.ProductInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kk.form.ProductInfoForm;
import com.kk.form.ProductInfoUpdateForm;
import com.kk.vo.ProductCategoryVO;
import com.kk.vo.ProductExcelVO;
import com.kk.vo.ResultVO;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author admin
 * @since 2022-10-09
 */
public interface ProductInfoService extends IService<ProductInfo> {
    ResultVO<List<ProductCategoryVO>> productList();
    BigDecimal getPriceById(Integer id);
    Boolean subStockById(Integer id, Integer quantity);
    ResultVO add(ProductInfoForm productInfoForm);
    ResultVO list(Integer page, Integer size);
    ResultVO like(String keyWord, Integer page, Integer size);
    ResultVO findByCategory(Integer categoryType, Integer page, Integer size);
    ResultVO findById(Integer id);
    ResultVO updateStatus(Integer id, Boolean status);
    ResultVO update(ProductInfoUpdateForm productInfoUpdateForm);
    List<ProductExcelVO> productExcelVOList();
    List<ProductInfo> excelToProductInfoList(InputStream inputStream);
}
