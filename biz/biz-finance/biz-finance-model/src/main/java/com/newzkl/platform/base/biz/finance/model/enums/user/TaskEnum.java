package com.newzkl.platform.base.biz.finance.model.enums.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 账号个性化
 *
 * @author fang
 */
@Data
public class TaskEnum {

    /**
     * 类型
     */
    @Getter
    @AllArgsConstructor
    public enum Type {
        PEOPLE(0, "邀请人次"),
        VIDEO(1, "观看激励广告"),
        PRODUCT(2, "购买商品"),
        ;
        private final Integer code;
        private final String value;
    }
}
