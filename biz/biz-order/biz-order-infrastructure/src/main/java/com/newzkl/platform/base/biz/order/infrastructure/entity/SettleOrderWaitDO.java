package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.model.entity.BaseDO;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 待结算订单信息表
 * @author sijiwang
 */
@Data
@TableName("settle_order_wait")
public class SettleOrderWaitDO extends BaseDO {

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
     * 结算类型 0-商品 1-运费
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
     * SKU数量
     */
    private Integer skuCount;

    /**
     * 结算状态 0-待结算 1-已结算
     */
    private Integer settleState;

    /**
     * 结算时间
     */
    private LocalDateTime settleTime;

    /**
     * 退款状态 0-未退款 1-已退款
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
}