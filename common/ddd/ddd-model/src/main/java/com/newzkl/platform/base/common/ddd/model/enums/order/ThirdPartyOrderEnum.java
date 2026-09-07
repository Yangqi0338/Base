package com.newzkl.platform.base.common.ddd.model.enums.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @Description: 订单相关枚举
 * @Author: niu
 * @Date: 2023/4/27 17:01
 */
public class ThirdPartyOrderEnum {


    /**
     * 平台类型枚举(三方平台)
     */
    @Getter
    @AllArgsConstructor
    public enum PlatformTypeEnum {
        /**
         * 会订货
         */
        HUI_DING_HUO("HUI_DING_HUO", "会订货"),
        /** 乐态 */
        LE_TAI("LE_TAI", "乐态"),
        ;

        private final String code;

        private final String description;

        public static PlatformTypeEnum getByCode(String code) {
            for (PlatformTypeEnum type : PlatformTypeEnum.values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("未知的平台类型: " + code);
        }
    }

    /**
     * 三方订单状态与我方订单状态的映射枚举
     */
    public enum ThirdPartyStateMapping {

        // 三方待付款 -> 我方C端待付款（若需对应其他待付款类型，可修改为CHANNEL_WAIT_PAY或OPERATOR_WAIT_PAY的code）
        WAIT_PAY(1, OrderEnum.State.MEMBER_WAIT_PAY),
        // 三方待发货 -> 我方待发货
        WAIT_DELIVERY(100, OrderEnum.State.WAIT_DELIVERY),
        // 三方待收货 -> 我方待收货
        WAIT_RECEIVE(200, OrderEnum.State.WAIT_RECEIVE),
        // 三方已收货 -> 我方已收货
        RECEIVED(300, OrderEnum.State.DOWN_RECEIVE),
        // 三方已退款 -> 我方已关闭（退款后订单通常处于关闭状态，若有特殊需求可调整）
        REFUNDED(500, OrderEnum.State.CLOSE),
        // 三方已关闭 -> 我方已关闭
        CLOSED(700, OrderEnum.State.CLOSE);

        private final Integer thirdPartyCode;  // 三方状态code
        private final OrderEnum.State ourCode;         // 我方对应状态code

        ThirdPartyStateMapping(Integer thirdPartyCode, OrderEnum.State ourCode) {
            this.thirdPartyCode = thirdPartyCode;
            this.ourCode = ourCode;
        }

        /**
         * 通过三方状态code获取我方状态code
         * @param thirdPartyCode 三方状态code
         * @return 我方状态code（无匹配时返回null）
         */
        public static OrderEnum.State getOurCodeByThirdPartyCode(Integer thirdPartyCode) {
            return Stream.of(values())
                    .filter(mapping -> mapping.thirdPartyCode.equals(thirdPartyCode))
                    .findFirst()
                    .map(mapping -> mapping.ourCode)
                    .orElse(null);
        }
    }

    /**
     * 三方动作名 与 third_party_order_record.interface_name 列取值一一对应
     */
    public interface Action {

        /** 下单 */
        String CREATE = "create";

        /** 补偿重推 */
        String COMPENSATION = "compensation";

        /** 开发者回调通知 */
        String NOTIFY = "notify";
    }
}
