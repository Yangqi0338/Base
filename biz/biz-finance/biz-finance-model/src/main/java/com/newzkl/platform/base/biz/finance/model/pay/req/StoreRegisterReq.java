package com.newzkl.platform.base.biz.finance.model.pay.req;

import lombok.Data;

import java.io.Serializable;

/**
 * 开店入参
 *
 * <p>迁移: 跨域 goods 结构 {@code com.zkl.scm.goods.rpc.model.store.req.StoreRegisterReq}
 * 降级为 account 本地端口 DTO。</p>
 *
 * @author KC
 */
@Data
public class StoreRegisterReq implements Serializable {

    /**
     * 渠道商ID
     */
    private Long channelId;

    /**
     * 门店类型
     */
    private Long storeType;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 门店地址
     */
    private String address;

    /**
     * 无参构造
     */
    public StoreRegisterReq() {
    }

    /**
     * 按渠道商ID构造
     *
     * @param channelId 渠道商ID
     */
    public StoreRegisterReq(Long channelId) {
        this.channelId = channelId;
    }
}
