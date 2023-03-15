package com.kk.mapper;

import com.kk.entity.ProductCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 类目表 Mapper 接口
 * </p>
 *
 * @author admin
 * @since 2022-10-09
 */
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {
    String getNameByType(Integer type);
}
