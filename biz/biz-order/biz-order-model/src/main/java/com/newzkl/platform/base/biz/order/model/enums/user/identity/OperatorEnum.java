package com.newzkl.platform.base.biz.order.model.enums.user.identity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @author fang
 */
public class OperatorEnum {

    public static final String TEAM_COUNT = "team_count";
    public static final String TODAY_INVITE = "today_invite";
    public static final String TO_MONTH_INVITE = "to_month_invite";
    public static final String TEAM_SUPPLIER_COUNT = "team_supplier_count";
    public static final String TEAM_SELECTOR_COUNT = "team_selector_count";

    /**
     * 运营商类型
     */
    @Getter
    @AllArgsConstructor
    public enum Type {
        /**
         * 机构
         */
        ORGANIZE(0, "机构"),
        /** 行业 */
        INDUSTRY(1, "行业"),
        /** 区域 */
        AREA(2, "区域"),
        /** 品牌 */
        BRAND(3, "品牌"),
        ;
        private final Integer code;
        private final String value;

        public static Type findByCode(Integer code) {
            return Stream.of(Type.values())
                    .filter(extension -> extension.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }
    }
}

