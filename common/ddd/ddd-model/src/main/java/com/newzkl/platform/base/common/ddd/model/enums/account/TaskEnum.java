package com.newzkl.platform.base.common.ddd.model.enums.account;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.newzkl.platform.base.common.core.model.enums.IEnum;
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
    public enum Type implements IEnum<Integer> {
        PEOPLE(0, "邀请人次"),
        VIDEO(1, "观看激励广告"),
        PRODUCT(2, "购买商品"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;
    }
}
