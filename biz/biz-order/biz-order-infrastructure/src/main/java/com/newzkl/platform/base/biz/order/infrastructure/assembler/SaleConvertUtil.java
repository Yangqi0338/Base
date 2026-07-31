package com.newzkl.platform.base.biz.order.infrastructure.assembler;


import com.newzkl.platform.base.biz.order.infrastructure.entity.OutOrderDO;
import com.newzkl.platform.base.biz.order.model.dto.OutOrder;
import lombok.Data;

@Data
public class SaleConvertUtil {

    public static OutOrderDO buildOutOrderDO(OutOrder outOrder) {
        OutOrderDO outOrderDO = new OutOrderDO();
        outOrderDO.setId(outOrder.getId());
        outOrderDO.setOrderSn(outOrder.getOrderSn());
        outOrderDO.setOrderId(outOrder.getOrderId());
        outOrderDO.setSkuIds(outOrder.getSkuIds());
        return outOrderDO;
    }
}
