package com.newzkl.platform.base.biz.order.model.enums.finance;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.stream.Stream;

/**
 * 支付记录
 */
@Data
public class PaymentEnum implements Serializable {

    /**
     * 支付状态
     */
    @Getter
    @AllArgsConstructor
    public enum PayState {
        PENDING(0,"待支付"),
        SUCCESS(1,"支付成功"),
        FAILED(2,"支付失败"),
        ;

        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;

        public static PayState getByType(Integer code) {
            return Stream.of(PayState.values())
                    .filter(extension -> extension.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }
    }

    /**
     * 保证金支付类型
     */
    @Getter
    @AllArgsConstructor
    public enum PromisePayType {
        PENDING(0, "待支付"),
        SUCCESS(1, "支付成功"),
        FAILED(2, "支付失败"),
        ;

        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;

        public static PayState getByType(Integer code) {
            return Stream.of(PayState.values())
                    .filter(extension -> extension.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }
    }
}
