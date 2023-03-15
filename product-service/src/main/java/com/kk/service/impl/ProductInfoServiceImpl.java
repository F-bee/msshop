package com.kk.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kk.entity.ProductCategory;
import com.kk.entity.ProductInfo;
import com.kk.exception.ShopException;
import com.kk.form.ProductInfoForm;
import com.kk.form.ProductInfoUpdateForm;
import com.kk.mapper.ProductCategoryMapper;
import com.kk.mapper.ProductInfoMapper;
import com.kk.result.ResponseEnum;
import com.kk.service.ProductInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kk.util.ExecutorUtil;
import com.kk.util.ResultVOUtil;
import com.kk.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author admin
 * @since 2022-10-09
 */
@Service
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo> implements ProductInfoService {

    @Autowired
    private ProductCategoryMapper categoryMapper;
    @Autowired
    private ProductInfoMapper infoMapper;

    @Override
    public ResultVO productList() {
        List<ProductCategory> productCategoryList = this.categoryMapper.selectList(null);
        List<ProductCategoryVO> productCategoryVOList = new ArrayList<>();
        for (ProductCategory productCategory : productCategoryList) {
            QueryWrapper<ProductInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("category_type", productCategory.getCategoryType());
            List<ProductInfo> productInfoList = this.infoMapper.selectList(queryWrapper);
            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for (ProductInfo productInfo : productInfoList) {
                ProductInfoVO productInfoVO = new ProductInfoVO();
                BeanUtils.copyProperties(productInfo, productInfoVO);
                productInfoVOList.add(productInfoVO);
            }
            ProductCategoryVO productCategoryVO = new ProductCategoryVO();
            productCategoryVO.setName(productCategory.getCategoryName());
            productCategoryVO.setType(productCategory.getCategoryType());
            productCategoryVO.setGoods(productInfoVOList);
            productCategoryVOList.add(productCategoryVO);
        }
        return ResultVOUtil.success(productCategoryVOList);
    }

    @Override
    public BigDecimal getPriceById(Integer id) {
        return this.infoMapper.getPriceById(id);
    }

//    @Override
//    public Boolean subStockById(Integer id, Integer quantity) {
//        ExecutorUtil.executorService.execute(()-> {
//            // 判断库存是否充足
//            Integer stock = this.infoMapper.getStockById(id);
//            if(stock < quantity) {
//                throw new ShopException(ResponseEnum.PRODUCT_STOCK_ERROR.getMsg());
//            }
//            // 库存充足则减库存
//            stock -= quantity;
//            // 更新数据库中库存数据
//            this.infoMapper.updateStockById(id, stock);
//        });
//        return true;
//    }
    @Override
    public Boolean subStockById(Integer id, Integer quantity) {
        final Boolean[] flag = {true};
        Future<?> future = ExecutorUtil.executorService.submit(()->{ // submit方法可以回到当前线程，execute方法不能
            try {
                // 判断库存是否充足
                Integer stock = this.infoMapper.getStockById(id);
                if (stock < quantity) {
                    throw new ShopException(ResponseEnum.PRODUCT_STOCK_ERROR.getMsg());
                }
                // 减库存
                stock -= quantity;
                // 更新数据库中库存数据
                this.infoMapper.updateStockById(id, stock);
            } catch (ShopException e) {
                flag[0] = false;
            }
        });
        try {
            // 获取一下返回值，否则执行出错的任务所属线程不会抛出异常
            Object o = future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return flag[0];
    }

    @Override
    public ResultVO add(ProductInfoForm productInfoForm) {
        ProductInfo productInfo = new ProductInfo();
        BeanUtils.copyProperties(productInfoForm, productInfo);
        this.infoMapper.insert(productInfo);
        return ResultVOUtil.success(null);
    }

    @Override
    public ResultVO list(Integer page, Integer size) {
        Page<ProductInfo> productInfoPage = new Page<>(page, size);
        Page<ProductInfo> resultPage = this.infoMapper.selectPage(productInfoPage, null);
        PageVO pageVO = new PageVO();
        pageVO.setSize(resultPage.getSize());
        pageVO.setTotal(resultPage.getTotal());
        List<SellerProductInfoVO> voList = new ArrayList<>();
        for (ProductInfo record : resultPage.getRecords()) {
            SellerProductInfoVO vo = new SellerProductInfoVO();
            BeanUtils.copyProperties(record, vo);
            if (record.getProductStatus() == 0) {
                vo.setStatus(false);
            } else if (record.getProductStatus() == 1){
                vo.setStatus(true);
            }
            vo.setCategoryName(this.categoryMapper.getNameByType(record.getCategoryType()));
            voList.add(vo);
        }
        pageVO.setContent(voList);
        return ResultVOUtil.success(pageVO);
    }

    @Override
    public ResultVO like(String keyWord, Integer page, Integer size) {
        Page<ProductInfo> productInfoPage = new Page<>(page, size);
        QueryWrapper<ProductInfo> queryWrapper = new QueryWrapper<>();
        // keyWord 为空时才添加模糊查询的条件；可通过商品名和商品描述进行模糊匹配
        queryWrapper.like(StringUtils.isNotBlank(keyWord), "product_name", keyWord)
                .or()
                .like(StringUtils.isNotBlank(keyWord), "product_description", keyWord);
        Page<ProductInfo> resultPage = this.infoMapper.selectPage(productInfoPage, queryWrapper);
        PageVO pageVO = new PageVO();
        pageVO.setSize(resultPage.getSize());
        pageVO.setTotal(resultPage.getTotal());
        List<SellerProductInfoVO> voList = new ArrayList<>();
        for (ProductInfo record : resultPage.getRecords()) {
            SellerProductInfoVO vo = new SellerProductInfoVO();
            BeanUtils.copyProperties(record, vo);
            if (record.getProductStatus() == 0) {
                vo.setStatus(false);
            } else if (record.getProductStatus() == 1){
                vo.setStatus(true);
            }
            vo.setCategoryName(this.categoryMapper.getNameByType(record.getCategoryType()));
            voList.add(vo);
        }
        pageVO.setContent(voList);
        return ResultVOUtil.success(pageVO);
    }

    @Override
    public ResultVO findByCategory(Integer categoryType, Integer page, Integer size) {
        Page<ProductInfo> productInfoPage = new Page<>(page, size);
        QueryWrapper<ProductInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_type", categoryType);
        Page<ProductInfo> resultPage = this.infoMapper.selectPage(productInfoPage, queryWrapper);
        PageVO pageVO = new PageVO();
        pageVO.setSize(resultPage.getSize());
        pageVO.setTotal(resultPage.getTotal());
        List<SellerProductInfoVO> voList = new ArrayList<>();
        for (ProductInfo record : resultPage.getRecords()) {
            SellerProductInfoVO vo = new SellerProductInfoVO();
            BeanUtils.copyProperties(record, vo);
            if (record.getProductStatus() == 0) {
                vo.setStatus(false);
            } else if (record.getProductStatus() == 1){
                vo.setStatus(true);
            }
            vo.setCategoryName(this.categoryMapper.getNameByType(record.getCategoryType()));
            voList.add(vo);
        }
        pageVO.setContent(voList);
        return ResultVOUtil.success(pageVO);
    }

    @Override
    public ResultVO findById(Integer id) {
        ProductInfo productInfo = this.infoMapper.selectById(id);
        SellerProductInfoVO2 vo = new SellerProductInfoVO2();
        BeanUtils.copyProperties(productInfo, vo);
        if (productInfo.getProductStatus() == 0) {
            vo.setStatus(false);
        } else if (productInfo.getProductStatus() == 1){
            vo.setStatus(true);
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("categoryType", productInfo.getCategoryType());
        vo.setCategory(map);
        return ResultVOUtil.success(vo);
    }

    @Override
    public ResultVO updateStatus(Integer id, Boolean status) {
        if (status) {
            this.infoMapper.updateStatus(id, 1);
        } else {
            this.infoMapper.updateStatus(id, 0);
        }
        return ResultVOUtil.success(null);
    }

    @Override
    public ResultVO update(ProductInfoUpdateForm productInfoUpdateForm) {
        ProductInfo productInfo = new ProductInfo();
        BeanUtils.copyProperties(productInfoUpdateForm, productInfo);
        if (productInfoUpdateForm.getProductStatus()) {
            productInfo.setProductStatus(1);
        } else {
            productInfo.setProductStatus(0);
        }
        productInfo.setCategoryType(productInfoUpdateForm.getCategory().getCategoryType());
        this.infoMapper.updateById(productInfo);
        return ResultVOUtil.success(null);
    }

    @Override
    public List<ProductExcelVO> productExcelVOList() {
        List<ProductInfo> productInfoList = this.infoMapper.selectList(null);
        List<ProductExcelVO> voList = new ArrayList<>();
        for (ProductInfo productInfo : productInfoList) {
            ProductExcelVO vo = new ProductExcelVO();
            BeanUtils.copyProperties(productInfo, vo);
            if (productInfo.getProductStatus() == 1) {
                vo.setProductStatus("上架");
            } else if (productInfo.getProductStatus() == 0) {
                vo.setProductStatus("下架");
            }
            vo.setCategoryName(this.categoryMapper.getNameByType(productInfo.getCategoryType()));
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public List<ProductInfo> excelToProductInfoList(InputStream inputStream) {
        try {
            List<ProductInfo> list = new ArrayList<>();
            EasyExcel.read(inputStream)
                    .head(ProductExcelVO.class)
                    .sheet()
                    .registerReadListener(new AnalysisEventListener<ProductExcelVO>() {

                        @Override
                        public void invoke(ProductExcelVO excelData, AnalysisContext analysisContext) {
                            ProductInfo productInfo = new ProductInfo();
                            BeanUtils.copyProperties(excelData, productInfo);
                            if(excelData.getProductStatus().equals("上架")){
                                productInfo.setProductStatus(1);
                            }else{
                                productInfo.setProductStatus(0);
                            }
                            list.add(productInfo);
                        }

                        @Override
                        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                        }
                    }).doRead();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
