package com.newzkl.platform.base.biz.market.application.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.market.application.service.MarketGoodsService;
import com.newzkl.platform.base.biz.market.domain.market.MarketDomain;
import com.newzkl.platform.base.biz.market.domain.relation.GoodsRelationDomain;
import com.newzkl.platform.base.biz.market.model.dto.relation.GoodsRelationQueryDTO;
import com.newzkl.platform.base.biz.market.model.req.market.UpdateMarketDataReq;
import com.newzkl.platform.base.biz.market.model.req.relation.SaveGoodsRelationReq;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.model.enums.goods.GoodsRelationEnum;
import com.newzkl.platform.base.common.ddd.model.enums.market.MarketEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 市场商品应用服务实现
 *
 * <p>编排下移自 {@code MarketGoodsController}: 商品关系落库与市场商品数累加、选品前置
 * 校验在此收敛, 保证跨领域操作在同一事务内。</p>
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class MarketGoodsServiceImpl implements MarketGoodsService {

    private final GoodsRelationDomain goodsRelationDomain;
    private final MarketDomain marketDomain;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMarketGoodsRelation(SaveGoodsRelationReq req) {
        req.setUserId(0L);
        req.setRelationType(GoodsRelationEnum.GoodsRelation.MARKET_GOODS);
        goodsRelationDomain.saveGoodsRelation(req);
        marketDomain.alterMarketData(UpdateMarketDataReq.buildUpdateMarketDataReq(
                req.getMarketId(), MarketEnum.NumType.GOODS_NUM, req.getGoodsIds().size()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMarketGoodsSelectRelation(SaveGoodsRelationReq req, Long accountId) {
        req.setUserId(accountId);
        req.setRelationType(GoodsRelationEnum.GoodsRelation.SELECT_GOODS);
        assertGoodsNotSelected(req);
        goodsRelationDomain.saveGoodsRelation(req);
    }

    /**
     * 校验请求内商品尚未被当前渠道商选品
     *
     * <p>只要有一件商品已在选品库即整单拒绝, 抛 {@code EXIST_DATA}。</p>
     *
     * @param req 保存商品关系请求
     */
    private void assertGoodsNotSelected(SaveGoodsRelationReq req) {
        GoodsRelationQueryDTO query = new GoodsRelationQueryDTO();
        query.setGoodsIdList(req.getGoodsIds());
        query.setUserId(req.getUserId());
        query.setRelationType(req.getRelationType());
        if (CollUtil.isNotEmpty(goodsRelationDomain.queryGoodsRelationListByDTO(query))) {
            throw new PlatformException(BaseErrorCode.EXIST_DATA, "选品");
        }
    }
}
