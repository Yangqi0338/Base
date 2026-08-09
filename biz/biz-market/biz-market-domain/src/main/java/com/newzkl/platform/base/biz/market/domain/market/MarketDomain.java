package com.newzkl.platform.base.biz.market.domain.market;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.model.dto.market.MarketDTO;
import com.newzkl.platform.base.biz.market.model.query.market.AppBindMarketGoodsPageQuery;
import com.newzkl.platform.base.biz.market.model.query.market.ChannelMarketPageQuery;
import com.newzkl.platform.base.biz.market.model.query.market.MarketPageQuery;
import com.newzkl.platform.base.biz.market.model.req.market.BindMarketListReq;
import com.newzkl.platform.base.biz.market.model.req.market.ClientBindMarketReq;
import com.newzkl.platform.base.biz.market.model.req.market.MarketUserReq;
import com.newzkl.platform.base.biz.market.model.req.market.UpdateMarketDataReq;
import com.newzkl.platform.base.biz.market.model.vo.market.*;
import com.newzkl.platform.base.common.ddd.facade.MarketRpcVO;

import java.util.List;

public interface MarketDomain {

    /**
     * 渠道商获取绑定市场列表
     */
    Page<AppBindMarketVO> queryChannelBindMarket(ChannelMarketPageQuery query);

    /**
     * 渠道商获取绑定市场列表
     */
    Page<AppBindMarketGoodsVO> queryChannelBindMarketGoods(AppBindMarketGoodsPageQuery query);

    /**
     * 查询市场列表
     */
    Page<MarketVO> queryMarketList(MarketPageQuery query);

    /**
     * 查询市场
     */
    MarketVO queryMarket(Long marketId);

    /**
     * 保存市场
     */
    void saveMarket(MarketDTO marketDTO);
    /**
     * 修改市场
     */
    void updateMarket(MarketDTO marketDTO);
    /**
     * 交易师绑定二级市场
     */
    Long bindMarket(ClientBindMarketReq req);

    /**
     * 移动APP渠道商绑定交易市场
     */
    Long appChannelBindMarket(ClientBindMarketReq req);

    /**
     * 交易师给渠道商绑定二级市场
     */
    Long channelBindTradersMarket(ClientBindMarketReq req);

    /**
     * 解除绑定
     */
    void deBindMarket(Long id);

    /**
     * 查询用户绑定市场列表
     */
    List<BindMarketVO> queryBindMarket(BindMarketListReq req);

    /**
     * 查询市场绑定用户列表
     */
    List<MarketUserVO> queryMarketUser(MarketUserReq req);

    void alterMarketData(UpdateMarketDataReq req);

    /**
     * 查询账号绑定的渠道市场列表
     */
    List<MarketRpcVO> queryAccountBindMarket(Long accountId);

}
