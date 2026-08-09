package com.newzkl.platform.base.biz.market.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.model.dto.distribution.StoreDistributionDTO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.biz.market.model.query.distribution.DistributionRandomPageQuery;
import com.newzkl.platform.base.biz.market.model.query.distribution.DistributionsPageQuery;
import com.newzkl.platform.base.biz.market.model.query.distribution.DistributionsQuery;
import com.newzkl.platform.base.biz.market.model.query.distribution.StoreDistributionQuery;
import com.newzkl.platform.base.biz.market.model.res.distribution.DistributionGoodsDetailRes;
import com.newzkl.platform.base.biz.market.model.res.distribution.DistributionGoodsListRes;
import com.newzkl.platform.base.biz.market.model.vo.market.DistributionCategoryVO;
import com.newzkl.platform.base.biz.market.model.vo.market.DistributionGoodsInfoVO;
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
 * @description: 铺货数仓
 * @date 2024/4/2 14:40
 */
public interface DistributionRepository {

    /**
     * 批量查询id
     */
    List<Long> idByQuery(DistributionsQuery req);

    /**
     * 批量修改铺货
     */
    void batchUpdateDistributions(List<StoreDistributionDTO> storeDistributionList);

    /**
     * 修改铺货
     */
    void updateDistributions(StoreDistributionDTO storeDistributionDTO);

    /**
     * 查询铺货列表-渠道商
     */
    Page<DistributionGoodsListRes> queryDistributionsChannel(DistributionsQuery req);

    /**
     * 查询铺货列表-总平台
     */
    Page<DistributionGoodsListOPVO> queryDistributionsOverallPlatform(DistributionsPageQuery query);

    /**
     * 查询铺货列表
     */
    List<StoreDistributionDTO> queryDistributionsList(StoreDistributionQuery query);

    /**
     * 查询铺货表数据
     */
    StoreDistributionDTO queryOneDistributions(StoreDistributionQuery query);

    /**
     * 根据skuId查询spuId
     */
    List<Long> getSpuIdsBySkuId(Long skuId);

    /**
     * 查询铺货商品信息
     */
    DistributionGoodsInfoVO queryGoodsDistribution(Long goodsId, boolean proprietary, Long channelId);

    /**
     * 查询铺货商品sku信息
     */
    List<DistributionGoodsInfoVO> queryGoodsDistributionSku(Long goodsId);

    void batchSave(List<StoreDistributionDTO> storeDistributionList);

    void delGoods(Long goodsId,Long accountId);

    /**
     * 统计可删除的铺货商品数量（下架/待上架且销量为0的商品级记录）
     */
    Long countDeletableGoods(List<Long> uniqueGoodsIdList, Long accountId);

    List<DistributionCategoryVO> getDistributionGoodsCategory(Long storeId);

    /**
     * 铺货列表随机分页查询
     */
    Page<DistributionRandomVO> randomSelectedGoodsPage(DistributionRandomPageQuery query);

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
     * 查询渠道商商品是否存在
     */
    Long queryChannelGoodsIsExist(Long goodsId, Long channelId, Long storeId);

    /**
     * 查询渠道商商品是否存在
     */
    Long queryChannelSkuIsExist(Long skuId, Long channelId, Long storeId);

    /**
     * 更新渠道商商品售价
     *
     * @param sellPrice 售价 (Money, 落库 BIGINT 分)
     */
    void alterChannelGoodsSellPrice(Long goodsId, Long channelId, Long storeId, Money sellPrice);

    /**
     * 更新渠道商sku售价
     *
     * @param sellPrice 售价 (Money, 落库 BIGINT 分)
     */
    void alterChannelSkuSellPrice(Long skuId, Long channelId, Long storeId, Money sellPrice);

    void increaseSellNum(Long id, Integer num);

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
     * 批量随机查询指定数量的门店下的铺货商品
     */
    Map<Long, List<DistributionRandomRPCVO>> queryRandomDistributionByStoreIdList(List<Long> storeIdList, Integer limitNum);

    /**
     * 根据id查询铺货信息
     */
    List<StoreDistributionDTO> getByIds(List<Long> ids);

    /**
     * 批量查询铺货详情
     */
    List<DistributionDetailVO> queryDistributionDetailByIds(List<Long> ids);

    /**
     * 复制渠道商铺货
     */
    void copyChannelDistribution(Long channelId, Long targetChannelId, Set<Long> goodsIdList);

}
