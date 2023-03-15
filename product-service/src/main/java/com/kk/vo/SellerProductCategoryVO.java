package com.kk.vo;

import lombok.Data;

import java.util.List;

@Data
public class SellerProductCategoryVO {
    private String name;
    private Integer type;
    private List goods;
}
