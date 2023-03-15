package com.kk.mapper;

import com.kk.entity.OrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kk.vo.BarModel;
import com.kk.vo.LineModel;
import com.kk.vo.LineModel2;

import java.util.List;

/**
 * <p>
 * 订单详情表 Mapper 接口
 * </p>
 *
 * @author admin
 * @since 2022-10-10
 */
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
    List<BarModel> barData();
    List<LineModel> lineData();
    List<LineModel2> lineData2(String name);
    List<String> getProductNames();
    List<String> getDate();
}
