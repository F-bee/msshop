package com.kk.util;

import com.kk.vo.ResultVO;

public class ResultVOUtil {
    public static ResultVO success(Object data) {
        ResultVO resultVO = new ResultVO<>();
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        resultVO.setData(data);
        return resultVO;
    }

    public static ResultVO fail(String msg) {
        ResultVO resultVO = new ResultVO<>();
        resultVO.setCode(-1);
        resultVO.setMsg(msg);
        return resultVO;
    }
}

