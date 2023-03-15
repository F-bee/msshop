package com.kk.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageVO {
    private List content;
    private Long size;
    private Long total;
}
