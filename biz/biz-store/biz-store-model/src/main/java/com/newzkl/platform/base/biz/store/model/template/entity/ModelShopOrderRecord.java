package com.newzkl.platform.base.biz.store.model.template.entity;

import com.newzkl.platform.base.common.ddd.model.enums.ModeShopOrderType;
import com.newzkl.platform.base.common.core.model.dto.Money;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 样板店订单记录实体类
 * 对应数据库表：model_shop_order_record
 */
@Data
public class ModelShopOrderRecord {

    /**
     * 主键
     */
    private Long id;

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 样板店ID
     */
    private Long modelShopId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 订单类型：ORDER 下单，PAY 支付
     *
     * @see ModeShopOrderType
     */
    private String type;

    /**
     * 金额 (Money, 落库 BIGINT 分)
     */
    private Money amount;

}