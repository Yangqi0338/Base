package com.newzkl.platform.base.biz.market.domain.market.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.domain.adapt.repository.MarketRepository;
import com.newzkl.platform.base.biz.market.domain.market.MarketDomain;
import com.newzkl.platform.base.biz.market.model.dto.market.MarketBindDTO;
import com.newzkl.platform.base.biz.market.model.dto.market.MarketDTO;
import com.newzkl.platform.base.biz.market.model.query.market.AppBindMarketGoodsPageQuery;
import com.newzkl.platform.base.biz.market.model.query.market.MarketQuery;
import com.newzkl.platform.base.biz.market.model.req.market.BindMarketListReq;
import com.newzkl.platform.base.biz.market.model.req.market.ClientBindMarketReq;
import com.newzkl.platform.base.biz.market.model.req.market.MarketUserReq;
import com.newzkl.platform.base.biz.market.model.req.market.UpdateMarketDataReq;
import com.newzkl.platform.base.biz.market.model.vo.market.*;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.facade.MarketRpcVO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.market.MarketEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MarketDomainImpl implements MarketDomain {

    private final MarketRepository marketRepository;

    @Override
    public Page<BindMarketGoodsRes> queryMarketGoods(AppBindMarketGoodsPageQuery query) {
        return marketRepository.queryMarketGoods(query);
    }

    @Override
    public Page<MarketRes> queryMarketList(MarketQuery query) {
        return marketRepository.queryMarketList(query);
    }

    @Override
    public MarketRes queryMarket(Long marketId) {
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

    /**
     * 渠道商绑定市场
     *
     * <p>仅供 {@link #appChannelBindMarket} 内部复用, 事务由外层方法保证</p>
     */
    private Long bindMarket(ClientBindMarketReq req) {
        MarketBindDTO exist = marketRepository.queryAccountIsBindMarket(req);
        MarketBindDTO marketBindDTO = TransferUtils.transfer(req, MarketBindDTO::new);
        marketBindDTO.setState(CommonEnum.YesOrNo.YES);
        if (exist == null) {
            marketBindDTO.setCreateTime(LocalDateTime.now());
            marketRepository.createMarketBind(marketBindDTO);
            marketRepository.alterMarketData(UpdateMarketDataReq.buildUpdateMarketDataReq(
                    req.getMarketId(), MarketEnum.NumType.SUB_BIND_NUM, 1));
        } else {
            marketBindDTO.setId(exist.getId());
            marketRepository.updateMarketBind(marketBindDTO);
            if (CommonEnum.YesOrNo.YES != exist.getState()) {
                marketRepository.alterMarketData(UpdateMarketDataReq.buildUpdateMarketDataReq(
                        req.getMarketId(), MarketEnum.NumType.SUB_BIND_NUM, 1));
            }
        }
        return marketBindDTO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long appChannelBindMarket(ClientBindMarketReq req) {
        req.setBindType(AccountEnum.Identity.CHANNEL);
        return this.bindMarket(req);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deBindMarket(Long id, Long operatorId) {
        MarketBindDTO bind = marketRepository.queryMarketBind(id);
        if (bind == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "市场绑定");
        }
        if (!Objects.equals(bind.getUserId(), operatorId)) {
            throw new PlatformException(BaseErrorCode.NO_AUTH);
        }
        if (CommonEnum.YesOrNo.NO == bind.getState()) {
            return;
        }
        marketRepository.deBindMarket(id);
        marketRepository.alterMarketData(UpdateMarketDataReq.buildUpdateMarketDataReq(
                bind.getMarketId(), MarketEnum.NumType.SUB_BIND_NUM, -1));
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
        req.setBindAccountId(accountId);
        req.setBindIdentity(AccountEnum.Identity.CHANNEL);
        List<BindMarketVO> list = queryBindMarket(req);
        return TransferUtils.transfers(list, MarketRpcVO.class);
    }

}
