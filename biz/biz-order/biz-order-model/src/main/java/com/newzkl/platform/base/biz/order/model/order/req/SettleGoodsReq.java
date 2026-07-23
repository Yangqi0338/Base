package com.newzkl.platform.base.biz.order.model.order.req;

import java.time.LocalDateTime;

import lombok.Data;

/**
* 结算商品信息表
* @author fang
*/
@Data
public class SettleGoodsReq {
    /**
     * ID
     */
    private Long id;
    /**
     * 供应商ID
     */
    private Long supplierId;
    /**
     * SPU_ID
     */
    private Long spuId;
    /**
     * 下次结算时间
     */
    private LocalDateTime nextSettleTime;
}
