package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * 外部订单表 DO
 *
 * @author sijiwang
 * @since 2026-07-04
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class OutOrderDO extends BaseDO {

    /**
     * 外部订单号
     */
    @Index
    private String orderSn;

    /**
     * 内部订单ID
     */
    @Index
    private Long orderId;

    /**
     * SKU_ID
     */
    @Index
    private String skuIds;

}