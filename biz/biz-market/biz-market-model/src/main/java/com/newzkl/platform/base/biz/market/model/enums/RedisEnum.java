package com.newzkl.platform.base.biz.market.model.enums;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class RedisEnum {
    public static void main(String[] args) {
        System.out.print(Key.APP_ORDER.getCode("111"));
    }

    /**
     * key
     */
    @Getter
    @AllArgsConstructor
    public enum Key {
        /* ============ 用户 ============ */
        /**
         * 应用订单, s = 应用订单ID
         */
        APP_ORDER(ModuleEnum.USER, "app_order:%s", "应用订单, s = 应用订单ID"),
        /** 父id列表, s = 账号ID */
        PID_LIST(ModuleEnum.USER, "AMC:pidList:%s", "父id列表, s = 账号ID"),
        /** 父角色列表, s = 账号ID */
        PROLE_LIST(ModuleEnum.USER, "AMC:pRoleList:%s", "父角色列表, s = 账号ID"),
        /** 子结构列表, s = 账号ID */
        SUB_STRUCTURE(ModuleEnum.USER, "AMC:subStructure:%s", "子结构列表, s = 账号ID"),

        /* ============ 商品 ============ */
        /** 商品信息, s = SKU ID */
        SKU(ModuleEnum.GOODS, "sku:%s", "商品信息, s = SKU ID"),
        /** 渠道商商品关系, s = 渠道商ID */
        CHANNEL_RELATION(ModuleEnum.GOODS, "relation:%s", "渠道商商品关系, s = 渠道商ID"),
        /** 三方商品销售范围检查结果 */
        OUT_SKU_CHECK(ModuleEnum.GOODS, "out_sku:check:%s", "三方商品销售范围检查结果"),
        /** 运费模板, s = 运费模板ID */
        FREIGHT_TEMPLATE(ModuleEnum.GOODS, "freight_template:%s", "运费模板, s = 运费模板ID"),
        /** 渠道商服务费, s = 渠道商ID */
        FEE_CONFIG(ModuleEnum.GOODS, "order:fee:%s", "渠道商服务费, s = 渠道商ID"),
        /** 渠道商分润配置, s = 渠道商ID */
        SHARE_CONFIG(ModuleEnum.GOODS, "order:share:%s", "渠道商分润配置, s = 渠道商ID"),
        /** 供应商结算节点, s = 供应商ID */
        SETTLE_ORDER_TYPE(ModuleEnum.GOODS, "order:settle_type:%s", "供应商结算节点, s = 供应商ID"),
        /** 支付状态, s1 = 订单类型, s2 = 订单ID */
        PAYMENT_STATE(ModuleEnum.GOODS, "payment:state:%s:%s", "支付状态, s1 = 订单类型, s2 = 订单ID"),

        /* ============ 财务 ============ */
        /** 供应商结算数据 */
        SUPPLIER_SETTLE_DATA_CACHE(ModuleEnum.FINANCE, "SUPPLIER_SETTLE_DATA_CACHE", "供应商结算数据"),
        /** 运营商杠杆等级 */
        CONFIG_CACHE_PREFIX(ModuleEnum.FINANCE, "operatorLever", "运营商杠杆等级"),
        /** 等待分润金额, s = 账号id */
        WAIT_EARNING_AMOUNT(ModuleEnum.FINANCE, "waitEarningAmount:%s", "等待分润金额, s = 账号id"),
        /** 总分润金额 */
        TOTAL_EARNING_AMOUNT(ModuleEnum.FINANCE, "totalEarningAmount", "总分润金额"),

        /* ============ 订单 ============ */
        /** 订单支付锁, s = 订单编号 */
        ORDER_PAY_LOCK_PRE(ModuleEnum.GOODS, "order:orderPayLock:%s", "订单支付锁, s = 订单编号"),
        /** 订单支付信息缓存, s = 订单编号 */
        ORDER_PAY_CACHE_PRE(ModuleEnum.GOODS, "order:totalEarningAmount:%s", "订单支付信息缓存, s = 订单编号"),

        /* ============ 系统 ============ */
        /** 短信分钟发送限制 */
        SMS_SEND_COUNT_MINUTE(ModuleEnum.COMMON, "sms_limit:minute", "短信分钟发送限制"),
        /** 短信小时发送限制 */
        SMS_SEND_COUNT_HOUR(ModuleEnum.COMMON, "sms_limit:hour", "短信小时发送限制"),
        /** SMS */
        SMS(ModuleEnum.COMMON, "SMS:%s", "SMS"),
        EMPTY(ModuleEnum.COMMON, "", ""),
        UN_PERMISSION_KEY_PREFIX(ModuleEnum.COMMON, "gateway:un_permission:", ""),
        INTERFACE_KEY_PREFIX(ModuleEnum.COMMON, "interface:list:", ""),

        ;

        private final ModuleEnum module;
        private final String code;
        private final String value;

        public String getCode(Object... params) {
            return String.format(this.getCode(), params);
        }

        public String getCode() {
            if (module == null || StrUtil.isBlank(module.getCode())) {
                return code;
            }
            return module.getCode() + ":" + code;
        }

        @Override
        public String toString() {
            return getCode();
        }
    }
}
