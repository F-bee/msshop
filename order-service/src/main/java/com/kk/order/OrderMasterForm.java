package com.kk.order;

import lombok.Data;

import java.util.List;

@Data
public class OrderMasterForm {
    private String name;
    private String phone;
    private String address;
    private Integer id;
    private List<OrderDetailForm> items;
}
