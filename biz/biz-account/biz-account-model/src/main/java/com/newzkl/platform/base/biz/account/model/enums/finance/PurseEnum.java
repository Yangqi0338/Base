package com.newzkl.platform.base.biz.account.model.enums.finance;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author niu
 * @description: 财务枚举
 * @date 2023/12/18 10:04
 */
public class PurseEnum implements Serializable {

    /**
     * 财务用户类型
     */
    @Getter
    @AllArgsConstructor
    public enum FinanceUser {
        /**
         * C端用户
         */
        C_CLIENT(0, RoleEnum.CompanyRole.MEMBER),
        /** 供应商 */
        SUPPLIER(1, RoleEnum.CompanyRole.SUPPLIER),
        /** 渠道商 */
        CHANNEL(2, RoleEnum.CompanyRole.CHANNEL),
        /** 运营商 */
        OPERATOR(4, RoleEnum.CompanyRole.OPERATOR),
        /** 交易师 */
        TRADERS(5, RoleEnum.CompanyRole.DEALER),
        /** 甄选师 */
        PICK(6, RoleEnum.CompanyRole.SELECTOR),
        ;

        @EnumValue
        @JsonValue
        private final Integer type;
        private final RoleEnum.CompanyRole role;

        public static FinanceUser getByType(Integer type) {
            return Stream.of(FinanceUser.values())
                    .filter(extension -> extension.getType().equals(type))
                    .findFirst()
                    .orElse(null);
        }

        public static FinanceUser getByRole(RoleEnum.CompanyRole role) {
            return Arrays.stream(values())
                    .filter(it -> it.getRole() == role)
                    .findFirst()
                    .orElse(null);
        }

        public static FinanceUser getByRole(Long roleId) {
            return Arrays.stream(values())
                    .filter(it -> it.getRole().getCode().equals(roleId))
                    .findFirst()
                    .orElse(null);
        }
    }

    /**
     * 钱包类型
     */
    @Getter
    @AllArgsConstructor
    public enum PurseType {
        /** 总账户 */
        TOTAL(0, false, "总账户"),
        /** 礼包返利账户 */
        PACK_INCOME(10, true, "礼包返利账户"),
        /** 商品分润账户 */
        GOODS_INCOME(11, true, "商品分润账户"),
        /** 分红奖账户 */
        AWARD_INCOME(12, true, "分红奖账户"),
        /** 商品货款结余账户 */
        SUPPLIER_INCOME(13, true, true, "商品货款结余账户"),
        /** 采购金账户 */
        PURCHASE(2, true, "采购金账户"),
        /** 杠杆采购金账户 */
        LEVERAGE_PURCHASE(20, false, "杠杆采购金账户"),
        /** 保证金账户 */
        PROMISE(3, true, "保证金账户"),
        /** 营销账户 */
        MARKETING(4, false, "营销账户"),
        /** 商品位 */
        GOODS_SEAT(5, false, "商品位"),
        ;

        @EnumValue
        @JsonValue
        private final Integer type;
        /**
         * 是否计入总账户
         */
        private final boolean totalRelation;
        /** 能否为负数 */
        private final boolean isNegative;
        private final String info;

        public static PurseType findByType(Integer type) {
            return Arrays.stream(values())
                    .filter(extension -> extension.getType().equals(type))
                    .findFirst()
                    .orElse(null);
        }

        PurseType(Integer type, boolean totalRelation, String info) {
            this.type = type;
            this.info = info;
            this.totalRelation = totalRelation;
            this.isNegative = false;
        }
    }

    /**
     * 账户变动记录类型 (实际上是存储到remark)
     */
    @Getter
    @AllArgsConstructor
    public enum PurseAlterType {
        /** 转出 */
        ROLL_OUT(1, "转出"),
        /** 订单支付 */
        ORDER_PAY(2, "订单支付"),
        /** 采购金充值 */
        RECHARGE(3, "采购金充值"),
        /** 售后退款 */
        SELL_AFTER(4, "售后退款"),
        /** 订单结算 */
        SUPPLIER_SETTLE(5, "订单结算"),
        /** 分润 */
        EARNING(6, "分润"),
        /** 平台给运营商分配采购金 */
        PLATFORM_TO_OPERATOR(7, "平台给运营商分配采购金"),
        /** 平台给运营商分配杠杆采购金 */
        PLATFORM_TO_OPERATOR_LEVER(8, "平台给运营商分配杠杆采购金"),
        /** 运营商给渠道商分配采购金 */
        OPERATOR_TO_CHANNEL(9, "运营商给渠道商分配采购金"),
        /** 供应商运营账户充值 */
        SUPPLIER_OPERATOR_RECHARGE(11, "供应商运营账户充值"),
        /** 商品位购买 */
        GOODS_POSITION_BUY(12, "商品位购买"),
        /** 平台赠送商品位 */
        PLATFORM_GIFT_GOODS_SEAT(13, "平台赠送商品位"),
        /** 商品审核失败或终止审核返还商品位 */
        SUPPLIER_GOODS_POSITION_ADD(14, "商品审核失败或终止审核返还商品位"),
        /** 商品提交平台扣除商品位 */
        SUPPLIER_GOODS_POSITION_SUB(15, "商品提交平台扣除商品位"),
        /**
         * 渠道商下游同步
         */
        CHANNEL_SYNC_DOWNSTREAM(16, "渠道商下游同步"),
        /** 订单流水分红 */
        BILL_ORDER_AWARD_INCOME(17, "订单流水分红"),
        /** 保证金充值 */
        SUPPLIER_PROMISE(18, "保证金充值"),
        /**
         * 商品上架扣除商品位
         */
        GOODS_POSITION_SUB(19, "商品上架扣除商品位"),
        /**
         * 商品下架返还商品位
         */
        GOODS_POSITION_ADD(20, "商品下架返还商品位"),
        /** 转出审核拒绝 */
        ROLL_OUT_REFUSE(21, "转出审核拒绝"),
        ;

        @EnumValue
        @JsonValue
        private final Integer type;
        private final String info;

        /**
         * 通过code获取枚举实例
         */
        public static PurseAlterType getByType(Integer type) {
            return Arrays.stream(values())
                    .filter(item -> item.getType().equals(type))
                    .findFirst()
                    .orElse(null);
        }
    }

    /**
     * 三方账号平台
     */
    @Getter
    @AllArgsConstructor
    public enum TripartitePurchasePlatform {
        /** 汇付 */
        HUI_FU("0", "汇付"),

        ;

        @EnumValue
        @JsonValue
        private final String value;
        private final String info;
    }

    /**
     * 购买类型
     */
    @Getter
    @AllArgsConstructor
    public enum PurchaseRecordType {
        /** 门店 */
        MERCHANT(0, "门店"),
        /** 席位 */
        SEAT_PACKAGE(1, "席位"),
        /**
         * 课程
         */
        COURSE(1, "课程"),
        ;

        @EnumValue
        @JsonValue
        private final Integer code;
        private final String info;
    }

    /**
     * 三方账号状态
     */
    @Getter
    @AllArgsConstructor
    public enum TripartitePurchaseStatus {
        /** 绑卡审核中 */
        BIND_SYNC("BIND_SYNC", "绑卡审核中"),
        /** 绑卡失败 */
        BIND_FAIL("BIND_FAIL", "绑卡失败"),
        /** 正常 */
        NORMAL("NORMAL", "正常"),

        ;

        @EnumValue
        @JsonValue
        private final String value;
        private final String desc;
    }

}
