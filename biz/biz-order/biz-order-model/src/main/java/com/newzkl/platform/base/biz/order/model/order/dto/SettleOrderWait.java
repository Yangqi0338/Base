package com.newzkl.platform.base.biz.order.model.order.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 待结算订单DTO
 * @author sijiwang
 */
@Data
public class SettleOrderWait {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * SPU订单号
     */
    private String spuOrderNo;

    /**
     * SKU订单号
     */
    private String skuOrderNo;

    /**
     * 类型 0-商品 1-运费
     */
    private Integer type;

    /**
     * 结算金额
     */
    private Integer orderMoney;

    /**
     * SPU ID
     */
    private Long spuId;

    /**
     * SKU ID
     */
    private Long skuId;

    /**
     * 商品数量
     */
    private Integer skuCount;

    /**
     * 结算状态
     */
    private Integer settleState;

    /**
     * 结算时间
     */
    private LocalDateTime settleTime;

    /**
     * 退款状态
     */
    private Integer refundState;

    /**
     * 退款ID
     */
    private Long refundId;

    /**
     * 结算记录ID
     */
    private Long settleRecordId;

    /**
     * 结算时间节点
     */
    private Long settleTimeNode;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}