package com.newzkl.platform.base.biz.account.model.enums;

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
    public enum Gender {
        /**
         * 男
         */
        MALE(0, "男"),
        /**
         * 女
         */
        FEMALE(1, "女"),
        ;
        private final Integer code;
        private final String value;
    }
}
