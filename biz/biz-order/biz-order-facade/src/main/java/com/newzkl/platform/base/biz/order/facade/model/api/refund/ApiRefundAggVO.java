package com.newzkl.platform.base.biz.order.facade.model.api.refund;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
* 售后单详情
* @author fang
*/
@Data
public class ApiRefundAggVO implements Serializable {
    /**
     * 售后单
     */
    private ApiRefundVO refund;
    /**
     * 售后单明细
     */
    private List<ApiRefundItemVO> refundItem;
}
