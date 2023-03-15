package com.kk.form;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductInfoForm {
    private String productName;
    private BigDecimal productPrice;
    private Integer productStock;
    private String productDescription;
    private String productIcon;
    private Integer categoryType;
    private Integer productStatus;
}
