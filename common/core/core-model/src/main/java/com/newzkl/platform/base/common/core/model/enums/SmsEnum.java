package com.newzkl.platform.base.common.core.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * 短信枚举
 *
 * <p>迁移自 new-scm {@code com.zkl.scm.model.constants.message.SmsEnum},
 * code/templateId/signType 逐字保留 (三方模板号为线上契约, 不可改)。</p>
 *
 * <p>模板号属短信通道技术资产, 故随 core-sms 收拢, 不散落各 biz。</p>
 *
 * @author KC
 */
public class SmsEnum {

    /**
     * 短信类型
     */
    @Getter
    @AllArgsConstructor
    public enum Type implements IEnum<String> {
        /** 登录验证码 */
        Login("1001", "登录验证码", "70597249", 1),
        /** 用户修改密码 */
        UpdatePassword("1002", "用户修改密码", "70597254", 1),
        /** 注册验证码 */
        Register("1003", "注册验证码", "70597249", 1),
        /** 用户修改绑定手机号 */
        UpdateUsername("1004", "用户修改绑定手机号", "70597254", 1),
        /** 注销用户验证码 */
        DESTROY_USER("1008", "注销用户验证码", "70597249", 1),
        /** 注销账号通知 */
        DESTROY_USER_EVENT("1009", "注销账号通知", "1bb9d1c9245f4bf9b2633de1a1e4fd15", 2),
        /** IM注销验证码 */
        IM_DEL("1021", "3", "70389217", 1),
        /** 供应商注册验证码 */
        SUPPLIER_REGISTER_CODE("2000", "3", "70315215", 1),
        /** 通知供应商添加售后退货地址 */
        SUPPLIER_REFUND_ADDRESS("1017","通知供应商添加售后退货地址","496b7c8b13354d9c98e97ab75d91db4d",2),
        ;
        /**
         * 类型编码
         */
        @EnumValue
        @JsonValue
        private final String code;
        /**
         * 类型描述
         */
        private final String value;
        /**
         * 三方短信模板号
         */
        private final String templateId;
        /**
         * 1 验证码 2 通知
         */
        private final Integer signType;

        /**
         * 按编码查类型
         *
         * @param code 类型编码
         * @return 匹配的类型, 无匹配返回 {@code null}
         */
        public static SmsEnum.Type getByCode(String code) {
            return Stream.of(SmsEnum.Type.values())
                    .filter(extension -> extension.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }
    }
}
