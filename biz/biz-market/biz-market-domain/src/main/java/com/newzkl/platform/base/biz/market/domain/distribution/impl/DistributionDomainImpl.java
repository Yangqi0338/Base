package com.newzkl.platform.base.biz.market.domain.distribution.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.domain.adapt.repository.StoreGoodsRepository;
import com.newzkl.platform.base.biz.market.domain.distribution.DistributionDomain;
import com.newzkl.platform.base.biz.market.model.dto.distribution.StateNotifyDTO;
import com.newzkl.platform.base.biz.market.model.dto.distribution.StoreGoodsDTO;
import com.newzkl.platform.base.biz.market.facade.model.UpDownReq;
import com.newzkl.platform.base.biz.market.model.query.distribution.DistributionRandomPageQuery;
import com.newzkl.platform.base.biz.market.model.query.distribution.DistributionsPageQuery;
import com.newzkl.platform.base.biz.market.model.query.distribution.DistributionsQuery;
import com.newzkl.platform.base.biz.market.model.query.distribution.StoreGoodsQuery;
import com.newzkl.platform.base.biz.market.model.req.distribution.DistributionsBatchUpdateReq;
import com.newzkl.platform.base.biz.market.model.req.distribution.DistributionsUpdateReq;
import com.newzkl.platform.base.biz.market.model.req.distribution.GoodsStateAlterReq;
import com.newzkl.platform.base.biz.market.model.res.distribution.DistributionGoodsDetailRes;
import com.newzkl.platform.base.biz.market.model.res.distribution.DistributionGoodsListRes;
import com.newzkl.platform.base.biz.market.model.vo.market.DistributionCategoryVO;
import com.newzkl.platform.base.biz.market.model.vo.market.DistributionGoodsInfoVO;
import com.newzkl.platform.base.biz.market.model.vo.market.DistributionGoodsListOPVO;
import com.newzkl.platform.base.biz.market.model.vo.market.DistributionRandomVO;
import com.newzkl.platform.base.common.ddd.facade.DistributionDetailVO;
import com.newzkl.platform.base.biz.market.model.rpc.distribution.DistributionRandomRPCVO;
import com.newzkl.platform.base.common.ddd.facade.GoodsSellNumVO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.StoreGoodsEnum;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.model.constant.MarketErrorCode;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author niu
 * @description:
 * @date 2024/4/2 14:01
 */
@Service
@RequiredArgsConstructor
public class DistributionDomainImpl implements DistributionDomain {

    private final StoreGoodsRepository distributionRepository;

    @Override
    public void upDownEvent(UpDownReq upDownReq) {
        StoreGoodsEnum.State state = upDownReq.getEnable() == SpuEnum.State.PLATFORM_DOWN ?
                StoreGoodsEnum.State.PLATFORM_UNLISTED : StoreGoodsEnum.State.LISTED;
        StoreGoodsDTO storeGoodsDTO = new StoreGoodsDTO();
        storeGoodsDTO.setGoodsState(state);

        StoreGoodsQuery storeGoodsQuery = new StoreGoodsQuery();
        storeGoodsQuery.setStateNot(state);
        storeGoodsQuery.setGoodsIds(upDownReq.getSpuIdList());
        int effectRows = distributionRepository.update(storeGoodsDTO, storeGoodsQuery);
    }

    @Override
    public StateNotifyDTO batchUpdateDistributions(DistributionsBatchUpdateReq req) {
        List<StoreGoodsDTO> distributions = TransferUtils.transfers(req.getUpdateReqs(), StoreGoodsDTO::new, (source, target) -> {
            target.setNeedUpdate(CommonEnum.YesOrNo.NO.getCode());
        });
        if (CollUtil.isNotEmpty(distributions)) {
            StoreGoodsDTO spuDistribution = CollUtil.getFirst(distributions);
            StoreGoodsEnum.State goodsState = spuDistribution.getGoodsState();

            if (StoreGoodsEnum.State.LISTED == goodsState) {
                // 售价 (Money 分); spu 售价为空或非正时, 取子项中最大售价兜底
                Money sellPrice = spuDistribution.getSellPrice();
                if (sellPrice == null || !sellPrice.greaterThanZero()) {
                    Money maxSellPrice = distributions.stream()
                            .map(StoreGoodsDTO::getSellPrice)
                            .filter(Objects::nonNull)
                            .max(Comparator.comparingLong(Money::getCent))
                            .orElse(null);
                    if (maxSellPrice == null || !maxSellPrice.greaterThanZero()) {
                        throw new PlatformException(BaseErrorCode.PARAM, "销售价为空或小于0");
                    } else {
                        spuDistribution.setSellPrice(maxSellPrice);
                    }
                }
                spuDistribution.setUpTime(LocalDateTime.now());
                distributionRepository.batchUpdateDistributions(distributions);
            }

            StateNotifyDTO stateNotifyDTO = new StateNotifyDTO();
            stateNotifyDTO.setChannelId(req.getChannelId());
            stateNotifyDTO.setDistributionId(spuDistribution.getId());
            stateNotifyDTO.setGoodsId(spuDistribution.getGoodsId());
            stateNotifyDTO.setGoodsState(goodsState);
            return stateNotifyDTO;
        }
        return null;
    }

    @Override
    public Page<DistributionGoodsListRes> queryDistributionsChannel(DistributionsQuery req) {
        return distributionRepository.queryDistributionsChannel(req);
    }

    @Override
    public Page<DistributionGoodsListOPVO> queryDistributionsOverallPlatform(DistributionsPageQuery query) {
        return TransferUtils.transferPage(distributionRepository.queryDistributionsOverallPlatform(query), DistributionGoodsListOPVO::new);
    }

    @Override
    public List<Long> getSpuIdsBySkuId(Long skuId) {
        return distributionRepository.getSpuIdsBySkuId(skuId);
    }

    @Override
    public void goodsDistribution(Long channelId, Long goodsId, boolean proprietary) {
        // 查询商品信息，若不是自营商品，必须有选品数据支持,若查不到市场id则判定为未选品
        DistributionGoodsInfoVO distributionGoodsInfoVO = distributionRepository.queryGoodsDistribution(goodsId, proprietary, channelId);
        if (distributionGoodsInfoVO == null) {
            throw new PlatformException(MarketErrorCode.NOT_EXIST_OR_STATE_ERROR);
        }

        List<StoreGoodsDTO> storeGoodsList = new ArrayList<>();
        storeGoodsList.add(buildStoreGoods(distributionGoodsInfoVO, channelId));
        // 查询商品对应的sku信息
        List<DistributionGoodsInfoVO> distributionGoodsInfos = distributionRepository.queryGoodsDistributionSku(goodsId);
        distributionGoodsInfos.forEach(x->{
            storeGoodsList.add(buildStoreGoods(x,channelId));
        });
        // 批量保存铺货数据
        distributionRepository.batchSave(storeGoodsList);
    }

    @Override
    public List<StoreGoodsDTO> getDistributionDetailList(Long channelId, Long goodsId, boolean isSpu) {
        StoreGoodsQuery query = new StoreGoodsQuery();
        query.setChannelId(channelId);
        query.setGoodsId(goodsId);
        if(isSpu){
            query.setDataType(0);
        }else {
            query.setDataType(1);
        }
        return distributionRepository.queryDistributionsList(query);
    }

    @Override
    public void alterGoodsState(GoodsStateAlterReq req) {
        distributionRepository.updateDistributions(TransferUtils.transfer(req, StoreGoodsDTO::new));
    }

    private StoreGoodsDTO buildStoreGoods(DistributionGoodsInfoVO distributionGoodsInfoVO, Long channelId){
        StoreGoodsDTO storeGoods = TransferUtils.transfer(distributionGoodsInfoVO, StoreGoodsDTO::new);
        storeGoods.setGoodsId(distributionGoodsInfoVO.getSpuId());
        storeGoods.setMarketId(distributionGoodsInfoVO.getMarketId() == null ? 0 : distributionGoodsInfoVO.getMarketId());
        storeGoods.setDataType(distributionGoodsInfoVO.getSkuId() == 0 ? 0: 1);
        storeGoods.setStoreId(channelId);
        storeGoods.setGoodsState(StoreGoodsEnum.State.PENDING_LISTING);
        storeGoods.setSupplierPrice(distributionGoodsInfoVO.getSellPrice());
        storeGoods.setUpTime(LocalDateTime.now());
        return storeGoods;
    }

    @Override
    public void delGoods(Long goodsId) {
        distributionRepository.delGoods(goodsId,SecurityUtils.getAccountId());
    }

    @Override
    public void recoverGoods(Long goodsId) {
        distributionRepository.recoverGoods(goodsId, SecurityUtils.getAccountId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelGoods(List<Long> goodsIdList) {
        if (CollUtil.isEmpty(goodsIdList)) {
            return;
        }
        // 使用Set去重
        Set<Long> goodsIdSet = new HashSet<>(goodsIdList);
        List<Long> uniqueGoodsIdList = new ArrayList<>(goodsIdSet);
        Long accountId = SecurityUtils.getAccountId();

        // 查询符合删除条件的数量
        Long deletableCount = distributionRepository.countDeletableGoods(uniqueGoodsIdList, accountId);

        // 校验数据是否全部存在且符合删除条件
        if (!deletableCount.equals((long) uniqueGoodsIdList.size())) {
            throw new PlatformException(BaseErrorCode.CUSTOM,
                    "部分商品不存在或不符合删除条件(待上架或下架且销量为0)");
        }
        uniqueGoodsIdList.forEach(goodsId -> {
            distributionRepository.delGoods(goodsId, accountId);
        });

    }

    @Override
    public Page<DistributionRandomVO> randomSelectedGoodsPage(DistributionRandomPageQuery query) {
        return distributionRepository.randomSelectedGoodsPage(query);
    }

    @Override
    public List<DistributionCategoryVO> getDistributionGoodsCategory(Long storeId) {
        return distributionRepository.getDistributionGoodsCategory(storeId);
    }

    @Override
    public void recommendationGoods(Long id) {
        distributionRepository.recommendationGoods(id);
    }

    @Override
    public void cancelRecommendationGoods(Long id) {
        distributionRepository.cancelRecommendationGoods(id);
    }

    @Override
    public void platformStoreListed(Long id) {
        distributionRepository.platformStoreListed(id);
    }

    @Override
    public void platformStoreUnlisted(Long id) {
        distributionRepository.platformStoreUnlisted(id);
    }

    @Override
    public List<DistributionGoodsDetailRes> queryRecommendationDistributions() {
        return distributionRepository.queryRecommendationDistributions();
    }

    @Override
    public List<DistributionGoodsDetailRes> queryRecommendationStores() {
        return distributionRepository.queryRecommendationStores();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void alterDistribution(Long channelId, Long goodsId) {
        // 查询商品信息，若不是自营商品，必须有选品数据支持,若查不到市场id则判定为未选品
        DistributionGoodsInfoVO distributionGoodsInfoVO = distributionRepository.queryGoodsDistribution(goodsId, true, channelId);
        if (distributionGoodsInfoVO == null || distributionGoodsInfoVO.getMarketId() != null) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        List<StoreGoodsDTO> storeGoodsList = new ArrayList<>();
        // 查询商品对应的sku信息
        List<DistributionGoodsInfoVO> distributionGoodsInfos = distributionRepository.queryGoodsDistributionSku(goodsId);
        if (distributionRepository.queryChannelGoodsIsExist(goodsId, channelId, channelId) != null) {
            distributionRepository.alterChannelGoodsSellPrice(goodsId, channelId, channelId, distributionGoodsInfoVO.getSellPrice());
            distributionGoodsInfos.forEach(x -> {
                if (distributionRepository.queryChannelSkuIsExist(x.getSkuId(), channelId, channelId) != null) {
                    distributionRepository.alterChannelSkuSellPrice(x.getSkuId(), channelId, channelId, distributionGoodsInfoVO.getSellPrice());
                } else {
                    storeGoodsList.add(buildStoreGoods(x, channelId));
                }
            });
        } else {
            storeGoodsList.add(buildStoreGoods(distributionGoodsInfoVO, channelId));
            distributionGoodsInfos.forEach(x -> {
                storeGoodsList.add(buildStoreGoods(x, channelId));
            });
        }
        if (!storeGoodsList.isEmpty()) {
            // 批量保存铺货数据
            distributionRepository.batchSave(storeGoodsList);
        }
    }

    @Override
    public Map<Long, List<DistributionRandomRPCVO>> queryRandomDistributionByStoreIdList(List<Long> storeIdList, Integer limitNum) {
        return distributionRepository.queryRandomDistributionByStoreIdList(storeIdList, limitNum);
    }

    @Override
    public List<StoreGoodsDTO> queryDistributionsList(StoreGoodsQuery query) {
        return distributionRepository.queryDistributionsList(query);
    }

    @Override
    public void increaseSellNum(Long skuId, Long storeId, Integer num) {
        StoreGoodsQuery query = new StoreGoodsQuery();
        query.setSkuId(skuId);
        query.setChannelId(storeId);
        query.setStoreId(storeId);
        StoreGoodsDTO storeGoodsDTO = distributionRepository.queryOneDistributions(query);
        if (storeGoodsDTO != null) {
            // 增加铺货sku销量
            distributionRepository.increaseSellNum(storeGoodsDTO.getId(), num);
            StoreGoodsQuery spuQuery = new StoreGoodsQuery();
            spuQuery.setChannelId(storeId);
            spuQuery.setStoreId(storeId);
            spuQuery.setGoodsId(storeGoodsDTO.getGoodsId());
            spuQuery.setDataType(0);
            StoreGoodsDTO spuDistribution = distributionRepository.queryOneDistributions(spuQuery);
            // 增加铺货spu销量
            distributionRepository.increaseSellNum(spuDistribution.getId(), num);
        }
    }

    @Override
    public Boolean checkGoodsIsDistributed(Long storeId, Long goodsId) {
        StoreGoodsQuery query = new StoreGoodsQuery();
        query.setGoodsId(goodsId);
        query.setChannelId(storeId);
        query.setStoreId(storeId);
        query.setDataType(0);
        StoreGoodsDTO storeGoodsDTO = distributionRepository.queryOneDistributions(query);
        return storeGoodsDTO != null;
    }

    @Override
    public Integer getStoreGoodsTotalSellNum(Long distributionId) {
        return distributionRepository.getStoreGoodsTotalSellNum(distributionId);
    }

    @Override
    public List<GoodsSellNumVO> queryGoodsSellNum(List<Long> distributionIds) {
        return distributionRepository.queryGoodsSellNum(distributionIds);
    }

    @Override
    public Integer getStoreTotalSellNum(Long storeId) {
        return distributionRepository.getStoreTotalSellNum(storeId);
    }

    @Override
    public StoreGoodsDTO queryDistributionsByCondition(StoreGoodsQuery query) {
        return distributionRepository.queryOneDistributions(query);
    }

    @Override
    public List<StoreGoodsDTO> getByIds(List<Long> ids) {
        return distributionRepository.getByIds(ids);
    }

}
