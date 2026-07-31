package com.newzkl.platform.base.biz.order.model.support.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * 运营商
 *
 * <p>迁移说明: 跨域 ACL 本地副本, 源自 biz-account 的
 * {@code com.newzkl.platform.base.biz.account.model.enums.identity.OperatorEnum},
 * 后续应上移 ddd-model 共享内核(登 deferred)</p>
 *
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
