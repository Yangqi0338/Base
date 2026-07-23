package com.newzkl.platform.base.biz.order.model.order.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单发货主单DTO
 * @author sijiwang
 */
@Data
public class OrderDelivery {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 发货单号
     */
    private String deliveryNo;

    /**
     * 主订单号
     */
    private String orderNo;

    /**
     * SPU订单号
     */
    private String spuOrderNo;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 渠道商ID
     */
    private Long channelId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 发货类型 1-整单 2-拆单 3-补发
     */
    private Integer deliveryType;

    /**
     * 发货状态 1-待发货 2-已发货 3-已签收 4-拒收
     */
    private Integer deliveryStatus;

    /**
     * 总发货数量
     */
    private Integer totalQuantity;

    /**
     * 物流编码
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
     * 物流轨迹
     */
    private String logisticsTrace;

    /**
     * 发货联系电话
     */
    private String expressPhone;

    /**
     * 地址ID
     */
    private Long shipAddressId;

    /**
     * 收货信息快照
     */
    private String receiptInfo;

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
     * 发货时间
     */
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
     * 扩展信息
     */
    private String extendInfo;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 发货明细列表
     */
    private List<OrderDeliveryItem> itemList;
}