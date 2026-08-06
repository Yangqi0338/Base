package com.newzkl.platform.base.biz.market.domain.market.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.domain.market.repository.MarketCategoryRepository;
import com.newzkl.platform.base.biz.market.domain.market.repository.MarketRepository;
import com.newzkl.platform.base.biz.market.domain.market.service.MarketDomain;
import com.newzkl.platform.base.biz.market.model.dto.market.MarketBindDTO;
import com.newzkl.platform.base.biz.market.model.dto.market.MarketCategoryPageQuery;
import com.newzkl.platform.base.biz.market.model.dto.market.MarketDTO;
import com.newzkl.platform.base.biz.market.model.query.market.AppBindMarketGoodsPageQuery;
import com.newzkl.platform.base.biz.market.model.query.market.ChannelMarketPageQuery;
import com.newzkl.platform.base.biz.market.model.query.market.MarketPageQuery;
import com.newzkl.platform.base.biz.market.model.req.market.BindMarketListReq;
import com.newzkl.platform.base.biz.market.model.req.market.ClientBindMarketReq;
import com.newzkl.platform.base.biz.market.model.req.market.MarketUserReq;
import com.newzkl.platform.base.biz.market.model.req.market.UpdateMarketDataReq;
import com.newzkl.platform.base.biz.market.model.vo.market.*;
import com.newzkl.platform.base.biz.market.model.biz.req.CategoryReq;
import com.newzkl.platform.base.common.ddd.facade.MarketRpcVO;
import com.newzkl.platform.base.common.ddd.model.enums.market.MarketEnum;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketDomainImpl implements MarketDomain {

    private final MarketRepository marketRepository;
    private final MarketCategoryRepository marketCategoryRepository;

    @Override
    public Page<AppBindMarketVO> queryChannelBindMarket(ChannelMarketPageQuery query) {
        return marketRepository.queryChannelBindMarket(query);
    }

    @Override
    public Page<AppBindMarketGoodsVO> queryChannelBindMarketGoods(AppBindMarketGoodsPageQuery query) {
        return marketRepository.queryChannelBindMarketGoods(query);
    }

    @Override
    public Page<MarketVO> queryMarketList(MarketPageQuery query) {
        return marketRepository.queryMarketList(query);
    }

    @Override
    public MarketVO queryMarket(Long marketId) {
        return marketRepository.queryMarket(marketId);
    }

    @Override
    public void saveMarket(MarketDTO marketDTO) {
        marketRepository.saveMarket(marketDTO);
    }

    @Override
    public void updateMarket(MarketDTO marketDTO) {
        marketRepository.updateMarket(marketDTO);
    }

    @Override
    public Long bindMarket(ClientBindMarketReq req) {
        Long bindId = marketRepository.queryAccountIsBindMarket(req);
        MarketBindDTO marketBindDTO = TransferUtils.transfer(req, MarketBindDTO::new);
        marketBindDTO.setId(bindId);
        marketBindDTO.setCreateTime(LocalDateTime.now());
        if (bindId == null){
            marketRepository.createMarketBind(marketBindDTO);
        }else {
            marketBindDTO.setId(bindId);
            marketBindDTO.setState(1);
            marketRepository.updateMarketBind(marketBindDTO);
        }
        return marketBindDTO.getId();
    }

    @Override
    public Long appChannelBindMarket(ClientBindMarketReq req) {
        req.setBindType(MarketEnum.User.CHANNEL.getType());
        req.setUserId(req.getUserId());

        // 更新市场统计数据
        marketRepository.alterMarketData(UpdateMarketDataReq.buildUpdateMarketDataReq(req.getMarketId(), MarketEnum.NumType.SUB_BIND_NUM, 1));
        return this.bindMarket(req);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long channelBindTradersMarket(ClientBindMarketReq req) {
        req.setBindType(MarketEnum.User.CHANNEL.getType());
        req.setUserId(req.getUserId());

        // 更新市场统计数据
        marketRepository.alterMarketData(UpdateMarketDataReq.buildUpdateMarketDataReq(req.getMarketId(), MarketEnum.NumType.SUB_BIND_NUM, 1));
        return this.bindMarket(req);
    }

    @Override
    public void deBindMarket(Long id) {
        marketRepository.deBindMarket(id);
    }

    @Override
    public List<BindMarketVO> queryBindMarket(BindMarketListReq req) {
        return marketRepository.queryBindMarket(req);
    }

    @Override
    public List<MarketUserVO> queryMarketUser(MarketUserReq req) {
        return marketRepository.queryMarketUser(req);
    }

    @Override
    public boolean saveMarketCategory(MarketCategoryVO marketCategory) {
        if (marketCategoryRepository.existsByName(marketCategory.getName(), marketCategory.getId())) {
            return false;
        }
        CategoryReq req = new CategoryReq();
        req.setName(marketCategory.getName());
        if (marketCategory.getId() != null) {
            req.setId(marketCategory.getId());
            marketCategoryRepository.categoryEdit(req);
        } else {
            marketCategoryRepository.categorySave(req);
        }
        return true;
    }

    @Override
    public Page<MarketCategoryVO> queryMarketCategoryPage(MarketCategoryPageQuery query) {
        return marketCategoryRepository.categoryPage(query);
    }

    @Override
    public void deleteMarketCategory(Long id) {
        marketCategoryRepository.categoryDelete(Collections.singletonList(id));
    }

    @Override
    public List<MarketGoodsCategoryVO> queryMarketGoodsCategory(Long marketId, Long userId) {
        return marketRepository.queryMarketGoodsCategory(marketId, userId);
    }

    @Override
    public void alterMarketData(UpdateMarketDataReq req) {
        marketRepository.alterMarketData(req);
    }

    @Override
    public List<MarketRpcVO> queryAccountBindMarket(Long accountId) {
        BindMarketListReq req = new BindMarketListReq();
        req.setClientId(accountId);
        req.setBindType(MarketEnum.User.CHANNEL.getType());
        List<BindMarketVO> list = queryBindMarket(req);
        return TransferUtils.transfers(list, MarketRpcVO.class);
    }

}
