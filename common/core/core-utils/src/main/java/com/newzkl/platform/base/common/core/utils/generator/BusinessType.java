package com.newzkl.platform.base.common.core.utils.generator;

import com.newzkl.platform.base.common.core.utils.generator.random.AvatarGenerator;
import com.newzkl.platform.base.common.core.utils.generator.random.UserNameGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务类型枚举
 *
 * @author fang
 */
@AllArgsConstructor
@Getter
public enum BusinessType {
    /* 门店 */
    STORE_SPECIAL_ZONE("STSZ", "门店专区"),
    STORE_STYLE("STS", "门店样式"),
    /* 席位 */
    SEAT_PACKAGE("SEP", "席位套餐"),
    /* 订单 */
    ORDER("O", "普通订单", new SnowflakeGenerator()),
    ORDER_SPU("OSP", "普通SPU订单", new SnowflakeGenerator()),
    ORDER_SKU("OSK", "普通SKU订单", new SnowflakeGenerator()),
    ORDER_RECHARGE("OR", "充值订单", new SnowflakeGenerator()),
    ORDER_SEAT_PACKAGE("OSEP", "席位订单", new SnowflakeGenerator()),
    PAYMENT("P", "交易订单", new SnowflakeGenerator()),
    ORDER_REFUND_RETURN("ORR", "售后订单", new SnowflakeGenerator()),
    ORDER_DELIVERY("OD", "发货单", new SnowflakeGenerator()),
    /* 课程 */
    COURSE_CATEGORY("CC", "课程分类"),
    COURSE("C", "课程"),
    /* 任务 */
    TASK_CODE("T", "任务码"),
    /* 用户 */
    USER_DEFAULT_AVATAR("", "默认头像", new AvatarGenerator()),
    USER_DEFAULT_NAME("", "默认用户名", new UserNameGenerator()),
    IM_USER_ACCOUNT("C", "腾讯IM账号", null),
    ;


    private final String prefix;
    private final String desc;
    private final Generator generator;
    /**
     * 是否走 id_generator 表 DB 号段发号
     */
    private final boolean dbSegment;

    BusinessType(String prefix, String desc) {
        this(prefix, desc, new DateTimeGenerator(), false);
    }

    BusinessType(String prefix, String desc, Generator generator) {
        this(prefix, desc, generator, false);
    }
}
