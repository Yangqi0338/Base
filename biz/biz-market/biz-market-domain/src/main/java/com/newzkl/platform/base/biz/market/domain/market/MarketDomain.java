package com.newzkl.platform.base.biz.market.domain.market;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.model.dto.market.MarketDTO;
import com.newzkl.platform.base.biz.market.model.query.market.AppBindMarketGoodsPageQuery;
import com.newzkl.platform.base.biz.market.model.query.market.MarketQuery;
import com.newzkl.platform.base.biz.market.model.req.market.BindMarketListReq;
import com.newzkl.platform.base.biz.market.model.req.market.ClientBindMarketReq;
import com.newzkl.platform.base.biz.market.model.req.market.MarketUserReq;
import com.newzkl.platform.base.biz.market.model.req.market.UpdateMarketDataReq;
import com.newzkl.platform.base.biz.market.model.vo.market.*;
import com.newzkl.platform.base.common.ddd.facade.MarketRpcVO;

import java.util.List;

public interface MarketDomain {

    /**
     * 获取绑定市场列表
     */
    Page<BindMarketGoodsRes> queryMarketGoods(AppBindMarketGoodsPageQuery query);

    /**
     * 查询市场列表
     */
    Page<MarketRes> queryMarketList(MarketQuery query);

    /**
     * 查询市场
     */
    MarketRes queryMarket(Long marketId);

    /**
     * 保存市场
     */
    void saveMarket(MarketDTO marketDTO);
    /**
     * 修改市场
     */
    void updateMarket(MarketDTO marketDTO);

    /**
     * 移动APP渠道商绑定交易市场
     */
    Long appChannelBindMarket(ClientBindMarketReq req);

    /**
     * 解除绑定市场
     *
     * @param id         绑定 id
     * @param operatorId 操作人账号 id, 必须是绑定行的归属人
     */
    void deBindMarket(Long id, Long operatorId);

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
