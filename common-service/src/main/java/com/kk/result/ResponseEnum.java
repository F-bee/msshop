package com.kk.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseEnum {

    PRODUCT_STOCK_ERROR(300,"商品库存不足"),
    CREATE_ORDER_ERROR(301,"创建订单失败"),
    ORDER_CANCEL_ERROR(302, "订单状态异常，无法取消"),
    ORDER_FINISH_ERROR(303, "订单状态异常，无法完成"),
    PAY_ERROR(304, "订单状态异常，无法支付"),
    PAYED_ERROR(305, "订单已支付，无需重复操作"),
    USER_INFO_NULL(306, "用户数据为空"),
    MOBILE_ERROR(307, "手机号格式错误"),
    USER_EXIST(308, "用户已存在"),
    MOBILE_NOT_EXIST(309, "用户不存在"),
    USER_PASSWORD_ERROR(310, "密码错误"),
    TOKEN_ERROR(311, "用户登录信息失效"),
    MOBILE_NULL(312, "手机号不能为空"),
    SMS_SEND_ERROR(313, "短信发送失败"),
    PRODUCT_DELETE_FAIL(314, "商品删除失败"),
    ADMIN_INFO_NULL(315, "管理员数据为空"),
    ADMIN_USERNAME_NULL(316, "管理员用户名不存在"),
    USER_CODE_ERROR(317, "验证码错误");

    private Integer code;
    private String msg;
}
