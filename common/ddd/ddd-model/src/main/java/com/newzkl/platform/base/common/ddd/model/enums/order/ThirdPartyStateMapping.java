package com.newzkl.platform.base.common.ddd.model.enums.order;



import java.util.stream.Stream;

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