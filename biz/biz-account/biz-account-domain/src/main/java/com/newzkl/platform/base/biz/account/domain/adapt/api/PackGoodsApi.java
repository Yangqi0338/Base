package com.newzkl.platform.base.biz.account.domain.adapt.api;

/**
 * 入会礼包商品出站端口 (outbound port)。
 *
 * <p>迁移: 原直连 {@code com.zkl.scm.admin.rpc.facade.IPackGoodsFacade};
 * 中台化后跨域只经端口, 由入口 starter 侧远程 consumer 覆盖默认实现。</p>
 *
 * @author KC
 */
public interface PackGoodsApi {

    /**
     * 保存入会礼包商品。
     *
     * @param req 礼包写入入参
     * @return 礼包商品 ID, 未接线时返回 null
     */
    Long save(PackGoodsSaveReq req);

    /**
     * 按 (类型, 等级) 查询入会礼包商品。
     *
     * @param query 查询条件
     * @return 礼包商品信息, 无则 null
     */
    PackGoodsInfo findByQuery(PackGoodsQuery query);
}
