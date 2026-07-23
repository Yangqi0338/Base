package com.newzkl.platform.base.biz.goods.rpc.model.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/12/1919:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundPassEvent implements Serializable {
    /**
     * 主键
     */
    private Long id;
    /**
     * 订单ID
     */
    private Long orderId;
    /**
     * SPU订单ID
     */
    private Long spuOrderId;
    /**
     * 渠道商ID
     */
    private Long channelId;
    /**
     * 供应商ID
     */
    private Long supplierId;
    /**
     * (0,"待渠道商审核"),(2,"待供应商审核"),(4,"待提交物流"),(6,"待确认收货"),(7,"待平台介入"),(8,"平台介入中"),(9,"退款中"),(10,"已完成"),(-2,"已拒绝"),(-4,"已关闭"),
     */
    private Integer refundState;
    /**
     * 售后类型 0仅退款 1退货退款
     */
    private Integer refundType;
    /**
     * 售后运费金额
     */
    private Integer freightAmount;
    /**
     * 售后金额
     */
    private Integer refundAmount;
    /**
     * 货款金额
     */
    private Integer supplierAmount;
    /**
     * 选品金额
     */
    private Integer goodsAmount;
    /**
     * 铺货金额
     */
    private Integer storeAmount;
    /**
     * 售后原因
     */
    private String reason;
    /**
     * 申请说明
     */
    private String remark;
    /**
     * 申请图片
     */
    private String images;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 物流公司名称
     */
    private String freightCompanyName;
    /**
     * 物流单号
     */
    private String freightNo;
    /**
     * 收货状态
     */
    private Integer takeDeliveryState;
    /**
     * 退款状态
     */
    private Integer payState;
    /**
     * 审核完成时间
     */
    private LocalDateTime auditTime;
    /**
     * 售后完成时间
     */
    private LocalDateTime refundTime;
    /**
     * 来源状态
     */
    private Integer fromState;
    /**
     * 审核日志
     */
    private String auditLog;
    /**
     * 售后流转状态
     */
    private String refundStateLog;
    /**
     * 售后明细
     */
    private List<Item> item;

    @Data
    public static class Item implements Serializable {
        /**
         * sku订单ID
         */
        private Long skuOrderId;
        private String spuImg;
        private String spuName;
        private String skuSaleAttribute;
        /**
         * spuId
         */
        private Long spuId;
        /**
         * skuId
         */
        private Long skuId;
        /**
         * 退款数量
         */
        private Integer count;
        /**
         * 购买数量
         */
        private Integer orderCount;
        /**
         * 已售后数量
         */
        private Integer refundedCount;
        /**
         * 售后金额
         */
        private Integer refundAmount;
    }
}
