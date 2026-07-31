package com.newzkl.platform.base.biz.order.infrastructure.entity;


import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;

@Data
public class OutOrderDO  extends BaseDO {

    private String orderSn;

    private Long orderId;

    private String skuIds;
}
