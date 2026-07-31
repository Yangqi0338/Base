package com.newzkl.platform.base.biz.order.model.support.api.order;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: TODO
 * @Author: niu
 * @Date: 2022/4/22 16:59
 */
@Data
public class GoodsVO implements Serializable {

    /**
     * 铺货表idID
     */
    private Long storeDistributionId;
    /**
     * skuId
     */
    private Long skuId;

    /**
     * 购买数量
     */
    private Integer num;
}
