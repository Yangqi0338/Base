package com.newzkl.platform.base.common.ddd.model.enums;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * 角色枚举（共享内核）。
 *
 * <p>身份/账号域概念，原分散于各 biz-*-model 中的 7 份副本已在此统一。
 * 位于 common/ddd-model 共享内核，所有 biz-*-model 均可直接引用。</p>
 *
 * @author KC
 */
public class RoleEnum {

    /**
     * 公司角色枚举。
     */
    @Getter
    @AllArgsConstructor
    public enum CompanyRole {
        /**
         * 平台管理员
         */
        PLATFORM(1L, "平台管理员", CommonEnum.Client.ADMIN),
        /** 平台员工 */
        EMP(2L, "平台员工", CommonEnum.Client.ADMIN),
        /** C端客户 */
        MEMBER(1000L, "C端客户", CommonEnum.Client.USER),
        /** 供应商 */
        SUPPLIER(1001L, "供应商", CommonEnum.Client.SUPPLIER),
        /** 渠道商 */
        CHANNEL(1002L, "渠道商", CommonEnum.Client.CHANNEL),
        /** 合伙人 */
        PARTNER(1003L, "合伙人", CommonEnum.Client.CHANNEL),
        /** 商户 */
        MERCHANT(1007L, "商户", CommonEnum.Client.CHANNEL),
        /** 运营商 */
        OPERATOR(1004L, "运营商", CommonEnum.Client.OPERATOR, 3),
        /** 交易师 */
        DEALER(1005L, "交易师", CommonEnum.Client.OPERATOR, 2),
        /** 甄选师 */
        SELECTOR(1006L, "甄选师", CommonEnum.Client.OPERATOR, 1),
        /** 游客 */
        OPERATOR_GUEST(-1L, "游客", CommonEnum.Client.OPERATOR, 0),

        ;

        @JsonValue
        @EnumValue
        private final Long code;
        private final String value;
        /** 所属端 */
        private final CommonEnum.Client client;
        /** 角色级别 */
        private final Integer level;

        CompanyRole(Long code, String value, CommonEnum.Client client) {
            this.code = code;
            this.value = value;
            this.client = client;
            this.level = 0;
        }

        /**
         * 根据编码获取枚举。
         *
         * @param code 编码
         * @return 匹配的枚举，未匹配返回 null
         */
        public static CompanyRole getByCode(Long code) {
            if (code == null) {
                return null;
            }
            return Stream.of(CompanyRole.values())
                    .filter(extension -> extension.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }
