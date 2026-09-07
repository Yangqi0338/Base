package com.newzkl.platform.base.biz.market.application.service;

import com.newzkl.platform.base.biz.market.model.req.relation.SaveGoodsRelationReq;

/**
 * 市场商品应用服务
 *
 * <p>承接市场商品/选品的跨域编排：商品关系落库与市场商品数累加、选品前置校验等
 * 多领域协作逻辑, 使 Controller 只做参数装配与身份注入。</p>
 *
 * @author KC
 */
public interface MarketGoodsService {

    /**
     * 市场添加商品（平台侧）
     *
     * <p>落市场商品关系({@code MARKET_GOODS})并累加市场商品数。</p>
     *
     * @param req 保存商品关系请求
     */
    void saveMarketGoodsRelation(SaveGoodsRelationReq req);

    /**
     * 渠道商市场选品
     *
     * <p>前置校验商品尚未被选品, 再落选品关系({@code SELECT_GOODS})。</p>
     *
     * @param req       保存商品关系请求
     * @param accountId 当前渠道商账号ID
     */
    void saveMarketGoodsSelectRelation(SaveGoodsRelationReq req, Long accountId);
}
