package com.newzkl.platform.base.biz.market.domain.distribution;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.model.dto.distribution.StateNotifyDTO;
import com.newzkl.platform.base.biz.market.model.dto.distribution.StoreDistributionDTO;
import com.newzkl.platform.base.biz.market.model.event.distribution.WorkTableUpDownEventMq;
import com.newzkl.platform.base.biz.market.model.query.distribution.DistributionRandomPageQuery;
import com.newzkl.platform.base.biz.market.model.query.distribution.DistributionsPageQuery;
import com.newzkl.platform.base.biz.market.model.query.distribution.DistributionsQuery;
import com.newzkl.platform.base.biz.market.model.query.distribution.StoreDistributionQuery;
import com.newzkl.platform.base.biz.market.model.req.distribution.DistributionsBatchUpdateReq;
import com.newzkl.platform.base.biz.market.model.req.distribution.GoodsStateAlterReq;
import com.newzkl.platform.base.biz.market.model.res.distribution.DistributionGoodsDetailRes;
import com.newzkl.platform.base.biz.market.model.res.distribution.DistributionGoodsListRes;
import com.newzkl.platform.base.biz.market.model.vo.market.DistributionCategoryVO;
import com.newzkl.platform.base.biz.market.model.vo.market.DistributionGoodsListOPVO;
import com.newzkl.platform.base.biz.market.model.vo.market.DistributionRandomVO;
import com.newzkl.platform.base.common.ddd.facade.DistributionDetailVO;
import com.newzkl.platform.base.biz.market.model.rpc.distribution.DistributionRandomRPCVO;
import com.newzkl.platform.base.common.ddd.facade.GoodsSellNumVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author niu
 * @description: 铺货服务
 * @date 2024/4/2 11:09
 */
public interface DistributionDomain {

    /**
     * 批量查询
     */
    List<Long> idByQuery(DistributionsQuery req);

    /**
     * 上下架通知
     */
    DistributionsBatchUpdateReq upDownEvent(WorkTableUpDownEventMq workTableUpDownEventMq);

    /**
     * 批量修改铺货
     */
    StateNotifyDTO batchUpdateDistributions(DistributionsBatchUpdateReq req);

    /**
     * 查询铺货列表-渠道商
     */
    Page<DistributionGoodsListRes> queryDistributionsChannel(DistributionsQuery req);

    /**
     * 查询铺货列表-总平台
     */
    Page<DistributionGoodsListOPVO> queryDistributionsOverallPlatform(DistributionsPageQuery query);

    /**
     * 根据skuId查询spuId
     */
    List<Long> getSpuIdsBySkuId(Long skuId);

    /**
     * 铺货
     */
    void goodsDistribution(Long channelId, Long goodsId, boolean proprietary);

    /**
     * 查询铺货商品信息集合
     */
    List<StoreDistributionDTO> getDistributionDetailList(Long channelId, Long goodsId, boolean isSpu);

    /**
     * 更新商品状态
     */
    void alterGoodsState(GoodsStateAlterReq req);

    /**
     * 删除铺货商品
     */
    void delGoods(Long goodsId);

    /**
     * 批量删除铺货商品
     */
    void batchDelGoods(List<Long> goodsIdList);

    /**
     * 铺货列表随机分页查询
     */
    Page<DistributionRandomVO> randomSelectedGoodsPage(DistributionRandomPageQuery query);

    /**
     * 获取铺货商品分类
     */
    List<DistributionCategoryVO> getDistributionGoodsCategory(Long storeId);

    /**
     * 推荐商品
     */
    void recommendationGoods(Long id);

    /**
     * 取消推荐商品
     */
    void cancelRecommendationGoods(Long id);

    /**
     * 平台门店上架
     * @param id 铺货ID
     */
    void platformStoreListed(Long id);

    /**
     * 平台门店下架
     * @param id 铺货ID
     */
    void platformStoreUnlisted(Long id);

    /**
     * 查询推荐铺货列表
     */
    List<DistributionGoodsDetailRes> queryRecommendationDistributions();

    /**
     * 查询推荐门店列表（每个门店销量最好的商品，按销量排序返回10个）
     */
    List<DistributionGoodsDetailRes> queryRecommendationStores();

    /**
     * 更新铺货
     */
    void alterDistribution(Long channelId, Long goodsId);

    /**
     * 批量复制铺货
     * 使用样板店时，批量复制目标样板店铺货数据，商品相同时覆盖自身商品
     */
    void copyChannelDistribution(Long channelId, Long targetChannelId, Set<Long> goodsIdList);

    /**
     * 批量随机查询指定数量的门店下的铺货商品
     */
    Map<Long, List<DistributionRandomRPCVO>> queryRandomDistributionByStoreIdList(List<Long> storeIdList, Integer limitNum);

    /**
     * 铺货表数据查询
     */
    List<StoreDistributionDTO> queryDistributionsList(StoreDistributionQuery query);

    /**
     * 增加销量
     */
    void increaseSellNum(Long skuId, Long storeId, Integer num);

    /**
     * 校验商品是否已铺货
     */
    Boolean checkGoodsIsDistributed(Long storeId, Long goodsId);

    /**
     * 门店单个商品总销量（门店商品真实销量+商品的虚拟销量）
     */
    Integer getStoreGoodsTotalSellNum(Long distributionId);

    /**
     * 批量查询商品总销量
     */
    List<GoodsSellNumVO> queryGoodsSellNum(List<Long> distributionIds);

    /**
     * 门店总销量（门店所有商品真实销量+商品的虚拟销量）
     */
    Integer getStoreTotalSellNum(Long storeId);

    /**
     * 查询铺货表数据
     */
    StoreDistributionDTO queryDistributionsByCondition(StoreDistributionQuery query);

    /**
     * 根据id查询铺货信息
     */
    List<StoreDistributionDTO> getByIds(List<Long> ids);

    /**
     * 批量查询铺货详情
     */
    List<DistributionDetailVO> queryDistributionDetailByIds(List<Long> ids);
}
