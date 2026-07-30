package com.newzkl.platform.base.biz.account.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 铺货商品
 *
 * <p>迁移: 跨域 market 结构 {@code com.zkl.scm.market.rpc.model.DistributionRandomRPCVO}
 * 降级为 account 本地端口 DTO, 只保留旧 {@code getUserHomePage} 用到的字段。</p>
 *
 * @author KC
 */
@Data
public class DistributionRandomInfo implements Serializable {

    /**
     * 铺货ID
     */
    private Long id;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品图片
     */
    private String img;

    /**
     * 销售价
     */
    private Integer sellPrice;
}
