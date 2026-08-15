package com.newzkl.platform.base.biz.market.domain.market.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.domain.adapt.repository.MarketRepository;
import com.newzkl.platform.base.biz.market.domain.market.MarketDomain;
import com.newzkl.platform.base.biz.market.model.dto.market.MarketBindDTO;
import com.newzkl.platform.base.biz.market.model.dto.market.MarketDTO;
import com.newzkl.platform.base.biz.market.model.query.market.AppBindMarketGoodsPageQuery;
import com.newzkl.platform.base.biz.market.model.query.market.ChannelMarketPageQuery;
import com.newzkl.platform.base.biz.market.model.query.market.MarketPageQuery;
import com.newzkl.platform.base.biz.market.model.req.market.BindMarketListReq;
import com.newzkl.platform.base.biz.market.model.req.market.ClientBindMarketReq;
import com.newzkl.platform.base.biz.market.model.req.market.MarketUserReq;
import com.newzkl.platform.base.biz.market.model.req.market.UpdateMarketDataReq;
import com.newzkl.platform.base.biz.market.model.vo.market.*;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.facade.MarketRpcVO;
import com.newzkl.platform.base.common.ddd.model.enums.market.MarketEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketDomainImpl implements MarketDomain {

    private final MarketRepository marketRepository;

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
