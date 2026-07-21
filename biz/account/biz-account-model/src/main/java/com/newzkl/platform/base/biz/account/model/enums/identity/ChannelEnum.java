package com.newzkl.platform.base.biz.account.model.enums.identity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 渠道商枚举
 */
@Data
public class ChannelEnum {

    /**
     * 性别
     */
    @Getter
    @AllArgsConstructor
    public enum State {
        /**
         * 已注销
         */
        DESTORY(-1, "已注销"),
        /**
         * 申请中
         */
        APPLY(0, "申请中"),
        /**
         * 已开通
         */
        OPEN(1, "已开通"),
        /**
         * 已入驻
         */
        IN(2, "已入驻"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;
    }

    /**
     * 渠道商类型
     */
    @Getter
    @AllArgsConstructor
    public enum ChannelType {
        /**
         * 分销
         */
        DISTRIBUTION(0, "分销"),
        /** 门店 */
        STORE(1, "门店"),
        ;
        private final Integer code;
        private final String value;
    }
}
