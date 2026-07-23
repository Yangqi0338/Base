package com.zkl.scm.goods.infrastructure.market.repository;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkl.scm.goods.domain.market.distribution.repository.IDistributionRepository;
import com.zkl.scm.goods.infrastructure.market.dao.StoreDistributionDAO;
import com.zkl.scm.goods.infrastructure.market.entity.StoreDistributionDO;
import com.zkl.scm.goods.model.market.dto.distribution.StoreDistributionDTO;
import com.zkl.scm.goods.model.market.query.distribution.DistributionRandomPageQuery;
import com.zkl.scm.goods.model.market.query.distribution.DistributionsPageQuery;
import com.zkl.scm.goods.model.market.query.distribution.DistributionsQuery;
import com.zkl.scm.goods.model.market.query.distribution.StoreDistributionQuery;
import com.zkl.scm.goods.model.market.res.distribution.DistributionGoodsDetailRes;
import com.zkl.scm.goods.model.market.res.distribution.DistributionGoodsListRes;
import com.zkl.scm.goods.model.market.vo.market.DistributionCategoryVO;
import com.zkl.scm.goods.model.market.vo.market.DistributionGoodsInfoVO;
import com.zkl.scm.goods.model.market.vo.market.DistributionGoodsListOPVO;
import com.zkl.scm.goods.model.market.vo.market.DistributionRandomVO;
import com.zkl.scm.goods.rpc.facade.ISpuFacade;
import com.zkl.scm.goods.rpc.facade.IStoreFacade;
import com.zkl.scm.goods.rpc.model.distribution.DistributionDetailVO;
import com.zkl.scm.goods.rpc.model.distribution.DistributionRandomRPCVO;
import com.zkl.scm.goods.rpc.model.openapi.ApiSpuDetailVO;
import com.zkl.scm.goods.rpc.model.spu.GoodsSellNumVO;
import com.zkl.scm.goods.rpc.model.store.StoreRPCVO;
import com.zkl.scm.model.enums.goods.DistributionEnum;
import com.zkl.scm.model.enums.goods.SpuEnum;
import com.zkl.scm.model.exception.BaseErrorCode;
import com.zkl.scm.model.exception.ScmException;
import com.zkl.scm.util.biz.generator.SnowflakeIdAble;
import com.zkl.scm.util.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author niu
 * @description:
 * @date 2024/4/2 14:55
 */
@Repository
@RequiredArgsConstructor
public class DistributionRepositoryImpl implements IDistributionRepository {

    private final StoreDistributionDAO storeDistributionDAO;

    @DubboReference
    private IStoreFacade storeFacade;

    @DubboReference
    private ISpuFacade spuFacade;

    @Override
    public List<Long> idByQuery(DistributionsQuery req) {
        return storeDistributionDAO.idByQuery(req);
    }

    @Override
    public void batchUpdateDistributions(List<StoreDistributionDTO> storeDistributionList) {
        for (StoreDistributionDTO dto : storeDistributionList) {
            this.updateDistributions(dto);
        }
    }

    @Override
    public void updateDistributions(StoreDistributionDTO dto) {
        storeDistributionDAO.update(
                new LambdaUpdateWrapper<StoreDistributionDO>()
                        .eq(StoreDistributionDO::getId, dto.getId())
                        .ne(StoreDistributionDO::getGoodsState, 3)
                        .set(dto.getGoodsState() != null, StoreDistributionDO::getGoodsState, dto.getGoodsState())
                        .set(dto.getSellPrice() != null, StoreDistributionDO::getSellPrice, dto.getSellPrice())
                        .set(dto.getUnitPrice() != null, StoreDistributionDO::getUnitPrice, dto.getUnitPrice())
                        .set(dto.getSupplierPrice() != null, StoreDistributionDO::getSupplierPrice, dto.getSupplierPrice())
                        .set(dto.getSellNum() != null, StoreDistributionDO::getSellNum, dto.getSellNum())
                        .set(dto.getGoodsInfo() != null, StoreDistributionDO::getGoodsInfo, dto.getGoodsInfo())
                        .set(dto.getNeedUpdate() != null, StoreDistributionDO::getNeedUpdate, dto.getNeedUpdate())
                        .set(dto.getUpTime() != null, StoreDistributionDO::getUpTime, dto.getUpTime())
        );
    }

    @Override
    public Page<DistributionGoodsListRes> queryDistributionsChannel(DistributionsQuery query) {
        return storeDistributionDAO.queryDistributionsChannel(query.getPage(), query);
    }

    @Override
    public Page<DistributionGoodsListOPVO> queryDistributionsOverallPlatform(DistributionsPageQuery query) {
        return storeDistributionDAO.queryDistributionsOverallPlatform(query.getPage(), query);
    }

    @Override
    public List<StoreDistributionDTO> queryDistributionsList(StoreDistributionQuery query) {
        return TransferUtils.transfers(storeDistributionDAO.selectList(storeDistributionDAO.buildQueryWrapper(TransferUtils.transfer(query, StoreDistributionQuery::new))), StoreDistributionDTO::new);
    }

    @Override
    public StoreDistributionDTO queryOneDistributions(StoreDistributionQuery query) {
        List<StoreDistributionDO> list = storeDistributionDAO.selectList(storeDistributionDAO.buildQueryWrapper(TransferUtils.transfer(query, StoreDistributionQuery::new)));
        return CollUtil.isEmpty(list) ? null : TransferUtils.transfer(list.get(0), StoreDistributionDTO::new);
    }

    @Override
    public List<Long> getSpuIdsBySkuId(Long skuId) {
        return storeDistributionDAO.selectList(new LambdaQueryWrapper<StoreDistributionDO>().eq(StoreDistributionDO::getSkuId, skuId)).stream().map(StoreDistributionDO::getGoodsId).collect(Collectors.toList());
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
        storeDistributionDAO.insert(TransferUtils.transfers(storeDistributionList, StoreDistributionDO::new));
    }

    @Override
    public void delGoods(Long goodsId, Long accountId) {
        storeDistributionDAO.delete(new LambdaQueryWrapper<StoreDistributionDO>()
                .eq(StoreDistributionDO::getGoodsId, goodsId)
                .eq(StoreDistributionDO::getChannelId, accountId)
                .eq(StoreDistributionDO::getSellNum, 0)
                .in(StoreDistributionDO::getGoodsState, Arrays.asList(
                        DistributionEnum.State.UNLISTED.getCode(),
                        DistributionEnum.State.PENDING_LISTING.getCode()
                ))
        );
    }

    @Override
    public Long countDeletableGoods(List<Long> uniqueGoodsIdList, Long accountId) {
        return storeDistributionDAO.selectCount(new LambdaQueryWrapper<StoreDistributionDO>()
                .eq(StoreDistributionDO::getChannelId, accountId)
                .in(StoreDistributionDO::getGoodsState, Arrays.asList(
                        DistributionEnum.State.UNLISTED.getCode(),
                        DistributionEnum.State.PENDING_LISTING.getCode()
                ))
                .eq(StoreDistributionDO::getSellNum, 0)
                .eq(StoreDistributionDO::getDataType, 0)
                .in(StoreDistributionDO::getGoodsId, uniqueGoodsIdList)
        );
    }

    @Override
    public List<DistributionCategoryVO> getDistributionGoodsCategory(Long storeId) {
        return storeDistributionDAO.queryDistributionGoodsCategory(storeId);
    }

    @Override
    public Page<DistributionRandomVO> randomSelectedGoodsPage(DistributionRandomPageQuery query) {
        if (CollUtil.isEmpty(query.getSortField()) && query.getSellPriceL() != null && query.getSellPriceR() != null) {
            query.addSortField("RAND()");
        }
        Page<DistributionRandomVO> page = storeDistributionDAO.randomSelectedGoodsPage(query.getPage(), query);
        List<DistributionRandomVO> list = page.getRecords();
        if (CollUtil.isNotEmpty(list)) {
            List<Long> collect = list.stream().map(DistributionRandomVO::getStoreId).collect(Collectors.toList());
            List<StoreRPCVO> storeRPCVOS = storeFacade.batchQueryStoreInfo(collect);
            Map<Long, StoreRPCVO> map = storeRPCVOS.stream().collect(Collectors.toMap(StoreRPCVO::getId, item -> item));
            list.forEach(x -> {
                if (map.containsKey(x.getStoreId())) {
                    x.setStoreName(map.get(x.getStoreId()).getName());
                }
            });
        }
        return page;
    }

    @Override
    public void recommendationGoods(Long id) {
        storeDistributionDAO.update(
                new LambdaUpdateWrapper<StoreDistributionDO>()
                        .eq(StoreDistributionDO::getId, id)
                        .set(StoreDistributionDO::getRecommendationTime, LocalDateTime.now())
        );
    }

    @Override
    public void cancelRecommendationGoods(Long id) {
        storeDistributionDAO.update(
                new LambdaUpdateWrapper<StoreDistributionDO>()
                        .eq(StoreDistributionDO::getId, id)
                        .set(StoreDistributionDO::getRecommendationTime, null)
        );
    }

    @Override
    public void platformStoreListed(Long id) {
        StoreDistributionDO distribution = storeDistributionDAO.selectById(id);
        if (distribution == null) {
            throw new ScmException(BaseErrorCode.CUSTOM, "铺货记录不存在");
        }
        ApiSpuDetailVO spuDetailVO = spuFacade.apiSpuDetail(null, distribution.getGoodsId());
        // 校验是否平台下架
        if (spuDetailVO == null || SpuEnum.State.PLATFORM_DOWN.getCode().equals(spuDetailVO.getSpu().getState())) {
            throw new ScmException(BaseErrorCode.CUSTOM, "商品已平台下架，无法上架");
        }
        // 更新状态为1（下架状态，恢复正常）
        storeDistributionDAO.update(
                new LambdaUpdateWrapper<StoreDistributionDO>()
                        .eq(StoreDistributionDO::getId, id)
                        .set(StoreDistributionDO::getGoodsState, DistributionEnum.State.UNLISTED.getCode())
        );
    }

    @Override
    public void platformStoreUnlisted(Long id) {
        // 更新状态为3（平台门店下架）
        storeDistributionDAO.update(
                new LambdaUpdateWrapper<StoreDistributionDO>()
                        .eq(StoreDistributionDO::getId, id)
                        .set(StoreDistributionDO::getGoodsState, DistributionEnum.State.PLATFORM_STORE_UNLISTED.getCode())
        );
    }

    @Override
    public List<DistributionGoodsDetailRes> queryRecommendationDistributions() {
        return storeDistributionDAO.queryRandomRecommendationDistributions(10);
    }

    @Override
    public List<DistributionGoodsDetailRes> queryRecommendationStores() {
        return storeDistributionDAO.queryRecommendationStores(10);
    }

    @Override
    public Long queryChannelGoodsIsExist(Long goodsId, Long channelId, Long storeId) {
        StoreDistributionDO record = storeDistributionDAO.selectOne(
                new LambdaQueryWrapper<StoreDistributionDO>()
                        .eq(StoreDistributionDO::getChannelId, channelId)
                        .eq(StoreDistributionDO::getStoreId, storeId)
                        .eq(StoreDistributionDO::getGoodsId, goodsId)
                        .select(StoreDistributionDO::getId)
        );
        return record == null ? null : record.getId();
    }

    @Override
    public Long queryChannelSkuIsExist(Long skuId, Long channelId, Long storeId) {
        StoreDistributionDO record = storeDistributionDAO.selectOne(
                new LambdaQueryWrapper<StoreDistributionDO>()
                        .eq(StoreDistributionDO::getChannelId, channelId)
                        .eq(StoreDistributionDO::getStoreId, storeId)
                        .eq(StoreDistributionDO::getSkuId, skuId)
                        .select(StoreDistributionDO::getId)
        );
        return record == null ? null : record.getId();
    }

    @Override
    public void alterChannelGoodsSellPrice(Long goodsId, Long channelId, Long storeId, Integer sellPrice) {
        storeDistributionDAO.update(
                new LambdaUpdateWrapper<StoreDistributionDO>()
                        .eq(StoreDistributionDO::getChannelId, channelId)
                        .eq(StoreDistributionDO::getStoreId, storeId)
                        .eq(StoreDistributionDO::getGoodsId, goodsId)
                        .set(StoreDistributionDO::getSellPrice, sellPrice)
        );
    }

    @Override
    public void alterChannelSkuSellPrice(Long skuId, Long channelId, Long storeId, Integer sellPrice) {
        storeDistributionDAO.update(
                new LambdaUpdateWrapper<StoreDistributionDO>()
                        .eq(StoreDistributionDO::getChannelId, channelId)
                        .eq(StoreDistributionDO::getStoreId, storeId)
                        .eq(StoreDistributionDO::getSkuId, skuId)
                        .set(StoreDistributionDO::getSellPrice, sellPrice)
        );
    }

    @Override
    public void increaseSellNum(Long id, Integer num) {
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
        return storeDistributionDAO.queryGoodsSellNum(distributionIds);
    }

    @Override
    public Integer getStoreTotalSellNum(Long storeId) {
        return storeDistributionDAO.getStoreTotalSellNum(storeId);
    }

    @Override
    public Map<Long, List<DistributionRandomRPCVO>> queryRandomDistributionByStoreIdList(List<Long> storeIdList, Integer limitNum) {
        List<DistributionRandomRPCVO> list = storeDistributionDAO.queryRandomDistributionByStoreIdList(storeIdList, limitNum);
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.groupingBy(DistributionRandomRPCVO::getStoreId));
    }

    @Override
    public List<StoreDistributionDTO> getByIds(List<Long> ids) {
        List<StoreDistributionDO> storeDistributionDOS = storeDistributionDAO.selectList(new LambdaQueryWrapper<StoreDistributionDO>()
                .in(StoreDistributionDO::getId, ids)
        );
        return TransferUtils.transfers(storeDistributionDOS, StoreDistributionDTO::new);
    }

    @Override
    public List<DistributionDetailVO> queryDistributionDetailByIds(List<Long> ids) {
        return storeDistributionDAO.queryDistributionDetailByIds(ids);
    }

    @Override
    public void copyChannelDistribution(Long channelId, Long targetChannelId, Set<Long> goodsIdList) {
        // 查询源渠道商的铺货数据
        List<StoreDistributionDO> storeDistributionList = storeDistributionDAO.selectList(
                new LambdaQueryWrapper<StoreDistributionDO>()
                        .eq(StoreDistributionDO::getChannelId, channelId)
                        .eq(StoreDistributionDO::getStoreId, channelId)
                        .in(StoreDistributionDO::getGoodsId, goodsIdList)
        );
        storeDistributionList.forEach(x -> {
            x.setId(SnowflakeIdAble.getSnowflakeId());
            x.setChannelId(targetChannelId);
            x.setStoreId(targetChannelId);
            x.setMarketId(DistributionEnum.Source.MODEL_SHOP.getCode());
        });
        // 删除目标渠道商原有铺货数据
        storeDistributionDAO.delete(
                new LambdaQueryWrapper<StoreDistributionDO>()
                        .eq(StoreDistributionDO::getChannelId, targetChannelId)
                        .eq(StoreDistributionDO::getStoreId, targetChannelId)
                        .in(StoreDistributionDO::getGoodsId, goodsIdList)
        );
        // 批量保存铺货数据
        storeDistributionDAO.insert(storeDistributionList);
    }

}
