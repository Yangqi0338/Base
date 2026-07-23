package com.newzkl.platform.base.biz.market.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @author fang
 */
public class CommonEnum {

    /**
     * 符号
     */
    @Getter
    @AllArgsConstructor
    public enum Symbol {
        /**
         * 负数
         */
        NEGATIVE(-1, "负数"),
        /** 正数 */
        POSITIVE(1, "正数"),
        ;
        private final Integer code;
        private final String value;

        public static CommonEnum.Symbol getByCode(Integer code) {
            return Stream.of(values())
                    .filter(extension -> extension.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }
    }

    /**
     * 客户端
     */
    @Getter
    @AllArgsConstructor
    public enum Client {
        /** 平台端 */
        ADMIN("admin", "平台端"),
        /** 后台端 */
        USER("user", "用户端"),
        /** 运营商端 */
        OPERATOR("operator", "运营商端"),
        /** 渠道商端 */
        CHANNEL("channel", "渠道商端"),
        /** 供应商端 */
        SUPPLIER("supplier", "供应商端"),
        ;

        @EnumValue
        @JsonValue
        private final String code;
        private final String value;

        public static CommonEnum.Client getByCode(String code) {
            return Stream.of(CommonEnum.Client.values())
                    .filter(extension -> extension.getCode().equalsIgnoreCase(code))
                    .findFirst()
                    .orElse(null);
        }
    }

    /**
     * 兑换码类型
     */
    @Getter
    @AllArgsConstructor
    public enum SystemType {
        /** 数字门店 */
        STORE(0, "数字门店"),
        ;

        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;

        public static CommonEnum.SystemType getByCode(Integer code) {
            return Stream.of(CommonEnum.SystemType.values())
                    .filter(extension -> extension.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }
    }

    /**
     * 是否
     */
    @Getter
    @AllArgsConstructor
    public enum YesOrNo {
        /** 是 */
        YES(1, "是"),
        /** 否 */
        NO(0, "否"),
        ;

        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;

        public static CommonEnum.YesOrNo getByCode(Integer code) {
            return Stream.of(CommonEnum.YesOrNo.values())
                    .filter(extension -> extension.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }

        public static CommonEnum.YesOrNo getByBool(Boolean flag) {
            return flag ? YES : NO;
        }


        public static CommonEnum.YesOrNo reverse(Integer code) {
            YesOrNo aSwitch = getByCode(code);
            if (aSwitch == null) return null;
            return aSwitch == YES ? NO : YES;
        }
    }

    /**
     * 请求状态
     */
    @Getter
    @AllArgsConstructor
    public enum RequestStatusEnum {
        /**
         * 成功
         */
        SUCCESS(1, "成功"),
        /**
         * 失败
         */
        FAILED(0, "失败"),

        ;

        private final int code;
        private final String description;

        public static RequestStatusEnum getByCode(int code) {
            for (RequestStatusEnum status : RequestStatusEnum.values()) {
                if (status.getCode() == code) {
                    return status;
                }
            }
            throw new IllegalArgumentException("未知的请求状态码: " + code);
        }
    }
}
