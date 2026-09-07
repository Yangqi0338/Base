package com.newzkl.platform.base.biz.market.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.model.dto.relation.GoodsRelationQueryDTO;
import com.newzkl.platform.base.biz.market.model.dto.relation.MarketGoodsRelationDTO;
import com.newzkl.platform.base.biz.market.model.query.relation.GoodsListPageQuery;
import com.newzkl.platform.base.biz.market.model.req.relation.PlatformQueryMarketNotAddGoodsReq;
import com.newzkl.platform.base.biz.market.model.req.relation.UpdateGoodsRelationReq;
import com.newzkl.platform.base.biz.market.model.vo.relation.GoodsRelationListVO;
import com.newzkl.platform.base.common.ddd.facade.ApiChannelSpuRelationVO;
import com.newzkl.platform.base.common.ddd.facade.SpuRelevancyMarketVO;

import java.util.List;

/**
 * @author niu
 * @description: 商品关系数据仓库接口
 * @date 2023/12/6 15:31
 */
public interface GoodsRelationRepository {

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
     * 新增商品关系
     */
    void batchSaveGoodsRelation(List<MarketGoodsRelationDTO> marketGoodsRelations);

    /**
     * 平台查询市场商品
     */
    Page<GoodsRelationListVO> queryGoodsRelationList(GoodsListPageQuery req);

    Page<GoodsRelationListVO> queryClientBindMarketGoodsRelationList(GoodsListPageQuery req);

    Page<GoodsRelationListVO> platformQueryMarketNotAddGoodsList(PlatformQueryMarketNotAddGoodsReq req);

    Page<GoodsRelationListVO> channelMarketNotSelectedGoodsList(PlatformQueryMarketNotAddGoodsReq req);

    void channelCancelSelected(Long id);

    Page<ApiChannelSpuRelationVO> channelSpuRelationList(GoodsListPageQuery query);

    void updateMarketGoodsLabel(UpdateGoodsRelationReq req);

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
