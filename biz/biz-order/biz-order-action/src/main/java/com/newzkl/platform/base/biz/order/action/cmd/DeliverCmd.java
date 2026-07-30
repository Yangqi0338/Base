package com.newzkl.platform.base.biz.order.action.cmd;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 发货域 controller 入参命令集
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.interfaces.controller.DeliverCmd} 与
 * {@code com.zkl.scm.sale.domain.order.model.req.DeliverCommand} /
 * {@code DeliverCodeCommand} / {@code DeliverItemCommand}。字段名与 Java 类型均逐字沿用旧定义,
 * {@code spuOrderId} 保持 {@code Long} 主键语义 —— 前端传的是 SPU 订单主键, 非业务单号。</p>
 *
 * @author KC
 */
public class DeliverCmd {

    /**
     * SPU 订单发货信息查询入参
     *
     * @author KC
     */
    @Data
    public static class OrderDeliverInfoReq implements Serializable {

        /**
         * SPU订单ID (主键)
         */
        private Long spuOrderId;
    }

    /**
     * 发货创建入参 (旧 {@code DeliverCommand})
     *
     * @author KC
     */
    @Data
    public static class Deliver implements Serializable {

        /**
         * 主键
         */
        private Long id;

        /**
         * SPU订单ID (主键)
         */
        @NotNull
        private Long spuOrderId;

        /**
         * 物流公司名称
         */
        @NotNull
        private String expressCompanyName;

        /**
         * 物流单号
         */
        @NotNull
        private String expressNo;

        /**
         * 发货手机号: 顺丰快递需填写
         */
        private String expressMobile;

        /**
         * 发货明细: 不传表示整单发货
         */
        private List<DeliverItem> deliverItemCommandList;
    }

    /**
     * 发货明细项 (旧 {@code DeliverItemCommand})
     *
     * @author KC
     */
    @Data
    public static class DeliverItem implements Serializable {

        /**
         * SKU_ID
         */
        private Long skuId;

        /**
         * 发货数量
         */
        private Integer count;
    }

    /**
     * 物流单号修改入参 (旧 {@code DeliverCodeCommand})
     *
     * @author KC
     */
    @Data
    public static class DeliverCode implements Serializable {

        /**
         * 发货单主键
         */
        private Long id;

        /**
         * 物流公司名称
         */
        @NotNull
        private String expressCompanyName;

        /**
         * 物流单号
         */
        @NotNull
        private String expressNo;

        /**
         * 发货手机号: 顺丰快递需填写
         */
        private String expressMobile;
    }
}
