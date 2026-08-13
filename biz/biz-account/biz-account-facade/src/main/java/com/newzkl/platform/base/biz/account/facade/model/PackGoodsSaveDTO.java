package com.newzkl.platform.base.biz.account.facade.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 入会礼包商品写入契约 (对外入参)
 *
 * <p>facade 自带 model, 物理禁引 biz-user-model。字段对齐消费方 biz-account
 * {@code PackGoodsApi.save} 的 {@code PackGoodsSaveReq}。</p>
 *
 * @author KC
 */
@Data
public class PackGoodsSaveDTO implements Serializable {

    /**
     * 礼包商品 ID (新建时为空)
     */
    private Long id;

    /**
     * 类型 (角色 ID)
     */
    private Integer type;

    /**
     * 礼包对应等级值
     */
    private Integer level;

    /**
     * 礼包图片
     */
    private String img;

    /**
     * 礼包金额 (分)
     */
    private Integer amount;

    /**
     * 礼包名称
     */
    private String name;
}
