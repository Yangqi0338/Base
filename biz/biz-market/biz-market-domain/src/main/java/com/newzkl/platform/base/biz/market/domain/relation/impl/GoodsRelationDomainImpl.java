package com.newzkl.platform.base.biz.market.domain.relation.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.domain.adapt.repository.GoodsRelationRepository;
import com.newzkl.platform.base.biz.market.domain.relation.GoodsRelationDomain;
import com.newzkl.platform.base.biz.market.model.dto.relation.GoodsRelationQueryDTO;
import com.newzkl.platform.base.biz.market.model.dto.relation.MarketGoodsRelationDTO;
import com.newzkl.platform.base.biz.market.model.query.relation.GoodsListPageQuery;
import com.newzkl.platform.base.biz.market.model.query.relation.MarketGoodsPageQuery;
import com.newzkl.platform.base.biz.market.model.req.relation.PlatformQueryMarketNotAddGoodsReq;
import com.newzkl.platform.base.biz.market.model.req.relation.SaveGoodsRelationReq;
import com.newzkl.platform.base.biz.market.model.req.relation.UpdateGoodsRelationReq;
import com.newzkl.platform.base.biz.market.model.vo.relation.GoodsRelationListVO;
import com.newzkl.platform.base.common.ddd.facade.ApiChannelSpuRelationVO;
import com.newzkl.platform.base.common.ddd.facade.SpuRelevancyMarketVO;
import com.newzkl.platform.base.common.ddd.model.enums.goods.GoodsRelationEnum;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author niu
 * @description: 商品关系维护接口实现
 * @date 2023/12/6 15:30
 */
@Service
@RequiredArgsConstructor
public class GoodsRelationDomainImpl implements GoodsRelationDomain {

    private final GoodsRelationRepository goodsRelationRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> saveGoodsRelation(SaveGoodsRelationReq req) {
        List<MarketGoodsRelationDTO> marketGoodsRelations = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        req.getGoodsIds().forEach(x->{
            GoodsRelationQueryDTO query = new GoodsRelationQueryDTO();
            query.setUserId(req.getUserId());
            query.setMarketId(req.getMarketId());
            query.setRelationType(req.getRelationType());
            query.setGoodsId(x);
            if (CollUtil.isEmpty(goodsRelationRepository.queryGoodsRelationListByDTO(query))){
                MarketGoodsRelationDTO marketGoodsRelation = new MarketGoodsRelationDTO();
                marketGoodsRelation.setGoodsId(x);
                marketGoodsRelation.setMarketId(req.getMarketId());
                marketGoodsRelation.setRelationType(req.getRelationType());
                marketGoodsRelation.setUserId(req.getUserId());
                marketGoodsRelation.setCreateTime(now);
                marketGoodsRelation.setDiscountRate(req.getDiscountRate());
                if (req.getRelationType() == GoodsRelationEnum.GoodsRelation.TWO_MARKET_GOODS){
                    marketGoodsRelation.setGoodsInfo(JSONUtil.toJsonStr(req.getGoodsInfoVO()));
                }
                if(req.getRelationType() == GoodsRelationEnum.GoodsRelation.SELECT_GOODS){
                    //补充商品信息
                    GoodsRelationQueryDTO goodsRelation = new GoodsRelationQueryDTO();
                    goodsRelation.setMarketId(req.getMarketId());
                    goodsRelation.setGoodsId(x);
                    goodsRelation.setRelationType(GoodsRelationEnum.GoodsRelation.TWO_MARKET_GOODS);
                    List<MarketGoodsRelationDTO> dtoList = goodsRelationRepository.queryGoodsRelationListByDTO(goodsRelation);
                    if(CollectionUtil.isNotEmpty(dtoList)) {
                        marketGoodsRelation.setGoodsInfo(dtoList.get(0).getGoodsInfo());
                    }

                }
                marketGoodsRelations.add(marketGoodsRelation);
            }
        });
        if (CollectionUtil.isNotEmpty(marketGoodsRelations)){
            goodsRelationRepository.batchSaveGoodsRelation(marketGoodsRelations);
        }

        return marketGoodsRelations.stream().map(MarketGoodsRelationDTO::getGoodsId).collect(Collectors.toList());
    }

    @Override
    public Page<MarketGoodsRelationDTO> queryGoodsRelationPage(GoodsRelationQueryDTO query) {
        return goodsRelationRepository.queryGoodsRelationPage(query);
    }

    @Override
    public List<MarketGoodsRelationDTO> queryGoodsRelationListByDTO(GoodsRelationQueryDTO query) {
        return goodsRelationRepository.queryGoodsRelationListByDTO(query);
    }

    @Override
    public List<SpuRelevancyMarketVO> getSpuRelevancyMarketNum(List<Long> spuIdList) {
        return goodsRelationRepository.getSpuRelevancyMarketNum(spuIdList);
    }

    @Override
    public Page<GoodsRelationListVO> platformQueryMarketGoodsList(GoodsListPageQuery req) {
        return goodsRelationRepository.queryGoodsRelationList(req);
    }

    @Override
    public Page<GoodsRelationListVO> appQueryMarketGoodList(GoodsListPageQuery req) {
        return goodsRelationRepository.appQueryMarketGoodList(req);
    }

    @Override
    public Page<GoodsRelationListVO> channelQuerySelectGoodsList(MarketGoodsPageQuery req) {
        GoodsListPageQuery queryGoodsListReq = TransferUtils.transfer(req, GoodsListPageQuery::new);
        queryGoodsListReq.setUserId(SecurityUtils.getAccountId());
        queryGoodsListReq.setRelationType(GoodsRelationEnum.GoodsRelation.SELECT_GOODS.getCode());
        return goodsRelationRepository.queryGoodsRelationList(queryGoodsListReq);
    }

    @Override
    public Page<GoodsRelationListVO> queryClientBindMarketGoodsRelationList(GoodsListPageQuery req) {
        return goodsRelationRepository.queryClientBindMarketGoodsRelationList(req);
    }

    @Override
    public Page<GoodsRelationListVO> platformQueryMarketNotAddGoodsList(PlatformQueryMarketNotAddGoodsReq req) {
        return goodsRelationRepository.platformQueryMarketNotAddGoodsList(req);
    }

    @Override
    public Page<GoodsRelationListVO> channelMarketNotSelectedGoodsList(PlatformQueryMarketNotAddGoodsReq req) {
        return goodsRelationRepository.channelMarketNotSelectedGoodsList(req);
    }

    @Override
    public Page<GoodsRelationListVO> channelDistributionSelectedGoodsList(PlatformQueryMarketNotAddGoodsReq query) {
        return goodsRelationRepository.channelDistributionSelectedGoodsList(query);
    }

    @Override
    public void channelCancelSelected(Long id) {
         goodsRelationRepository.channelCancelSelected(id);
    }

    @Override
    public Page<ApiChannelSpuRelationVO> channelSpuRelationList(GoodsListPageQuery query) {
        return goodsRelationRepository.channelSpuRelationList(query);
    }

    @Override
    public void updateMarketGoodsLabel(UpdateGoodsRelationReq req) {
        goodsRelationRepository.updateMarketGoodsLabel(req);
    }

    @Override
    public void updateMarketGoodsLabel(Long accountId, Long goodsId, String productLabel) {
        GoodsRelationQueryDTO query = new GoodsRelationQueryDTO();
        query.setUserId(accountId);
        query.setGoodsId(goodsId);
        query.setRelationType(GoodsRelationEnum.GoodsRelation.SELECT_GOODS);
        List<MarketGoodsRelationDTO> list = queryGoodsRelationListByDTO(query);
        if (list == null || list.isEmpty()) {
            return;
        }
        UpdateGoodsRelationReq req = new UpdateGoodsRelationReq();
        req.setId(list.get(0).getId());
        req.setGoodsInfo(productLabel);
        updateMarketGoodsLabel(req);
    }

}
