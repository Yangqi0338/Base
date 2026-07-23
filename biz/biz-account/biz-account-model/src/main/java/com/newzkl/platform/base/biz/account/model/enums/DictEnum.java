package com.newzkl.platform.base.biz.account.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @author fang
 */
public class DictEnum {

    @Getter
    @AllArgsConstructor
    public enum Key {
        /**
         * 全局渠道商服务费
         */
        CHANNEL_SERVICE_FEE(1001L, "全局渠道商服务费"),
        /** 订单配置 */
        ORDER_CONFIG(1002L, "订单配置"),
        /** 数智门店设置 */
        CHANNEL_CONFIG(1003L, "数智门店设置"),
        /** 供应商配置 */
        SUPPLIER_CONFIG(1004L, "供应商配置"),
        /** 运营商提现配置 */
        OPERATOR_WITHDRAW_CONFIG(1005L, "运营商提现配置"),
        /**
         * 运营商配置
         */
        OPERATOR_CONFIG(1006L, "运营商配置"),
        /** app版本配置 */
        APP_VERSION(1007L, "app版本配置"),
        /** 腾信IM配置 */
        TENCENT_IM_CONFIG(1008L, "腾信IM配置"),

        LECTURER_CATEGORY_HEAD(1009L, "讲师分类头像"),

        LECTURER_HEAD(1010L, "讲师分类头像"),
        ;
        private final Long code;
        private final String value;

        public static DictEnum.Key getByCode(Long code) {
            return Stream.of(DictEnum.Key.values())
                    .filter(extension -> extension.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }
    }
}
