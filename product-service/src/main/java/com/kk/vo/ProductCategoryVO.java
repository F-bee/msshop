package com.kk.vo;

import lombok.Data;

import java.util.List;

@Data
public class ProductCategoryVO {
    private String name;
    private Integer type;
    private List<ProductInfoVO> goods;
}
