package com.newzkl.platform.base.biz.order.model.order.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 订单发货创建请求
 * @author sijiwang
 */
@Data
public class OrderDeliveryCreateReq {

    /**
     * 主订单号
     */
    @NotBlank(message = "订单号不能为空")
    private String orderNo;

    /**
     * 物流公司编码
     */
    private String logisticsCode;

    /**
     * 物流公司名称
     */
    private String logisticsName;

    /**
     * 物流单号
     */
    private String logisticsNo;

    /**
     * 发货/收货联系电话
     */
    private String expressPhone;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 操作人角色
     */
    private Integer operatorRole;

    /**
     * 操作人姓名
     */
    private String operatorName;

    /**
     * 发货备注
     */
    private String remark;

    /**
     * 发货明细（为空则整单发货）
     */
    private List<DeliveryItemReq> items;

    /**
     * 发货明细项
     */
    @Data
    public static class DeliveryItemReq {

        /**
         * SKU订单号
         */
        @NotBlank(message = "skuOrderNo不能为空")
        private String skuOrderNo;

        /**
         * 发货数量
         */
        private Integer deliveryQuantity;
    }
}