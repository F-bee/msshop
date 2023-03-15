package com.kk.vo;

import lombok.Data;

import java.util.List;

@Data
public class LineVO2 {
    private List<String> names;
    private List<String> dates;
    private List<LineInnerVO> datas;
}
