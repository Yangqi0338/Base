package com.newzkl.platform.base.biz.market.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.domain.adapt.repository.DistributionRepository;
import com.newzkl.platform.base.biz.market.infrastructure.dao.StoreGoodsDAO;
import com.newzkl.platform.base.biz.market.infrastructure.entity.StoreGoodsDO;
import com.newzkl.platform.base.biz.market.model.dto.distribution.StoreGoodsDTO;
import com.newzkl.platform.base.common.ddd.model.enums.goods.DistributionEnum;
import com.newzkl.platform.base.biz.market.model.query.distribution.DistributionRandomPageQuery;
import com.newzkl.platform.base.biz.market.model.query.distribution.DistributionsPageQuery;
import com.newzkl.platform.base.biz.market.model.query.distribution.DistributionsQuery;
import com.newzkl.platform.base.biz.market.model.query.distribution.StoreGoodsQuery;
import com.newzkl.platform.base.biz.market.model.res.distribution.DistributionGoodsDetailRes;
import com.newzkl.platform.base.biz.market.model.res.distribution.DistributionGoodsListRes;
import com.newzkl.platform.base.common.ddd.facade.DistributionDetailVO;
import com.newzkl.platform.base.biz.market.model.rpc.distribution.DistributionRandomRPCVO;
import com.newzkl.platform.base.common.ddd.facade.GoodsSellNumVO;
import com.newzkl.platform.base.biz.market.model.vo.market.DistributionCategoryVO;
import com.newzkl.platform.base.biz.market.model.vo.market.DistributionGoodsInfoVO;
import com.newzkl.platform.base.biz.market.model.vo.market.DistributionGoodsListOPVO;
import com.newzkl.platform.base.biz.market.model.vo.market.DistributionRandomVO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * {@code DistributionRepository} 实现
 *
 * <p>照 {@code MarketRepositoryImpl} 风格: store_goods 单表读写用 MyBatis-Plus
 * 条件组装, 跨表(铺货 JOIN 商品/门店)的查询委派给 {@code StoreGoodsDAO}
 * 已声明的同签名自定义方法。</p>
 *
 * <p>说明: 委派给 DAO 自定义方法的查询依赖 MyBatis 语句绑定, 未绑定时由 MyBatis 抛
 * Invalid bound statement, 不会静默成功。</p>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class DistributionRepositoryImpl implements DistributionRepository {

    /**
     * 推荐门店取数条数, 接口 javadoc 明确"按销量排序返回10个"
     */
    private static final int RECOMMENDATION_STORE_LIMIT = 10;

    /**
     * 推荐铺货取数条数, 对齐 new-scm {@code queryRandomRecommendationDistributions(10)}
     */
    private static final int RECOMMENDATION_DISTRIBUTION_LIMIT = 10;

    private final StoreGoodsDAO storeGoodsDAO;

    @Override
    public List<Long> idByQuery(DistributionsQuery req) {
        return storeGoodsDAO.idByQuery(req);
    }

    @Override
    public void batchUpdateDistributions(List<StoreGoodsDTO> storeGoodsList) {
        if (CollectionUtils.isEmpty(storeGoodsList)) {
            return;
        }
        for (StoreGoodsDTO dto : storeGoodsList) {
            storeGoodsDAO.updateById(toDO(dto));
        }
    }

    @Override
    public void updateDistributions(StoreGoodsDTO storeGoodsDTO) {
        storeGoodsDAO.updateById(toDO(storeGoodsDTO));
    }

    @Override
    public Page<DistributionGoodsListRes> queryDistributionsChannel(DistributionsQuery req) {
        translateSort(req);
        return storeGoodsDAO.queryDistributionsChannel(RepositorySupport.page(req), req);
    }

    @Override
    public Page<DistributionGoodsListOPVO> queryDistributionsOverallPlatform(DistributionsPageQuery query) {
        return storeGoodsDAO.queryDistributionsOverallPlatform(RepositorySupport.page(query), query);
    }

    @Override
    public List<StoreGoodsDTO> queryDistributionsList(StoreGoodsQuery query) {
        return toDTOList(storeGoodsDAO.selectList(storeGoodsDAO.buildQueryWrapper(query)));
    }

    @Override
    public StoreGoodsDTO queryOneDistributions(StoreGoodsQuery query) {
        List<StoreGoodsDO> list = storeGoodsDAO.selectList(
                storeGoodsDAO.buildQueryWrapper(query).last("limit 1")
        );
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return toDTO(list.get(0));
    }

    @Override
    public List<Long> getSpuIdsBySkuId(Long skuId) {
        return storeGoodsDAO.selectList(
                new LambdaQueryWrapper<StoreGoodsDO>()
                        .select(StoreGoodsDO::getGoodsId)
                        .eq(StoreGoodsDO::getSkuId, skuId)
        ).stream().map(StoreGoodsDO::getGoodsId).distinct().collect(Collectors.toList());
    }

    @Override
    public DistributionGoodsInfoVO queryGoodsDistribution(Long goodsId, boolean proprietary, Long channelId) {
        return storeGoodsDAO.queryGoodsDistribution(goodsId, proprietary, channelId);
    }

    @Override
    public List<DistributionGoodsInfoVO> queryGoodsDistributionSku(Long goodsId) {
        return storeGoodsDAO.queryGoodsDistributionSku(goodsId);
    }

    @Override
    public void batchSave(List<StoreGoodsDTO> storeGoodsList) {
        if (CollectionUtils.isEmpty(storeGoodsList)) {
            return;
        }
        for (StoreGoodsDTO dto : storeGoodsList) {
            storeGoodsDAO.insert(toDO(dto));
        }
    }

    @Override
    public void delGoods(Long goodsId, Long accountId) {
        // 渠道商删除选品铺货 = 逻辑删除, 仅「下架(1)/待上架(2)且销量为 0」的商品级记录可删
        storeGoodsDAO.delete(
                new LambdaQueryWrapper<StoreGoodsDO>()
                        .eq(StoreGoodsDO::getChannelId, accountId)
                        .eq(StoreGoodsDO::getGoodsId, goodsId)
                        .in(StoreGoodsDO::getGoodsState,
                                DistributionEnum.State.UNLISTED, DistributionEnum.State.PENDING_LISTING)
                        .eq(StoreGoodsDO::getSellNum, 0)
        );
    }

    @Override
    public Long countDeletableGoods(List<Long> uniqueGoodsIdList, Long accountId) {
        if (CollectionUtils.isEmpty(uniqueGoodsIdList)) {
            return 0L;
        }
        // 对齐 new-scm countDeletableGoods: 仅统计 data_type=0 且符合删除条件的商品级记录数
        return storeGoodsDAO.selectCount(
                new LambdaQueryWrapper<StoreGoodsDO>()
                        .eq(StoreGoodsDO::getChannelId, accountId)
                        .eq(StoreGoodsDO::getStoreId, accountId)
                        .eq(StoreGoodsDO::getDataType, 0)
                        .in(StoreGoodsDO::getGoodsId, uniqueGoodsIdList)
                        .in(StoreGoodsDO::getGoodsState,
                                DistributionEnum.State.UNLISTED, DistributionEnum.State.PENDING_LISTING)
                        .eq(StoreGoodsDO::getSellNum, 0)
        );
    }

    @Override
    public void recoverGoods(Long goodsId, Long accountId) {
        // 逻辑删除恢复: del_flag NULL -> 0 (setSql 绕过 @TableLogic 字段识别)
        storeGoodsDAO.update(
                new LambdaUpdateWrapper<StoreGoodsDO>()
                        .eq(StoreGoodsDO::getChannelId, accountId)
                        .eq(StoreGoodsDO::getGoodsId, goodsId)
                        .setSql("del_flag = 0")
        );
    }

    @Override
    public List<DistributionCategoryVO> getDistributionGoodsCategory(Long storeId) {
        return storeGoodsDAO.queryDistributionGoodsCategory(storeId);
    }

    @Override
    public Page<DistributionRandomVO> randomSelectedGoodsPage(DistributionRandomPageQuery query) {
        return storeGoodsDAO.randomSelectedGoodsPage(RepositorySupport.page(query), query);
    }

    @Override
    public void recommendationGoods(Long id) {
        StoreGoodsDO distributionDO = new StoreGoodsDO();
        distributionDO.setId(id);
        distributionDO.setRecommendationTime(LocalDateTime.now());
        storeGoodsDAO.updateById(distributionDO);
    }

    @Override
    public void cancelRecommendationGoods(Long id) {
        // 走 update wrapper 显式置 null, updateById 会忽略 null 字段
        storeGoodsDAO.update(
                new LambdaUpdateWrapper<StoreGoodsDO>()
                        .eq(StoreGoodsDO::getId, id)
                        .set(StoreGoodsDO::getRecommendationTime, null)
        );
    }

    @Override
    public void platformStoreListed(Long id) {
        StoreGoodsDO distributionDO = new StoreGoodsDO();
        distributionDO.setId(id);
        distributionDO.setGoodsState(DistributionEnum.State.LISTED);
        storeGoodsDAO.updateById(distributionDO);
    }

    @Override
    public void platformStoreUnlisted(Long id) {
        StoreGoodsDO distributionDO = new StoreGoodsDO();
        distributionDO.setId(id);
        distributionDO.setGoodsState(DistributionEnum.State.PLATFORM_STORE_UNLISTED);
        storeGoodsDAO.updateById(distributionDO);
    }

    @Override
    public List<DistributionGoodsDetailRes> queryRecommendationDistributions() {
        return storeGoodsDAO.queryRandomRecommendationDistributions(RECOMMENDATION_DISTRIBUTION_LIMIT);
    }

    @Override
    public List<DistributionGoodsDetailRes> queryRecommendationStores() {
        return storeGoodsDAO.queryRecommendationStores(RECOMMENDATION_STORE_LIMIT);
    }

    @Override
    public Long queryChannelGoodsIsExist(Long goodsId, Long channelId, Long storeId) {
        return findIdByChannelScope(StoreGoodsDO::getGoodsId, goodsId, channelId, storeId);
    }

    @Override
    public Long queryChannelSkuIsExist(Long skuId, Long channelId, Long storeId) {
        return findIdByChannelScope(StoreGoodsDO::getSkuId, skuId, channelId, storeId);
    }

    @Override
    public void alterChannelGoodsSellPrice(Long goodsId, Long channelId, Long storeId, Money sellPrice) {
        storeGoodsDAO.update(
                new LambdaUpdateWrapper<StoreGoodsDO>()
                        .eq(StoreGoodsDO::getGoodsId, goodsId)
                        .eq(channelId != null, StoreGoodsDO::getChannelId, channelId)
                        .eq(storeId != null, StoreGoodsDO::getStoreId, storeId)
                        // sell_price 列 BIGINT 分, MyBatis-Plus set 走裸值, 传分整数
                        .set(StoreGoodsDO::getSellPrice, sellPrice == null ? null : sellPrice.getCent())
        );
    }

    @Override
    public void alterChannelSkuSellPrice(Long skuId, Long channelId, Long storeId, Money sellPrice) {
        storeGoodsDAO.update(
                new LambdaUpdateWrapper<StoreGoodsDO>()
                        .eq(StoreGoodsDO::getSkuId, skuId)
                        .eq(channelId != null, StoreGoodsDO::getChannelId, channelId)
                        .eq(storeId != null, StoreGoodsDO::getStoreId, storeId)
                        // sell_price 列 BIGINT 分, MyBatis-Plus set 走裸值, 传分整数
                        .set(StoreGoodsDO::getSellPrice, sellPrice == null ? null : sellPrice.getCent())
        );
    }

    @Override
    public void increaseSellNum(Long id, Integer num) {
        if (num == null) {
            return;
        }
        storeGoodsDAO.update(
                new LambdaUpdateWrapper<StoreGoodsDO>()
                        .eq(StoreGoodsDO::getId, id)
                        .setSql("sell_num = sell_num + " + num)
        );
    }

    @Override
    public Integer getStoreGoodsTotalSellNum(Long distributionId) {
        return storeGoodsDAO.getStoreGoodsTotalSellNum(distributionId);
    }

    @Override
    public List<GoodsSellNumVO> queryGoodsSellNum(List<Long> distributionIds) {
        if (CollectionUtils.isEmpty(distributionIds)) {
            return new ArrayList<>();
        }
        return storeGoodsDAO.queryGoodsSellNum(distributionIds);
    }

    @Override
    public Integer getStoreTotalSellNum(Long storeId) {
        return storeGoodsDAO.getStoreTotalSellNum(storeId);
    }

    @Override
    public Map<Long, List<DistributionRandomRPCVO>> queryRandomDistributionByStoreIdList(
            List<Long> storeIdList, Integer limitNum) {
        if (CollectionUtils.isEmpty(storeIdList)) {
            return new HashMap<>();
        }
        List<DistributionRandomRPCVO> list =
                storeGoodsDAO.queryRandomDistributionByStoreIdList(storeIdList, limitNum);
        if (CollectionUtils.isEmpty(list)) {
            return new HashMap<>();
        }
        return list.stream()
                .filter(item -> item.getStoreId() != null)
                .collect(Collectors.groupingBy(DistributionRandomRPCVO::getStoreId));
    }

    @Override
    public List<StoreGoodsDTO> getByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return toDTOList(storeGoodsDAO.selectByIds(ids));
    }

    @Override
    public List<DistributionDetailVO> queryDistributionDetailByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return storeGoodsDAO.queryDistributionDetailByIds(ids);
    }

    /**
     * 将前端传入的业务排序 code 翻译为真实排序片段
     *
     * <p>真实字段映射内包在 {@link StoreGoodsDAO.SortFieldEnum}, model 层不再暴露
     * SQL 片段; 翻译结果直接覆盖 query.sortField, 交 XML 的 {@code ${query.sortSQL}} 消费。</p>
     *
     * @param query 铺货查询
     */
    private void translateSort(DistributionsQuery query) {
        List<String> sortFields = query.getSortField();
        if (CollectionUtils.isEmpty(sortFields)) {
            return;
        }
        List<String> translated = sortFields.stream()
                .map(StoreGoodsDAO.SortFieldEnum::translateSort)
                .collect(Collectors.toList());
        sortFields.clear();
        sortFields.addAll(translated);
    }

    /**
     * 按渠道商与门店范围查铺货id
     *
     * @param field   商品维度字段(goodsId 或 skuId)
     * @param value   字段值
     * @param channelId 渠道商id, 为空则不参与过滤
     * @param storeId 门店id, 为空则不参与过滤
     * @return 铺货id, 不存在时返回 null
     */
    private Long findIdByChannelScope(
            SFunction<StoreGoodsDO, Long> field, Long value, Long channelId, Long storeId) {
        return storeGoodsDAO.selectList(
                new LambdaQueryWrapper<StoreGoodsDO>()
                        .select(StoreGoodsDO::getId)
                        .eq(field, value)
                        .eq(channelId != null, StoreGoodsDO::getChannelId, channelId)
                        .eq(storeId != null, StoreGoodsDO::getStoreId, storeId)
                        .last("limit 1")
        ).stream().findFirst().map(StoreGoodsDO::getId).orElse(null);
    }

    /**
     * DO 集合转 DTO 集合
     *
     * @param list DO 集合
     * @return DTO 集合, 恒非 null
     */
    private List<StoreGoodsDTO> toDTOList(List<StoreGoodsDO> list) {
        List<StoreGoodsDTO> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        for (StoreGoodsDO distributionDO : list) {
            result.add(toDTO(distributionDO));
        }
        return result;
    }

    /**
     * DO 转 DTO
     *
     * <p>逐字段手工映射: goodsState 两侧类型不同(DO 为枚举, DTO 为 code),
     * 且 DTO 的 upTime 在本表无对应列, 不做映射。</p>
     *
     * @param distributionDO 铺货 DO
     * @return 铺货 DTO
     */
    private StoreGoodsDTO toDTO(StoreGoodsDO distributionDO) {
        StoreGoodsDTO dto = new StoreGoodsDTO();
        dto.setId(distributionDO.getId());
        dto.setGoodsId(distributionDO.getGoodsId());
        dto.setSkuId(distributionDO.getSkuId());
        dto.setMarketId(distributionDO.getMarketId());
        dto.setDataType(distributionDO.getDataType());
        dto.setSellPrice(distributionDO.getSellPrice());
        dto.setSellNum(distributionDO.getSellNum());
        dto.setStoreId(distributionDO.getStoreId());
        dto.setGoodsState(distributionDO.getGoodsState() == null ? null : distributionDO.getGoodsState().getCode());
        dto.setChannelId(distributionDO.getChannelId());
        dto.setCreateTime(distributionDO.getCreateTime());
        dto.setUnitPrice(distributionDO.getUnitPrice());
        dto.setSupplierPrice(distributionDO.getSupplierPrice());
        dto.setGoodsInfo(distributionDO.getGoodsInfo());
        dto.setNeedUpdate(distributionDO.getNeedUpdate());
        return dto;
    }

    /**
     * DTO 转 DO
     *
     * <p>goodsState 由 code 反查枚举; DTO 的 upTime 在本表无对应列, 不参与映射。</p>
     *
     * @param dto 铺货 DTO
     * @return 铺货 DO
     */
    private StoreGoodsDO toDO(StoreGoodsDTO dto) {
        StoreGoodsDO distributionDO = new StoreGoodsDO();
        distributionDO.setId(dto.getId());
        distributionDO.setGoodsId(dto.getGoodsId());
        distributionDO.setSkuId(dto.getSkuId());
        distributionDO.setMarketId(dto.getMarketId());
        distributionDO.setDataType(dto.getDataType());
        distributionDO.setSellPrice(dto.getSellPrice());
        distributionDO.setSellNum(dto.getSellNum());
        distributionDO.setStoreId(dto.getStoreId());
        distributionDO.setGoodsState(
                dto.getGoodsState() == null ? null : DistributionEnum.State.getState(dto.getGoodsState()));
        distributionDO.setChannelId(dto.getChannelId());
        distributionDO.setUnitPrice(dto.getUnitPrice());
        distributionDO.setSupplierPrice(dto.getSupplierPrice());
        distributionDO.setGoodsInfo(dto.getGoodsInfo());
        distributionDO.setNeedUpdate(dto.getNeedUpdate());
        return distributionDO;
    }
}
