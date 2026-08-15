package com.newzkl.platform.base.common.ddd.model.enums.finance;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author niu
 * @description: 分润枚举
 * @date 2023/12/18 16:59
 */
public class EarningsEnum implements Serializable {

    /**
     * 消费类型
     */
    @AllArgsConstructor
    @Getter
    public enum ConsumeType {
        /**
         * 礼包
         */
        PICK_PACK(1, "礼包"),
        /** 渠道商充值 */
        RECHARGE(2, "渠道商充值"),
        /** 商品 */
        GOODS(3, "商品"),
        /** 供应商运营账户充值 */
        SUPPLIER_RECHARGE(5, "供应商运营账户充值"),
        /** 分红 */
        DIVIDEND_BONUS(6, "分红"),
        /** 商品席位 */
        GOODS_SEAT(7, "商品席位"),
        /** 数智门店 */
        STORE(8, "数智门店"),
        /** 课程 */
        COURSE(9, "课程"),
        ;

        @EnumValue
        @JsonValue
        private final Integer type;
        private final String info;

        public static ConsumeType getByType(Integer consumeType) {
            return Stream.of(ConsumeType.values())
                    .filter(extension -> extension.getType().equals(consumeType))
                    .findFirst()
                    .orElse(null);
        }
    }

    /**
     * 贡献类型
     */
    @AllArgsConstructor
    @Getter
    public enum ContributeType {
        /** 消费 */
        AMOUNT(1, "消费"),
        /** 分润 */
        EARNING(2, "分润"),
        /** 服务费 */
        SERVICE_AMOUNT(3, "服务费"),

        ;

        @EnumValue
        @JsonValue
        private final Integer type;
        private final String info;
    }

    /**
     * 贡献类型
     */
    @AllArgsConstructor
    @Getter
    public enum State {
        /** 待结算 */
        SETTLE(0, "待结算"),
        /** 已结算 */
        FINISH(1, "已结算"),
        /** 已售后 */
        AFTER_SALE(2, "已售后"),
        ;

        @EnumValue
        @JsonValue
        private final Integer type;
        private final String info;
    }

    /**
     * 账户变动类型
     */
    @AllArgsConstructor
    @Getter
    public enum PurseAlterTypeEnum {
        /** 进账 */
        IN(1, "进账", CommonEnum.Symbol.POSITIVE),
        /** 出账 */
        OUT(2, "出账", CommonEnum.Symbol.NEGATIVE),
        ;
        @EnumValue
        @JsonValue
        private final Integer type;
        private final String info;
        private final CommonEnum.Symbol symbol;
    }

    /**
     * 消费类型对应分润得益角色code
     */
    @AllArgsConstructor
    @Getter
    public enum EarningType {
        /** 商品渠道商分润 */
        GOODS_CHANNEL(ConsumeType.GOODS, Type.CHANNEL, PurseEnum.FinanceUser.CHANNEL, "商品渠道商分润"),
        ;

        private final ConsumeType consumeType;
        private final Type type;
        private final PurseEnum.FinanceUser user;
        private final String value;

        public static EarningType getByRole(ConsumeType consumeType, Type type) {
            return Arrays.stream(values())
                    .filter(it -> it.consumeType == consumeType && it.type == type)
                    .findFirst()
                    .orElse(null);
        }

        public static EarningType getByRole(ConsumeType consumeType, RoleEnum.CompanyRole role) {
            return Arrays.stream(values())
                    .filter(it -> it.getConsumeType() == consumeType && (it.getUser() == null || it.getUser().getRole() == role))
                    .findFirst()
                    .orElse(null);
        }

        public static EarningType getByRole(ConsumeType consumeType, PurseEnum.FinanceUser role) {
            return Arrays.stream(values())
                    .filter(it -> it.getConsumeType() == consumeType && it.getUser() == role)
                    .findFirst()
                    .orElse(null);
        }
    }

    @AllArgsConstructor
    @Getter
    public enum Type {
        /** 渠道商分润 */
        CHANNEL(5, "渠道商分润"),
        ;
        @EnumValue
        @JsonValue
        private final Integer type;
        private final String info;
    }
}
