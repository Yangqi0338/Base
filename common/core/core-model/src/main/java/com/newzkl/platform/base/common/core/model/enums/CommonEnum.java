package com.newzkl.platform.base.common.core.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * 通用枚举集合: 客户端 / 是否 / 符号 / 请求状态 等平台级通用状态
 *
 * @author fang
 */
public class CommonEnum {

    /**
     * 符号
     */
    @Getter
    @AllArgsConstructor
    public enum Symbol implements IEnum<Integer> {
        /**
         * 负数
         */
        NEGATIVE(-1, "负数"),
        /** 正数 */
        POSITIVE(1, "正数"),
        ;
        @EnumValue
        @JsonValue
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
     * 系统类型
     */
    @Getter
    @AllArgsConstructor
    public enum SystemType implements IEnum<Integer> {
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
     *
     * <p>描述带两套语义: 第 0 位 是/否, 第 1 位 启用/禁用, 按语义位下标取用</p>
     */
    @Getter
    @AllArgsConstructor
    public enum YesOrNo implements IEnum<Integer> {
        /** 是 */
        YES(1, "是|启用"),
        /** 否 */
        NO(0, "否|禁用"),
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
    public enum RequestStatusEnum implements IEnum<Integer> {
        /**
         * 成功
         */
        SUCCESS(1, "成功"),
        /**
         * 失败
         */
        FAILED(0, "失败"),

        ;

        @EnumValue
        @JsonValue
        private final Integer code;
        private final String description;

        public static RequestStatusEnum getByCode(int code) {
            for (RequestStatusEnum status : RequestStatusEnum.values()) {
                if (status.getCode() == code) {
                    return status;
                }
            }
            throw new IllegalArgumentException("未知的请求状态码: " + code);
        }

        /**
         * 获取枚举描述 (IEnum 契约, 委托 description)
         *
         * @return 枚举描述
         */
        @Override
        public String getValue() {
            return description;
        }
    }

    /**
     * 翻译方式
     *
     * <p>决定语义位文案取出后按哪种来源重写文案</p>
     * <p>FIX 走 SysProperties 的静态替换表, 其余为扩展占位, 命中时不重写</p>
     */
    public enum TranslateType {
        /** 静态文案 */
        FIX,
        /** 配置文件 */
        FILE,
        /** 数据库字典 */
        DB,
        /** 缓存字典 */
        CACHE,
        ;
    }
}
