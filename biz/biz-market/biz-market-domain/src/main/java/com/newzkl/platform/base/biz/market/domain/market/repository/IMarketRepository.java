package com.newzkl.platform.base.biz.market.domain.market.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

import java.util.List;

public interface IMarketRepository {

    Page<AppBindMarketVO> queryChannelBindMarket(ChannelMarketPageQuery query);

    Page<AppBindMarketGoodsVO> queryChannelBindMarketGoods(AppBindMarketGoodsPageQuery query);

    Page<MarketVO> queryMarketList(MarketPageQuery query);

    MarketVO queryMarket(Long marketId);

    void saveMarket(MarketDTO marketDTO);

    void updateMarket(MarketDTO marketDTO);

    Long queryAccountIsBindMarket(ClientBindMarketReq req);

    void updateMarketBind(MarketBindDTO marketBindDTO);

    void createMarketBind(MarketBindDTO marketBindDTO);

    void alterMarketData(UpdateMarketDataReq req);

    void deBindMarket(Long id);

    List<BindMarketVO> queryBindMarket(BindMarketListReq req);

    List<MarketUserVO> queryMarketUser(MarketUserReq req);

    List<MarketGoodsCategoryVO> queryMarketGoodsCategory(Long marketId, Long userId);
}
