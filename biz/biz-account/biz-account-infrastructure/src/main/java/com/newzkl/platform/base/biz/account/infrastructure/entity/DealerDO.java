package com.newzkl.platform.base.biz.account.infrastructure.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 交易师
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DealerDO extends OperatorClientBaseDO {
    /**
     * 分润比例
     */
    private Double serviceRate;
    /**
     * 绑定的二级市场数量
     */
    private Integer marketCount;
    /**
     * 供应商商品数量
     */
    private Integer supplierGoodsCount;
}