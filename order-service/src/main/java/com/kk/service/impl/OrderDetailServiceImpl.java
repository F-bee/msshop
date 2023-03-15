package com.kk.service.impl;

import com.kk.entity.OrderDetail;
import com.kk.mapper.OrderDetailMapper;
import com.kk.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kk.util.EChartsColorUtil;
import com.kk.util.ResultVOUtil;
import com.kk.vo.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单详情表 服务实现类
 * </p>
 *
 * @author admin
 * @since 2022-10-10
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

    @Resource
    private OrderDetailMapper orderDetailMapper;

    @Override
    public ResultVO barData() {
        List<BarModel> barModels = this.orderDetailMapper.barData();
        BarVO barVO = new BarVO();
        List<String> names = new ArrayList<>();
        List<BarInnerVO> values = new ArrayList<>();
        for (BarModel barModel : barModels) {
            names.add(barModel.getName());
            BarInnerVO barInnerVO = new BarInnerVO();
            barInnerVO.setValue(barModel.getValue());
            barInnerVO.setItemStyle(EChartsColorUtil.createItemStyle(barModel.getValue()));
            values.add(barInnerVO);
        }
        barVO.setNames(names);
        barVO.setValues(values);
        return ResultVOUtil.success(barVO);
    }

    @Override
    public ResultVO lineData() {
        List<LineModel> lineModels = this.orderDetailMapper.lineData();
        LineVO lineVO = new LineVO();
        List<String> names = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        for (LineModel lineModel : lineModels) {
            names.add(lineModel.getDate());
            values.add(lineModel.getValue());
        }
        lineVO.setNames(names);
        lineVO.setValues(values);
        return ResultVOUtil.success(lineVO);
    }

    @Override
    public ResultVO lineData2() {
        List<String> names = this.orderDetailMapper.getProductNames();
        List<String> dates = this.orderDetailMapper.getDate();
        List<LineInnerVO> innerVOList = new ArrayList<>();
        LineVO2 vo = new LineVO2();
        for (String name : names) {
            LineInnerVO innerVO = new LineInnerVO();
            innerVO.setName(name);
            List<Integer> data = new ArrayList<>();
            List<LineModel2> list = this.orderDetailMapper.lineData2(name);
            for (LineModel2 lineModel2 : list) {
                data.add(lineModel2.getCount());
            }
            innerVO.setData(data);
            innerVOList.add(innerVO);
        }
        vo.setNames(names);
        vo.setDates(dates);
        vo.setDatas(innerVOList);
        return ResultVOUtil.success(vo);
    }


}
