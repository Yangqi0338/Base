package com.newzkl.platform.base.biz.order.model.req;

import com.newzkl.platform.base.common.core.model.money.Money;

import lombok.Data;

import java.time.LocalDateTime;

/**
* 结算记录表
* @author fang
*/
@Data
public class SettleRecordCommand {
    /**
     * ID
     */
    private Long id;
    /**
     * 供应商ID
     */
    private Long supplierId;
    /**
     * 结算时间(版本号)
     */
    private LocalDateTime settleTime;
    /**
     * 结算金额
     */
    private Money settleMoney;
    /**
     * 结算商品数量
     */
    private Integer settleGoodsNum;
}
