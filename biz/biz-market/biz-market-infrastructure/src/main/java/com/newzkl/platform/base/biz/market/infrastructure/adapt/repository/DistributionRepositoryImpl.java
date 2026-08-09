package com.newzkl.platform.base.biz.market.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.domain.adapt.repository.DistributionRepository;
import com.newzkl.platform.base.biz.market.infrastructure.dao.StoreDistributionDAO;
import com.newzkl.platform.base.biz.market.infrastructure.entity.StoreDistributionDO;
import com.newzkl.platform.base.biz.market.model.dto.distribution.StoreDistributionDTO;
import com.newzkl.platform.base.common.ddd.model.enums.goods.DistributionEnum;
import com.newzkl.platform.base.biz.market.model.query.distribution.DistributionRandomPageQuery;
import com.newzkl.platform.base.biz.market.model.query.distribution.DistributionsPageQuery;
import com.newzkl.platform.base.biz.market.model.query.distribution.DistributionsQuery;
import com.newzkl.platform.base.biz.market.model.query.distribution.StoreDistributionQuery;
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
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@code DistributionRepository} 实现
 *
 * <p>照 {@code MarketRepositoryImpl} 风格: store_distribution 单表读写用 MyBatis-Plus
 * 条件组装, 跨表(铺货 JOIN 商品/门店)的查询委派给 {@code StoreDistributionDAO}
 * 已声明的同签名自定义方法。</p>
 *
 * <p>下列方法为 infra 能力缺口, 显式抛 {@code UnsupportedOperationException} 而非静默返回空值:</p>
 * <ul>
 *   <li>{@code delGoods} — 入参 accountId 无法定位到 store_distribution 列;
 *       该表只有 store_id / channel_id, accountId 与二者的对应关系未在现有 DO 中体现</li>
 *   <li>{@code countDeletableGoods} — 同上 accountId 归属不明; 且"销量为0"需区分真实销量与
 *       虚拟销量, 虚拟销量不在本表</li>
 *   <li>{@code queryRecommendationDistributions} — DAO 的
 *       {@code queryRandomRecommendationDistributions} 必须传 limitNum, 而接口未定义取几条,
 *       不臆造默认值</li>
 *   <li>{@code copyChannelDistribution} — 铺货行绑定 store_id, 复制到目标渠道商时新
 *       store_id 的取值规则未定义, 直接沿用源门店会把数据挂到错误门店</li>
 * </ul>
 *
 * <p>说明: 委派给 DAO 自定义方法的查询依赖 MyBatis 语句绑定(本模块暂无 XML),
 * 与 {@link MarketRepositoryImpl#queryChannelBindMarket} 现状一致 —
 * 未绑定时由 MyBatis 抛 Invalid bound statement, 不会静默成功。</p>
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

    private final StoreDistributionDAO storeDistributionDAO;

    @Override
    public List<Long> idByQuery(DistributionsQuery req) {
        return storeDistributionDAO.idByQuery(req);
    }

    @Override
    public void batchUpdateDistributions(List<StoreDistributionDTO> storeDistributionList) {
        if (CollectionUtils.isEmpty(storeDistributionList)) {
            return;
        }
        for (StoreDistributionDTO dto : storeDistributionList) {
            storeDistributionDAO.updateById(toDO(dto));
        }
    }

    @Override
    public void updateDistributions(StoreDistributionDTO storeDistributionDTO) {
        storeDistributionDAO.updateById(toDO(storeDistributionDTO));
    }

    @Override
    public Page<DistributionGoodsListRes> queryDistributionsChannel(DistributionsQuery req) {
        return storeDistributionDAO.queryDistributionsChannel(RepositorySupport.page(req), req);
    }

    @Override
    public Page<DistributionGoodsListOPVO> queryDistributionsOverallPlatform(DistributionsPageQuery query) {
        return storeDistributionDAO.queryDistributionsOverallPlatform(RepositorySupport.page(query), query);
    }

    @Override
    public List<StoreDistributionDTO> queryDistributionsList(StoreDistributionQuery query) {
        return toDTOList(storeDistributionDAO.selectList(storeDistributionDAO.buildQueryWrapper(query)));
    }

    @Override
    public StoreDistributionDTO queryOneDistributions(StoreDistributionQuery query) {
        List<StoreDistributionDO> list = storeDistributionDAO.selectList(
                storeDistributionDAO.buildQueryWrapper(query).last("limit 1")
        );
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return toDTO(list.get(0));
    }

    @Override
    public List<Long> getSpuIdsBySkuId(Long skuId) {
        return storeDistributionDAO.selectList(
                new LambdaQueryWrapper<StoreDistributionDO>()
                        .select(StoreDistributionDO::getGoodsId)
                        .eq(StoreDistributionDO::getSkuId, skuId)
        ).stream().map(StoreDistributionDO::getGoodsId).distinct().collect(Collectors.toList());
    }

    @Override
    public DistributionGoodsInfoVO queryGoodsDistribution(Long goodsId, boolean proprietary, Long channelId) {
        return storeDistributionDAO.queryGoodsDistribution(goodsId, proprietary, channelId);
    }

    @Override
    public List<DistributionGoodsInfoVO> queryGoodsDistributionSku(Long goodsId) {
        return storeDistributionDAO.queryGoodsDistributionSku(goodsId);
    }

    @Override
    public void batchSave(List<StoreDistributionDTO> storeDistributionList) {
        if (CollectionUtils.isEmpty(storeDistributionList)) {
            return;
        }
        for (StoreDistributionDTO dto : storeDistributionList) {
            storeDistributionDAO.insert(toDO(dto));
        }
    }

    @Override
    public void delGoods(Long goodsId, Long accountId) {
        throw new UnsupportedOperationException(
                "TODO[infra-gap]: accountId 无法映射到 store_distribution 列(表内只有 store_id/channel_id), "
                        + "删除范围无法确定");
    }

    @Override
    public Long countDeletableGoods(List<Long> uniqueGoodsIdList, Long accountId) {
        throw new UnsupportedOperationException(
                "TODO[infra-gap]: accountId 归属列不明, 且'销量为0'需合并本表真实销量与不在本表的虚拟销量");
    }

    @Override
    public List<DistributionCategoryVO> getDistributionGoodsCategory(Long storeId) {
        return storeDistributionDAO.queryDistributionGoodsCategory(storeId);
    }

    @Override
    public Page<DistributionRandomVO> randomSelectedGoodsPage(DistributionRandomPageQuery query) {
        return storeDistributionDAO.randomSelectedGoodsPage(RepositorySupport.page(query), query);
    }

    @Override
    public void recommendationGoods(Long id) {
        StoreDistributionDO distributionDO = new StoreDistributionDO();
        distributionDO.setId(id);
        distributionDO.setRecommendationTime(LocalDateTime.now());
        storeDistributionDAO.updateById(distributionDO);
    }

    @Override
    public void cancelRecommendationGoods(Long id) {
        // 走 update wrapper 显式置 null, updateById 会忽略 null 字段
        storeDistributionDAO.update(
                new LambdaUpdateWrapper<StoreDistributionDO>()
                        .eq(StoreDistributionDO::getId, id)
                        .set(StoreDistributionDO::getRecommendationTime, null)
        );
    }

    @Override
    public void platformStoreListed(Long id) {
        StoreDistributionDO distributionDO = new StoreDistributionDO();
        distributionDO.setId(id);
        distributionDO.setGoodsState(DistributionEnum.State.LISTED);
        storeDistributionDAO.updateById(distributionDO);
    }

    @Override
    public void platformStoreUnlisted(Long id) {
        StoreDistributionDO distributionDO = new StoreDistributionDO();
        distributionDO.setId(id);
        distributionDO.setGoodsState(DistributionEnum.State.PLATFORM_STORE_UNLISTED);
        storeDistributionDAO.updateById(distributionDO);
    }

    @Override
    public List<DistributionGoodsDetailRes> queryRecommendationDistributions() {
        throw new UnsupportedOperationException(
                "TODO[infra-gap]: DAO queryRandomRecommendationDistributions 需 limitNum, 接口未定义取数条数");
    }

    @Override
    public List<DistributionGoodsDetailRes> queryRecommendationStores() {
        return storeDistributionDAO.queryRecommendationStores(RECOMMENDATION_STORE_LIMIT);
    }

    @Override
    public Long queryChannelGoodsIsExist(Long goodsId, Long channelId, Long storeId) {
        return findIdByChannelScope(StoreDistributionDO::getGoodsId, goodsId, channelId, storeId);
    }

    @Override
    public Long queryChannelSkuIsExist(Long skuId, Long channelId, Long storeId) {
        return findIdByChannelScope(StoreDistributionDO::getSkuId, skuId, channelId, storeId);
    }

    @Override
    public void alterChannelGoodsSellPrice(Long goodsId, Long channelId, Long storeId, Money sellPrice) {
        storeDistributionDAO.update(
                new LambdaUpdateWrapper<StoreDistributionDO>()
                        .eq(StoreDistributionDO::getGoodsId, goodsId)
                        .eq(channelId != null, StoreDistributionDO::getChannelId, channelId)
                        .eq(storeId != null, StoreDistributionDO::getStoreId, storeId)
                        // sell_price 列 BIGINT 分, MyBatis-Plus set 走裸值, 传分整数
                        .set(StoreDistributionDO::getSellPrice, sellPrice == null ? null : sellPrice.getCent())
        );
    }

    @Override
    public void alterChannelSkuSellPrice(Long skuId, Long channelId, Long storeId, Money sellPrice) {
        storeDistributionDAO.update(
                new LambdaUpdateWrapper<StoreDistributionDO>()
                        .eq(StoreDistributionDO::getSkuId, skuId)
                        .eq(channelId != null, StoreDistributionDO::getChannelId, channelId)
                        .eq(storeId != null, StoreDistributionDO::getStoreId, storeId)
                        // sell_price 列 BIGINT 分, MyBatis-Plus set 走裸值, 传分整数
                        .set(StoreDistributionDO::getSellPrice, sellPrice == null ? null : sellPrice.getCent())
        );
    }

    @Override
    public void increaseSellNum(Long id, Integer num) {
        if (num == null) {
            return;
        }
        storeDistributionDAO.update(
                new LambdaUpdateWrapper<StoreDistributionDO>()
                        .eq(StoreDistributionDO::getId, id)
                        .setSql("sell_num = sell_num + " + num)
        );
    }

    @Override
    public Integer getStoreGoodsTotalSellNum(Long distributionId) {
        return storeDistributionDAO.getStoreGoodsTotalSellNum(distributionId);
    }

    @Override
    public List<GoodsSellNumVO> queryGoodsSellNum(List<Long> distributionIds) {
        if (CollectionUtils.isEmpty(distributionIds)) {
            return new ArrayList<>();
        }
        return storeDistributionDAO.queryGoodsSellNum(distributionIds);
    }

    @Override
    public Integer getStoreTotalSellNum(Long storeId) {
        return storeDistributionDAO.getStoreTotalSellNum(storeId);
    }

    @Override
    public Map<Long, List<DistributionRandomRPCVO>> queryRandomDistributionByStoreIdList(
            List<Long> storeIdList, Integer limitNum) {
        if (CollectionUtils.isEmpty(storeIdList)) {
            return new HashMap<>();
        }
        List<DistributionRandomRPCVO> list =
                storeDistributionDAO.queryRandomDistributionByStoreIdList(storeIdList, limitNum);
        if (CollectionUtils.isEmpty(list)) {
            return new HashMap<>();
        }
        return list.stream()
                .filter(item -> item.getStoreId() != null)
                .collect(Collectors.groupingBy(DistributionRandomRPCVO::getStoreId));
    }

    @Override
    public List<StoreDistributionDTO> getByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return toDTOList(storeDistributionDAO.selectByIds(ids));
    }

    @Override
    public List<DistributionDetailVO> queryDistributionDetailByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return storeDistributionDAO.queryDistributionDetailByIds(ids);
    }

    @Override
    public void copyChannelDistribution(Long channelId, Long targetChannelId, Set<Long> goodsIdList) {
        throw new UnsupportedOperationException(
                "TODO[infra-gap]: 铺货行绑定 store_id, 复制到目标渠道商时新 store_id 取值规则未定义");
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
            SFunction<StoreDistributionDO, Long> field, Long value, Long channelId, Long storeId) {
        return storeDistributionDAO.selectList(
                new LambdaQueryWrapper<StoreDistributionDO>()
                        .select(StoreDistributionDO::getId)
                        .eq(field, value)
                        .eq(channelId != null, StoreDistributionDO::getChannelId, channelId)
                        .eq(storeId != null, StoreDistributionDO::getStoreId, storeId)
                        .last("limit 1")
        ).stream().findFirst().map(StoreDistributionDO::getId).orElse(null);
    }

    /**
     * DO 集合转 DTO 集合
     *
     * @param list DO 集合
     * @return DTO 集合, 恒非 null
     */
    private List<StoreDistributionDTO> toDTOList(List<StoreDistributionDO> list) {
        List<StoreDistributionDTO> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        for (StoreDistributionDO distributionDO : list) {
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
    private StoreDistributionDTO toDTO(StoreDistributionDO distributionDO) {
        StoreDistributionDTO dto = new StoreDistributionDTO();
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
    private StoreDistributionDO toDO(StoreDistributionDTO dto) {
        StoreDistributionDO distributionDO = new StoreDistributionDO();
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
