package com.newzkl.platform.base.biz.order.model.order.util;

/**
 * 订单Redis Key
 * @author sijiwang
 */
public class OrderRedisKeyUtils {

    private static final String PRE_PAY_ORDER_KEY = "prePayOrder:";

    public static final Long TIME_OUT = 60L * 60;

    public static String getPrePayOrderKey(String orderNo,Long userId) {
        return PRE_PAY_ORDER_KEY + orderNo + "-" + userId;
    }
}
