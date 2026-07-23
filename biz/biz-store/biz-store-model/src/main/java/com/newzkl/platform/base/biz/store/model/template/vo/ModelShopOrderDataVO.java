package com.newzkl.platform.base.biz.store.model.template.vo;

import lombok.Data;

/**
 * 样板店订单数据VO
 */
@Data
public class ModelShopOrderDataVO {

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 总支付金额
     */
    private Integer totalPayAmount;

    /**
     * 总支付笔数
     */
    private Integer totalPayNum;

}
