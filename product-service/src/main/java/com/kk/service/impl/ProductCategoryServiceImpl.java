package com.kk.service.impl;

import com.kk.entity.ProductCategory;
import com.kk.entity.ProductInfo;
import com.kk.form.ProductInfoForm;
import com.kk.mapper.ProductCategoryMapper;
import com.kk.mapper.ProductInfoMapper;
import com.kk.service.ProductCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kk.util.ResultVOUtil;
import com.kk.vo.ResultVO;
import com.kk.vo.SellerProductCategoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 类目表 服务实现类
 * </p>
 *
 * @author admin
 * @since 2022-10-09
 */
@Service
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {

    @Autowired
    private ProductCategoryMapper productCategoryMapper;
    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Override
    public ResultVO findAllProductCategory() {
        List<ProductCategory> productCategoryList = this.productCategoryMapper.selectList(null);
        List<SellerProductCategoryVO> voList = new ArrayList<>();
        for (ProductCategory productCategory : productCategoryList) {
            SellerProductCategoryVO vo = new SellerProductCategoryVO();
            vo.setName(productCategory.getCategoryName());
            vo.setType(productCategory.getCategoryType());
            voList.add(vo);
        }
        Map<String, List<SellerProductCategoryVO>> map = new HashMap<>();
        map.put("content", voList);
        return ResultVOUtil.success(map);
    }


}
