package com.kk.mapper;

import com.kk.entity.ProductInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.math.BigDecimal;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author admin
 * @since 2022-10-09
 */
public interface ProductInfoMapper extends BaseMapper<ProductInfo> {
    BigDecimal getPriceById(Integer id);
    Integer getStockById(Integer id);
    void updateStockById(Integer id, Integer quantity);
    void updateStatus(Integer id, Integer status);
}
