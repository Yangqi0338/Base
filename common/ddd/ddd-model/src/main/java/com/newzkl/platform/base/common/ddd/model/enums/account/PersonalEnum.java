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
public class PersonalEnum {

    /**
     * 性别
     */
    @Getter
    @AllArgsConstructor
    public enum Gender implements IEnum<Integer> {
        /**
         * 男
         */
        MALE(0, "男"),
        /**
         * 女
         */
        FEMALE(1, "女"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;
    }
}
