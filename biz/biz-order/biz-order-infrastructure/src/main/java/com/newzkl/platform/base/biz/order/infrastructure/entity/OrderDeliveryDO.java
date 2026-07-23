package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.model.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

import java.time.LocalDateTime;

/**
 * 订单发货主表
 * @author sijiwang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class OrderDeliveryDO extends BaseDO {

    /**
     * 发货单号
     */
    @Index
    private String deliveryNo;

    /**
     * 主订单号
     */
    @Index
    private String orderNo;

    /**
     * SPU订单号
     */
    private String spuOrderNo;

    /**
     * 门店ID
     */
    @Index
    private Long storeId;

    /**
     * 渠道商ID
     */
    private Long channelId;

    /**
     * 用户ID
     */
    @Index
    private Long userId;

    /**
     * 发货类型 1-整单发货 2-拆单发货 3-补发
     */
    private Integer deliveryType;

    /**
     * 发货状态 1-待发货 2-已发货 3-已签收 4-拒收
     */
    @Index
    private Integer deliveryStatus;

    /**
     * 总发货商品数量
     */
    private Integer totalQuantity;

    /**
     * 物流公司编码
     */
    @Index
    private String logisticsCode;

    /**
     * 物流公司名称
     */
    private String logisticsName;

    /**
     * 物流单号
     */
    @Index
    private String logisticsNo;

    /**
     * 物流轨迹JSON
     */
    private String logisticsTrace;

    /**
     * 发货/收货联系电话
     */
    private String expressPhone;

    /**
     * 收货地址ID
     */
    private Long shipAddressId;

    /**
     * 收货信息快照JSON
     */
    private String receiptInfo;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 操作人角色 0-平台 1-供应商 2-渠道商 3-门店
     */
    private Integer operatorRole;

    /**
     * 操作人姓名
     */
    private String operatorName;

    /**
     * 发货时间
     */
    @Index
    private LocalDateTime deliveryTime;

    /**
     * 签收时间
     */
    private LocalDateTime signTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 扩展字段JSON
     */
    private String extendInfo;

}