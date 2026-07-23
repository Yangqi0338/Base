package com.newzkl.platform.base.biz.market.domain.relation.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.model.dto.relation.GoodsRelationQueryDTO;
import com.newzkl.platform.base.biz.market.model.dto.relation.MarketGoodsRelationDTO;
import com.newzkl.platform.base.biz.market.model.query.relation.GoodsListPageQuery;
import com.newzkl.platform.base.biz.market.model.req.relation.PlatformQueryMarketNotAddGoodsReq;
import com.newzkl.platform.base.biz.market.model.req.relation.UpdateGoodsRelationReq;
import com.newzkl.platform.base.biz.market.model.vo.relation.GoodsRelationListVO;
import com.newzkl.platform.base.biz.market.model.rpc.openapi.ApiChannelSpuRelationVO;
import com.newzkl.platform.base.biz.market.model.rpc.relation.AlterChannelSelectorSellDataReq;
import com.newzkl.platform.base.biz.market.model.rpc.relation.SpuRelevancyMarketVO;

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

    /**
     * app查询市场商品
     */
    Page<GoodsRelationListVO> appQueryMarketGoodList(GoodsListPageQuery req);

    Page<GoodsRelationListVO> queryClientBindMarketGoodsRelationList(GoodsListPageQuery req);

    Page<GoodsRelationListVO> platformQueryMarketNotAddGoodsList(PlatformQueryMarketNotAddGoodsReq req);

    Page<GoodsRelationListVO> operateQueryMarketNotAddGoodsList(PlatformQueryMarketNotAddGoodsReq req);

    Page<GoodsRelationListVO> channelMarketNotSelectedGoodsList(PlatformQueryMarketNotAddGoodsReq req);

    void channelCancelSelected(Long id);

    Page<GoodsRelationListVO> channelDistributionSelectedGoodsList(PlatformQueryMarketNotAddGoodsReq query);

    Page<ApiChannelSpuRelationVO> channelSpuRelationList(GoodsListPageQuery query);

    void alterChannelSelectorSellData(List<AlterChannelSelectorSellDataReq> req);

    void updateMarketGoodsLabel(UpdateGoodsRelationReq req);
}
