package com.newzkl.platform.base.biz.market.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.model.dto.market.MarketBindDTO;
import com.newzkl.platform.base.biz.market.model.dto.market.MarketDTO;
import com.newzkl.platform.base.biz.market.model.query.market.AppBindMarketGoodsPageQuery;
import com.newzkl.platform.base.biz.market.model.query.market.MarketQuery;
import com.newzkl.platform.base.biz.market.model.req.market.BindMarketListReq;
import com.newzkl.platform.base.biz.market.model.req.market.ClientBindMarketReq;
import com.newzkl.platform.base.biz.market.model.req.market.MarketUserReq;
import com.newzkl.platform.base.biz.market.model.req.market.UpdateMarketDataReq;
import com.newzkl.platform.base.biz.market.model.vo.market.*;

import java.util.List;

public interface MarketRepository {

    Page<BindMarketGoodsRes> queryMarketGoods(AppBindMarketGoodsPageQuery query);

    Page<MarketRes> queryMarketList(MarketQuery query);

    MarketRes queryMarket(Long marketId);

    void saveMarket(MarketDTO marketDTO);

    void updateMarket(MarketDTO marketDTO);

    MarketBindDTO queryAccountIsBindMarket(ClientBindMarketReq req);

    /**
     * 按主键查绑定行
     *
     * @param id 绑定 id
     * @return 绑定行, 不存在返回 null
     */
    MarketBindDTO queryMarketBind(Long id);

    void updateMarketBind(MarketBindDTO marketBindDTO);

    void createMarketBind(MarketBindDTO marketBindDTO);

    void alterMarketData(UpdateMarketDataReq req);

    void deBindMarket(Long id);

    List<BindMarketVO> queryBindMarket(BindMarketListReq req);

    List<MarketUserVO> queryMarketUser(MarketUserReq req);
}
