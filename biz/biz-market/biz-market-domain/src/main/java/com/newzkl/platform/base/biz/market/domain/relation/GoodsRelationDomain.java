package com.newzkl.platform.base.biz.market.domain.relation;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

import java.util.List;

/**
 * @author niu
 * @description: 商品关系接口
 * @date 2023/12/6 14:52
 */
public interface GoodsRelationDomain {

    /**
     * 保存商品关系
     */
    List<Long> saveGoodsRelation(SaveGoodsRelationReq req);

    /**
     * 商品关联的市场数量
     */
    List<SpuRelevancyMarketVO> getSpuRelevancyMarketNum(List<Long> spuIdList);

    /**
     * 分页查询商品关系列表
     */
    Page<MarketGoodsRelationDTO> queryGoodsRelationPage(GoodsRelationQueryDTO query);

    /**
     * 不分页查询商品关系列表
     */
    List<MarketGoodsRelationDTO> queryGoodsRelationListByDTO(GoodsRelationQueryDTO query);

    /**
     * 平台查询市场商品
     */
    Page<GoodsRelationListVO> platformQueryMarketGoodsList(GoodsListPageQuery req);

    /**
     * 渠道商查询选品列表
     */
    Page<GoodsRelationListVO> channelQuerySelectGoodsList(MarketGoodsPageQuery req);

    /**
     * 查询客户绑定市场商品关系列表
     */
    Page<GoodsRelationListVO> queryClientBindMarketGoodsRelationList(GoodsListPageQuery req);

    /**
     * 平台市场添加商品查询商品列表
     */
    Page<GoodsRelationListVO> platformQueryMarketNotAddGoodsList(PlatformQueryMarketNotAddGoodsReq req);

    /**
     * 渠道商选品查询市场商品
     */
    Page<GoodsRelationListVO> channelMarketNotSelectedGoodsList(PlatformQueryMarketNotAddGoodsReq req);

    /**
     * 渠道商取消选品
     */
    void channelCancelSelected(Long id);
    /**
     * API选品关系
     */
    Page<ApiChannelSpuRelationVO> channelSpuRelationList(GoodsListPageQuery query);

    void updateMarketGoodsLabel(UpdateGoodsRelationReq updateGoodsRelationReq);

    /**
     * 更新市场商品标签 (按账号+商品查询)
     */
    void updateMarketGoodsLabel(Long accountId, Long goodsId, String productLabel);

    /**
     * 反查订阅指定 SPU 的渠道商账户ID列表
     *
     * <p>覆盖两条订阅路径并去重: 市场选品(relation_type=SELECT_GOODS 取 user_id)、
     * 专区绑定(relation_type=MARKET_GOODS 取 market_id 再查 market_bind 中 bind_type=CHANNEL
     * 且 state=YES 的 user_id)</p>
     *
     * @param spuId SPU 主键
     * @return 渠道商账户ID列表 无订阅返回空列表
     */
    List<Long> channelIdListBySpuId(Long spuId);
}
