package com.kk.vo;

import lombok.Data;

import java.util.List;

@Data
public class LineVO {
    private List<String> names;
    private List<Integer> values;
}
