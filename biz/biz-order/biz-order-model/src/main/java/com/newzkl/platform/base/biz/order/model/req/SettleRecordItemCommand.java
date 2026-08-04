package com.newzkl.platform.base.biz.order.model.req;

import com.newzkl.platform.base.common.core.model.dto.Money;

import lombok.Data;

/**
* 结算记录明细表
* @author fang
*/
@Data
public class SettleRecordItemCommand {
    /**
     * ID
     */
    private Long id;
    /**
     * 结算记录ID
     */
    private Long settleRecordId;
    /**
     * SPU_订单ID
     */
    private Long spuOrderId;
    /**
     * SPU_ID
     */
    private Long spuId;
    /**
     * spu名称
     */
    private String spuName;
    /**
     * spu图片
     */
    private String spuImg;
    /**
     * sku订单结算信息
     */
    private String skuSettleDetail;
    /**
     * 结算金额
     */
    private Money settleMoney;
    /**
     * 结算商品数量
     */
    private Integer settleGoodsNum;
}
