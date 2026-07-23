package com.newzkl.platform.base.biz.account.model.enums.finance;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.account.model.enums.identity.RoleEnum;
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
        /** 兑换码 */
        REDEEM_CODE(4, "兑换码"),
        /** 供应商运营账户充值 */
        SUPPLIER_RECHARGE(5, "供应商运营账户充值"),
        /** 分红 */
        DIVIDEND_BONUS(6, "分红"),
        /** 商品席位 */
        GOODS_SEAT(7, "商品席位"),
        /**
         * 课程
         */
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
        /** 甄选师礼包 */
        PICK_PACK_SELECTOR(ConsumeType.PICK_PACK, Type.SELECTOR, PurseEnum.FinanceUser.PICK, "甄选师礼包"),
        /** 交易师礼包 */
        PICK_PACK_TRADE_PERSON(ConsumeType.PICK_PACK, Type.TRADE_PERSON, PurseEnum.FinanceUser.TRADERS, "交易师礼包"),
        /** 运营商礼包 */
        PICK_PACK_OPERATE(ConsumeType.PICK_PACK, Type.OPERATE, PurseEnum.FinanceUser.OPERATOR, "运营商礼包"),
        /** 商品甄选师分润 */
        GOODS_SELECTOR(ConsumeType.GOODS, Type.SELECTOR, PurseEnum.FinanceUser.PICK, "商品甄选师分润"),
        /** 商品交易师分润 */
        GOODS_TRADE_PERSON(ConsumeType.GOODS, Type.TRADE_PERSON, PurseEnum.FinanceUser.TRADERS, "商品交易师分润"),
        /** 商品运营商服务费 */
        GOODS_OPERATE(ConsumeType.GOODS, Type.OPERATE, PurseEnum.FinanceUser.OPERATOR, "商品运营商服务费"),
        /** 奖金池分红 */
        AWARD_DIVIDEND(ConsumeType.DIVIDEND_BONUS, Type.AWARD, null, "奖金池分红"),
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
        /** 甄选师分润 */
        SELECTOR(1, "甄选师分润"),
        /** 交易师分润 */
        TRADE_PERSON(2, "交易师分润"),
        /** 运营商服务费 */
        OPERATE(3, "运营商服务费"),
        /** 分红 */
        AWARD(4, "分红"),
        /** 渠道商分润 */
        CHANNEL(5, "渠道商分润"),
        ;
        @EnumValue
        @JsonValue
        private final Integer type;
        private final String info;
    }
}
