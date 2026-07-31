package com.newzkl.platform.base.biz.order.model.support.api;

import lombok.Data;

import java.io.Serializable;

/**
 * SPU关联的市场VO
 */
@Data
public class SpuRelevancyMarketVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** SPU ID */
    private Long spuId;
    /**
     * 关联的市场数量
     */
    private Integer marketNum;

}
