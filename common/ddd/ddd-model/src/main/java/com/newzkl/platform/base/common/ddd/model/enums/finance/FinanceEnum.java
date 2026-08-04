package com.newzkl.platform.base.common.ddd.model.enums.finance;

import cn.hutool.core.lang.Opt;
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
public class FinanceEnum implements Serializable {


    /**
     * 财务用户类型
     */
    @Getter
    @AllArgsConstructor
    public enum FinanceUser {

        /**
         * 供应商
         */
        SUPPLIER(1, 1001L, "供应商"),

        /**
         * 渠道商
         */
        CHANNEL(2, 1002L, "渠道商"),

        /**
         * 甄选师
         */
        PICK(3, 1006L, "甄选师"),

        /**
         * 运营商
         */
        OPERATOR(3, 1004L, "运营商"),

        /**
         * 交易师
         */
        TRADERS(3, 1005L, "交易师"),

        /**
         * 渠道商C端用户
         */
        C_CLIENT(6, 1000L, "C端用户"),
        ;

        private final Integer type;

        private final Long roleId;

        private final String info;

        public static Integer accountTypeByRoleId(Long roleId){
            return Opt.ofNullable(getByRole(roleId))
                    .map(FinanceUser::getType)
                    .orElse(null);
        }

        public static FinanceEnum.FinanceUser getByType(Integer type) {
            return Stream.of(FinanceEnum.FinanceUser.values())
                    .filter(extension -> extension.getType().equals(type))
                    .findFirst()
                    .orElse(null);
        }

        public static FinanceEnum.FinanceUser getByRole(Long roleId) {
            return Arrays.stream(values())
                    .filter(it -> it.getRoleId().equals(roleId))
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
        /* 账户类目不同采用不同的个位, 明细账户设置为不同的十位 */
        TOTAL(0, "总账户"),
        //        INCOME(1, "总收益账户"),
        PACK_INCOME(10, "礼包返利账户"),
        GOODS_INCOME(11, "商品分润账户"),
        AWARD_INCOME(12, "分红奖账户"),
        PURCHASE(2, "采购金账户"),
        LEVERAGE_PURCHASE(20, "杠杆采购金账户"),
        PROMISE(3, "保证金账户"),
        MARKETING(4, "营销账户"),
        GOODS_SEAT(5, "商品位"),
        ;

        private final Integer type;
        private final String info;

        public static FinanceEnum.PurseType findByType(Integer type) {
            return Stream.of(FinanceEnum.PurseType.values())
                    .filter(extension -> extension.getType().equals(type))
                    .findFirst()
                    .orElse(null);
        }
    }

    /**
     * 账户变动记录类型 (实际上是存储到remark)
     */
    @Getter
    public enum PurseAlterType {
        /**
         * 采购金充值
         */
        RECHARGE(3, "采购金充值"),

        /**
         * 转出
         */
        ROLL_OUT(1, "转出"),

        /**
         * 订单支付
         */
        ORDER_PAY(2, "订单支付"),

        /**
         * 售后退款
         */
        SELL_AFTER(4, "售后退款"),

        /**
         * 订单结算
         */
        SUPPLIER_SETTLE(5, "订单结算"),

        /**
         * 分润
         */
        EARNING(6, "分润"),

        /**
         * 平台给运营商分配采购金
         */
        PLATFORM_TO_OPERATOR(7, "平台给运营商分配采购金"),

        /**
         * 平台给运营商分配杠杆采购金
         */
        PLATFORM_TO_OPERATOR_LEVER(8, "平台给运营商分配杠杆采购金"),

        /**
         * 运营商给渠道商分配采购金
         */
        OPERATOR_TO_CHANNEL(9, "运营商给渠道商分配采购金"),

        /**
         * 供应商订单结算补充保证金
         */
        SUPPLIER_SETTLE_SUB_DEPOSIT(10, "供应商订单结算补充保证金"),

        /**
         * 供应商运营账户充值
         */
        SUPPLIER_OPERATOR_RECHARGE(11, "供应商运营账户充值"),

        /**
         * 商品位购买
         */
        GOODS_POSITION_BUY(12, "商品位购买"),

        /**
         * 平台赠送商品位
         */
        PLATFORM_GIFT_GOODS_SEAT(13, "平台赠送商品位"),

        /**
         * 商品审核失败返还商品位
         */
        SUPPLIER_GOODS_POSITION_ADD(14, "商品审核失败或终止审核返还商品位"),

        /**
         * 商品提交平台扣除商品位
         */
        SUPPLIER_GOODS_POSITION_SUB(15, "商品提交平台扣除商品位"),
	    
	    /**
	     * 渠道商下游自同步
	     *
	     */
	    CHANNEL_SYNC_DOWNSTREAM(16, "渠道商下游自同步"),

        BILL_ORDER_AWARD_INCOME(17, "订单流水分红"),

        /**
         * 供应商订单结算补充保证金
         */
        SUPPLIER_PROMISE(18, "保证金充值"),
        /**
         * 渠道商扣除商品位
         */
        CHANNEL_GOODS_POSITION_SUB(19, "渠道商扣除商品位"),
        /**
         * 渠道商返还商品位
         */
        CHANNEL_GOODS_POSITION_ADD(20, "渠道商返还商品位"),
        ;


        private Integer type;
        private String info;

        PurseAlterType(Integer type, String info) {
            this.type = type;
            this.info = info;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
        /**
         * 通过code获取枚举实例
         */
        public static PurseAlterType getByType(Integer type) {
            if (type == null) {
                return null;
            }
            for (PurseAlterType state : PurseAlterType.values()) {
                if (state.getType().equals(type)) {
                    return state;
                }
            }
            return null;
        }
    }

    /**
     * 三方账号平台
     */
    @Getter
    @AllArgsConstructor
    public enum TripartitePurchasePlatform {
        /**
         * 汇付
         */
        HUI_FU("0", "汇付个人"),
        HUI_FU_ENT("1", "汇付企业"),
        ;

        private final String value;
        private final String info;
    }

    /**
     * 购买类型
     */
    @Getter
    @AllArgsConstructor
    public enum PurchaseRecordType {
        /**/
        MERCHANT(0, "门店"),
        SEAT_PACKAGE(1, "席位"),
        ;

        private final Integer code;
        private final String info;
    }

    /**
     * 三方账号状态
     */
    @Getter
    @AllArgsConstructor
    public enum TripartitePurchaseStatus {
        /**
         * 绑卡审核中
         */
        BIND_SYNC("BIND_SYNC", "绑卡审核中"),
        BIND_FAIL("BIND_FAIL", "绑卡失败"),
        NORMAL("NORMAL", "正常"),

        ;

        private final String value;
        private final String desc;
    }

    /**
     * 三方账号请求状态码
     */
    @Getter
    @AllArgsConstructor
    public enum TripartitePurchaseRespCode {

        /**
         * 汇付成功
         */
        HuiFu_SUCCESS("00000000", "成功"),
        /* --- 汇付 --- */;

        private final String value;
        private final String desc;
    }

    /**
     * 三方账号审核状态
     */
    @Getter
    @AllArgsConstructor
    public enum TripartitePurchaseAuditStatus {

        /**
         * 汇付审核通过
         */
        HuiFu_SUCCESS("Y", "审核通过"),
        HuiFu_PROCESS("P", "审核中"),
        HuiFu_FAIL("N", "审核失败"),
        /* --- 汇付 --- */;

        private final String value;
        private final String desc;

        public static TripartitePurchaseAuditStatus findByCode(String code) {
            return Arrays.stream(TripartitePurchaseAuditStatus.values()).filter(it -> it.getValue().equals(code)).findFirst().orElse(null);
        }
    }
}
