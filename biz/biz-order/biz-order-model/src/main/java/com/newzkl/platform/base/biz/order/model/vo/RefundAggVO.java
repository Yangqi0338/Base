package com.newzkl.platform.base.biz.order.model.vo;


import lombok.Data;

import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/1/1916:31
 */
@Data
public class RefundAggVO {
    /**
     * 售后单
     */
    private RefundVO refund;
    /**
     * 售后单明细
     */
    private List<RefundItemVO> refundItemList;
}
