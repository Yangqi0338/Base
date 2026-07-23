package com.newzkl.platform.base.biz.store.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @author fang
 */
public class DictEnum {

    public static final String USER_DEFAULT_PASSWORD = "123456";
    @Getter
    @AllArgsConstructor
    public enum Key {
        YQM(1001L,6, "对公转账配置"),
        PAY_IMG(1002L,null, "支付图片"),
        PAGE_UI_CONFIG(1003L,null, "页面名称背景图配置"),
        SELECTOR_DATA_CONFIG(1004L,null, "甄选师首页假数据"),
        CHANNEL_SERVICE_FEE(1005L, null, "全局渠道商服务费"),
        SYNC_OUT_USER_TIME(1006L,null, "同步九赋用户时间戳"),
        ORDER_CONFIG(1010L,null, "订单配置"),
        APP_CONFIG(1011L,null, "应用配置"),
        CHANNEL_CONFIG(1012L,null, "数智门店设置"),
        OPERATOR_CONFIG(1013L, null, "供应商配置"),
        ;
        private Long code;
        private Integer length;
        private String value;

        public static DictEnum.Key getByCode(Long code) {
            return Stream.of(DictEnum.Key.values())
                    .filter(extension -> extension.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }
    }
}
