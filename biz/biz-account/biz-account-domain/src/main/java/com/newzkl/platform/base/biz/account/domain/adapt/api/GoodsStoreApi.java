package com.newzkl.platform.base.biz.account.domain.adapt.api;

/**
 * 商品域门店出站端口 (outbound port)
 *
 * <p>迁移: 原直连 {@code com.zkl.scm.goods.rpc.facade.IStoreFacade};
 * 中台化后跨域只经端口, 由入口 starter 侧远程 consumer 覆盖默认实现。</p>
 *
 * @author KC
 */
public interface GoodsStoreApi {

    /**
     * 按渠道商查询门店
     *
     * @param channelId 渠道商ID
     * @return 门店, 无则 null
     */
    StoreRPCVO storeByChannelId(Long channelId);

    /**
     * 建立门店与账号的关联
     *
     * @param req 关联入参
     */
    void createStoreAccount(StoreAccountCreateReq req);
}
