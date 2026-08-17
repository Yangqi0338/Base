package com.newzkl.platform.base.common.ddd.model.enums.order;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Optional;

/**
 * @Description: 订单相关枚举
 * @Author: niu
 * @Date: 2023/4/27 17:01
 */
public class RefundEnum {


    /**
     * 售后操作类型枚举
     * @ext 定义售后流程中所有核心操作类型, 每个类型对应唯一数字 code, 便于数据库存储和业务判断
     *
     * @author sijiwang
     */
    @Getter
    public enum RefundOperateTypeEnum {
        /** 发起售后 */
        LAUNCH_REFUND(1, "发起售后"),
        /** 渠道商同意 */
        CHANNEL_AGREE(2, "渠道商同意"),
        /** 渠道商拒绝 */
        CHANNEL_REFUSE(3, "渠道商拒绝"),
        /** 渠道商撤销售后 */
        CHANNEL_CANCEL_REFUND(4, "渠道商撤销售后"),
        /** 消费者撤销售后 */
        MEMBER_CANCEL_REFUND(5, "消费者撤销售后"),
        /** 渠道商超时自动同意 */
        CHANNEL_TIMEOUT_AGREE(6, "渠道商超时自动同意"),
        /** 买家超时关闭 */
        BUYER_TIMEOUT_CLOSE(7, "买家超时关闭"),
        /** 供应商超时自动同意 */
        SUPPLIER_TIMEOUT_AGREE(8, "供应商超时自动同意"),
        /** 供应商同意 */
        SUPPLIER_AGREE(9, "供应商同意"),
        /** 供应商拒绝 */
        SUPPLIER_REFUSE(10, "供应商拒绝"),
        /** 买家寄回 */
        BUYER_RETURN_GOODS(11, "买家寄回"),
        /** 供应商确认收货 */
        SUPPLIER_CONFIRM_RECEIPT(12, "供应商确认收货"),
        /** 供应商拒绝收货 */
        SUPPLIER_REFUSE_RECEIPT(13, "供应商拒绝收货"),
        /** 退款成功 */
        REFUND_MONEY_SUCCESS(14, "退款成功"),
        /** 超时退款成功 */
        REFUND_MONEY_TIMEOUT_SUCCESS(15, "超时退款成功"),
        /** 申请平台介入 */
        PLATFORM_WAIT(16, "申请平台介入"),
        /** 平台介入中 */
        PLATFORM_ING(17, "平台介入中");

        /**
         * 操作类型编码
         * @ext 落库/接口传输值, MP 按此 code 持久化
         */
        @EnumValue
        @JsonValue
        private final int code;

        /**
         * 操作类型描述
         * @ext 前端展示/日志输出
         */
        private final String desc;

        RefundOperateTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 根据操作类型 code 获取枚举项
         *
         * @param code 操作类型编码
         * @return 对应枚举项, 空则 Optional.empty()
         */
        public static Optional<RefundOperateTypeEnum> getByCode(int code) {
            for (RefundOperateTypeEnum type : values()) {
                if (type.code == code) {
                    return Optional.of(type);
                }
            }
            return Optional.empty();
        }

        /**
         * 根据 code 获取枚举项, 无匹配返回 null
         *
         * @param code 操作类型编码
         * @return 对应枚举项
         */
        public static RefundOperateTypeEnum getByCodeOrNull(int code) {
            return getByCode(code).orElse(null);
        }

        /**
         * 校验 code 是否为有效售后操作类型
         *
         * @param code 操作类型编码
         * @return true=有效, false=无效
         */
        public static boolean isValidCode(int code) {
            return getByCode(code).isPresent();
        }

        /**
         * 根据 code 获取操作类型描述, 无匹配返回默认值
         *
         * @param code        操作类型编码
         * @param defaultDesc 默认描述
         * @return 操作类型描述
         */
        public static String getDescByCode(int code, String defaultDesc) {
            return getByCode(code).map(RefundOperateTypeEnum::getDesc).orElse(defaultDesc);
        }

        /**
         * 根据 code 获取操作类型描述, 无匹配返回空字符串
         *
         * @param code 操作类型编码
         * @return 操作类型描述
         */
        public static String getDescByCode(int code) {
            return getDescByCode(code, "");
        }
    }
}
