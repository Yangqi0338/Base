package com.newzkl.platform.base.common.ddd.model.enums.course;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 课程枚举
 * @author fang
 */
public class CourseEnum {
    /**
     * 课程购买支付状态
     *
     * <p>迁移自源实现散落的 {@code PaymentEnum.PayState} 引用。源用 finance 侧公共枚举,
     * 本域按业务概念内聚, 落 biz-course-model 自持一份, 不跨域引用 finance/account 枚举。</p>
     *
     * @author KC
     */
    @Getter
    @AllArgsConstructor
    public enum PurchasePayStateEnum {

        /**
         * 待支付
         */
        PENDING(0, "待支付"),

        /**
         * 支付成功
         */
        SUCCESS(1, "支付成功"),

        /**
         * 支付失败
         */
        FAIL(2, "支付失败");

        /**
         * 状态码
         */
        private final Integer code;

        /**
         * 状态描述
         */
        private final String desc;

        /**
         * 按状态码取描述
         *
         * @param code 状态码
         * @return 状态描述, 未知返回"未知状态"
         */
        public static String descOf(Integer code) {
            if (code == null) {
                return "未知状态";
            }
            for (PurchasePayStateEnum e : values()) {
                if (e.code.equals(code)) {
                    return e.desc;
                }
            }
            return "未知状态";
        }
    }
}
