package com.newzkl.platform.base.common.core.utils.generator;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务类型枚举。
 *
 * @author fang
 */
@AllArgsConstructor
@Getter
public enum BusinessType {
    STORE_SPECIAL_ZONE("SSZ", "门店专区"),
    SEAT_PACKAGE("SP", "席位套餐"),
    STORE_STYLE("SS", "门店样式"),
    RECHARGE_ORDER("RO", "充值订单"),
    SEAT_PACKAGE_ORDER("SPO", "席位订单"),
    PAYMENT("71", "交易订单", new SnowflakeIdAble()),
    ORDER("D", "普通订单", new SnowflakeIdAble()),
    ORDER_SPU("DP", "普通SPU订单", new SnowflakeIdAble()),
    ORDER_SKU("DK", "普通SKU订单", new SnowflakeIdAble()),
    REFUND_RETURN_ORDER("R", "售后订单", new SnowflakeIdAble()),
    ORDER_DELIVERY("OD", "发货单"),
    COURSE_CATEGORY("KF", "课程分类"),
    COURSE("K", "课程"),

    DEFAULT_AVATAR("", "默认头像", new AvatarGenerator()),
    DEFAULT_USER_NAME("", "默认用户名", new UserNameGenerator()),
    IM_USER_ACCOUNT("C", "腾讯IM账号", null),
    ;


    private final String prefix;
    private final String desc;
    private final Generator generator;

    BusinessType(String prefix, String desc) {
        this.prefix = prefix;
        this.desc = desc;
        this.generator = new DateTimeIdAble();
    }
}
