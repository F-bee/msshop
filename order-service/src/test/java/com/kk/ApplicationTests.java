package com.kk;

import com.kk.mapper.OrderDetailMapper;
import com.kk.vo.BarModel;
import com.kk.vo.LineModel;
import com.kk.vo.LineModel2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class ApplicationTests {

    @Resource
    private OrderDetailMapper orderDetailMapper;

    @Test
    void Test() {
        List<BarModel> barModels = this.orderDetailMapper.barData();
        System.out.println(1);
    }

    @Test
    void Test2() {
        List<LineModel> lineModels = this.orderDetailMapper.lineData();
        System.out.println(1);
    }

    @Test
    void Test3() {
        List<LineModel2> lineModel2s = this.orderDetailMapper.lineData2("生鲜大虾");
        System.out.println(1);
    }
}
