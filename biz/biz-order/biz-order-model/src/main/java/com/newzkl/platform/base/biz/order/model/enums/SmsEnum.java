package com.newzkl.platform.base.biz.order.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @author fang
 */
public class SmsEnum {

    @Getter
    @AllArgsConstructor
    public enum Type {
        /**
         * 登录验证码
         */
        Login("1001", "登录验证码", "94792ac718374b54a949a5b6c0ec8316", 1),
        /** 用户修改密码 */
        UpdatePassword("1002", "用户修改密码", "dd7c973d105e494ab20ae4a421cb3bdb", 1),
        /** 注册验证码 */
        Register("1003", "注册验证码", "55cc24df986b4371b1a3251f6d796657", 1),
        /** 用户修改绑定手机号 */
        UpdateUsername("1004", "用户修改绑定手机号", "c86432138a1849afa4980aa6757388a5", 1),
        /** 供应商用户信息审核失败 */
        FormAuditFail("1005", "供应商用户信息审核失败", "0ae30683cb3c4a35a9201e3156b6cbeb", 2),
        /** 供应商信息审核成功 */
        SupplierAuditSuccess("1006", "供应商信息审核成功", "fb97227eccd24c609b7d982f290ee12c", 2),
        /** 注销用户验证码 */
        DESTROY_USER("1008", "注销用户验证码", "5e2c4fe95b6644758850b39fbaa58498", 1),
        /** 注销账号通知 */
        DESTROY_USER_EVENT("1009", "注销账号通知", "1bb9d1c9245f4bf9b2633de1a1e4fd15", 2),
        /** 运营商注册成功通知 */
        OPERATOR_REGISTER("1010", "运营商注册成功通知", "a088884c99b043bda077e25274d5b807", 2),
        /** 供应商注册通知 */
        SUPPLIER_REGISTER("1011", "供应商注册通知", "6a8a7bcbb64a41e2b977632efb6314a3", 2),
        /** 注册成功后通知 */
        ROLE_REGISTER("1012", "注册成功后通知", "46b00fbda52b4ac398d5546e8ee93521", 2),
        /** 保证金审核通过 */
        PROMISE_SUCCESS("1013", "保证金审核通过", "61d77c1b8f54405a9452e890c24b6387", 2),
        /** 九赋渠道商注册通知 */
        CHANNEL_SUCCESS("1014", "九赋渠道商注册通知", "a9f82e1e4277423992a4f914448f1e19", 2),
        /** 供应商入驻通知_延迟 */
        Supplier_In_Delay("1015", "供应商入驻通知_延迟", "57d7ce632d0045c39bedc1a20a25eb3e", 2),
        /** 九赋授权验证码 */
        AUTH("1016", "九赋授权验证码", "118494b6afdf44b98245d0c75fe9acfe", 1),
        /** 通知供应商添加售后退货地址 */
        SUPPLIER_REFUND_ADDRESS("1017", "通知供应商添加售后退货地址", "496b7c8b13354d9c98e97ab75d91db4d", 2),
        /** IM登录验证码 */
        IM_LOGIN("1018", "3", "70365866", 1),
        /** IM注册验证码 */
        IM_REGISTER("1019", "3", "70365866", 1),
        /** IM修改密码验证码 */
        IM_UPDATE_PASSWORD("1020", "3", "", 1),
        /**
         * IM注销验证码
         */
        IM_DEL("1021", "3", "70389217", 1),
        SUPPLIER_REGISTER_CODE("2000", "3", "70315215", 1),
        SUPPLIER_AUDIT_FAIL("2001", "3", "70315242", 1),
        SUPPLIER_AUDIT_SUCCESS("2002", "3", "70315244", 1),
        /** 换绑手机号 */
        ALTER_PHONE("2004", "3", "70320226", 1),
        ALTER_PASSWORD("2005", "3", "70320255", 1),
        CHANNEL_REGISTER("2006", "3", "70332161", 1);

        @EnumValue
        @JsonValue
        private final String code;
        private final String value;
        private final String templateId;
        /**
         * 1 验证码 2 通知
         */
        private final Integer signType;

        public static SmsEnum.Type getByCode(String code) {
            return Stream.of(SmsEnum.Type.values())
                    .filter(extension -> extension.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }
    }
}
