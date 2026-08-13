package com.newzkl.platform.base.biz.account.facade.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 入会礼包订单信息 (对外出参)
 *
 * <p>facade 自带 model, 物理禁引 biz-user-model。供 biz-finance 支付回调读订单要素。</p>
 *
 * @author KC
 */
@Data
public class PackOrderFacadeDTO implements Serializable {

    /**
     * 订单 ID
     */
    private Long id;

    /**
     * 下单账号 ID
     */
    private Long accountId;

    /**
     * 订单金额 (分)
     */
    private Integer amount;

    /**
     * 礼包商品 ID
     */
    private Long packId;

    /**
     * 礼包类型 (角色 ID)
     */
    private Integer packType;

    /**
     * 礼包等级
     */
    private Integer packLevel;

    /**
     * 订单状态
     */
    private Integer state;
}
