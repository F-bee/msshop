package com.kk.controller;

import com.kk.exception.Assert;
import com.kk.result.ResponseEnum;
import com.kk.service.SmsService;
import com.kk.util.RandomUtil;
import com.kk.util.RegexValidateUtil;
import com.kk.util.ResultVOUtil;
import com.kk.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/send/{mobile}")
    public ResultVO send(@PathVariable("mobile") String mobile) {
        // 验证手机号不为空
        Assert.notNull(mobile, ResponseEnum.MOBILE_NULL);
        // 验证手机号的格式
        Assert.isTrue(RegexValidateUtil.checkMobile(mobile), ResponseEnum.MOBILE_ERROR);
        // 发送短信
        String code = RandomUtil.getSixBitRandom();
        boolean send = this.smsService.send(mobile, code);
        if (send) {
            this.redisTemplate.opsForValue().set("msshop-sms-code-" + mobile, code);
            return ResultVOUtil.success("短信发送成功");
        }
        return ResultVOUtil.fail("短信发送失败");
    }
}
