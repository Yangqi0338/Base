package com.newzkl.platform.base.biz.finance.model.enums.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

public class AppOrderEnum {

    @Getter
    @AllArgsConstructor
    public enum Type {
        /**
         * 兑换码订单
         */
        CDK(0, "兑换码订单", 4),
        ;
        private Integer code;
        private String value;
        //消费类型
        private Integer consumeType;

        public static AppOrderEnum.Type getByCode(Integer code) {
            return Stream.of(AppOrderEnum.Type.values())
                    .filter(extension -> extension.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }
    }

    /**
     * 状态
     */
    @Getter
    public enum State {
        /** 待支付 */
        MEMBER_WAIT_PAY(1, "待支付"),
        /** 已完成 */
        SUCCESS(12, "已完成"),
        /** 已关闭 */
        CLOSE(99, "已关闭"),
        ;

        private final Integer code;
        private final String info;

        State(Integer code, String info) {
            this.code = code;
            this.info = info;
        }

        public static AppOrderEnum.State getByCode(Integer code) {
            return Stream.of(AppOrderEnum.State.values())
                    .filter(extension -> extension.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }
    }
}
