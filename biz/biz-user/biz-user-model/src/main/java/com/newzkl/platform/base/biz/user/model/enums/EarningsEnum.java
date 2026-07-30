package com.newzkl.platform.base.biz.user.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.stream.Stream;

/**
 * 分润枚举（迁移精简版：relation 仅用到 {@code ConsumeType}）
 *
 * <p>源 EarningsEnum 依赖 finance 域 PurseEnum（未迁移），此处仅保留 relation 所需的 ConsumeType。
 * 其余枚举（ContributeType/State/EarningType/Type/PurseAlterTypeEnum）随 finance 域迁移。</p>
 *
 * @author niu
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

        /**
         * 根据类型编码获取枚举
         *
         * @param consumeType 类型编码
         * @return 匹配的枚举
         */
        public static ConsumeType getByType(Integer consumeType) {
            return Stream.of(ConsumeType.values())
                    .filter(extension -> extension.getType().equals(consumeType))
                    .findFirst()
                    .orElse(null);
        }
    }
}
