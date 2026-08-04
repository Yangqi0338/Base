package com.newzkl.platform.base.biz.user.facade.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 入会礼包商品信息 (对外出参)
 *
 * <p>facade 自带 model, 物理禁引 biz-user-model。字段对齐消费方 biz-account
 * {@code PackGoodsApi.findByQuery} 的 {@code PackGoodsInfo}, 供升级校验。</p>
 *
 * @author KC
 */
@Data
public class PackGoodsFacadeDTO implements Serializable {

    /**
     * 礼包商品 ID
     */
    private Long id;

    /**
     * 礼包金额 (分)
     */
    private Integer amount;

    /**
     * 礼包对应等级值
     */
    private Integer level;

    /**
     * 类型 (角色 ID)
     */
    private Integer type;

    /**
     * 礼包名称
     */
    private String name;
}
