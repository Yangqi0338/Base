package com.newzkl.platform.base.biz.order.facade.model.settle;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* 结算商品信息表
* @author fang
*/
@Data
public class SettleGoodsRpcCommand implements Serializable {
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
