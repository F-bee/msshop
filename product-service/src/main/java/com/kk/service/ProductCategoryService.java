package com.kk.service;

import com.kk.entity.ProductCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kk.form.ProductInfoForm;
import com.kk.vo.ResultVO;

/**
 * <p>
 * 类目表 服务类
 * </p>
 *
 * @author admin
 * @since 2022-10-09
 */
public interface ProductCategoryService extends IService<ProductCategory> {
    ResultVO findAllProductCategory();

}
