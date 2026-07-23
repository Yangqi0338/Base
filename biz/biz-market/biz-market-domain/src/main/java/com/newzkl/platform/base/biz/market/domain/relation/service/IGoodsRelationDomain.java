package com.newzkl.platform.base.biz.market.domain.relation.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.model.dto.relation.GoodsRelationQueryDTO;
import com.newzkl.platform.base.biz.market.model.dto.relation.MarketGoodsRelationDTO;
import com.newzkl.platform.base.biz.market.model.query.relation.GoodsListPageQuery;
import com.newzkl.platform.base.biz.market.model.query.relation.MarketGoodsPageQuery;
import com.newzkl.platform.base.biz.market.model.req.relation.PlatformQueryMarketNotAddGoodsReq;
import com.newzkl.platform.base.biz.market.model.req.relation.SaveGoodsRelationReq;
import com.newzkl.platform.base.biz.market.model.req.relation.UpdateGoodsRelationReq;
import com.newzkl.platform.base.biz.market.model.vo.relation.GoodsRelationListVO;
import com.newzkl.platform.base.biz.market.model.rpc.openapi.ApiChannelSpuRelationVO;
import com.newzkl.platform.base.biz.market.model.rpc.relation.SpuRelevancyMarketVO;

import java.util.List;

/**
 * @author niu
 * @description: 商品关系接口
 * @date 2023/12/6 14:52
 */
public interface IGoodsRelationDomain {

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
     * app查询市场商品
     */
    Page<GoodsRelationListVO> appQueryMarketGoodList(GoodsListPageQuery req);

    /**
     * 运营商查询二级市场
     */
    Page<GoodsRelationListVO> operatorQueryMarketGoodsList(MarketGoodsPageQuery req);

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
     * 运营商二级市场添加商品查询商品列表
     */
    Page<GoodsRelationListVO> operateQueryMarketNotAddGoodsList(PlatformQueryMarketNotAddGoodsReq req);

    /**
     * 渠道商选品查询市场商品
     */
    Page<GoodsRelationListVO> channelMarketNotSelectedGoodsList(PlatformQueryMarketNotAddGoodsReq req);

    /**
     * 渠道商铺货查询选品列表
     */
    Page<GoodsRelationListVO> channelDistributionSelectedGoodsList(PlatformQueryMarketNotAddGoodsReq query);

    /**
     * 渠道商取消选品
     */
    void channelCancelSelected(Long id);
    /**
     * API选品关系
     */
    Page<ApiChannelSpuRelationVO> channelSpuRelationList(GoodsListPageQuery query);

    void updateMarketGoodsLabel(UpdateGoodsRelationReq updateGoodsRelationReq);
}
