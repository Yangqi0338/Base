package com.newzkl.platform.base.biz.market.domain.distribution.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.domain.distribution.repository.IDistributionRepository;
import com.newzkl.platform.base.biz.market.domain.distribution.service.IDistributionDomain;
import com.newzkl.platform.base.biz.market.model.dto.distribution.StateNotifyDTO;
import com.newzkl.platform.base.biz.market.model.dto.distribution.StoreDistributionDTO;
import com.newzkl.platform.base.biz.market.model.event.distribution.WorkTableUpDownEventMq;
import com.newzkl.platform.base.biz.market.model.query.distribution.DistributionRandomPageQuery;
import com.newzkl.platform.base.biz.market.model.query.distribution.DistributionsPageQuery;
import com.newzkl.platform.base.biz.market.model.query.distribution.DistributionsQuery;
import com.newzkl.platform.base.biz.market.model.query.distribution.StoreDistributionQuery;
import com.newzkl.platform.base.biz.market.model.req.distribution.DistributionsBatchUpdateReq;
import com.newzkl.platform.base.biz.market.model.req.distribution.DistributionsUpdateReq;
import com.newzkl.platform.base.biz.market.model.req.distribution.GoodsStateAlterReq;
import com.newzkl.platform.base.biz.market.model.res.distribution.DistributionGoodsDetailRes;
import com.newzkl.platform.base.biz.market.model.res.distribution.DistributionGoodsListRes;
import com.newzkl.platform.base.biz.market.model.vo.market.DistributionCategoryVO;
import com.newzkl.platform.base.biz.market.model.vo.market.DistributionGoodsInfoVO;
import com.newzkl.platform.base.biz.market.model.vo.market.DistributionGoodsListOPVO;
import com.newzkl.platform.base.biz.market.model.vo.market.DistributionRandomVO;
import com.newzkl.platform.base.biz.market.model.rpc.distribution.DistributionDetailVO;
import com.newzkl.platform.base.biz.market.model.rpc.distribution.DistributionRandomRPCVO;
import com.newzkl.platform.base.biz.market.model.rpc.spu.GoodsSellNumVO;
import com.newzkl.platform.base.biz.market.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.market.model.enums.DistributionEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.biz.market.model.enums.MarketErrorCode;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
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
public class DistributionDomainImpl implements IDistributionDomain {

    private final IDistributionRepository distributionRepository;

    @Override
    public List<Long> idByQuery(DistributionsQuery req) {
        return distributionRepository.idByQuery(req);
    }

    @Override
    public DistributionsBatchUpdateReq upDownEvent(WorkTableUpDownEventMq workTableUpDownEventMq) {
        DistributionsBatchUpdateReq req = new DistributionsBatchUpdateReq();
        Integer state = DistributionEnum.State.PLATFORM_UNLISTED.getCode();
        DistributionsQuery distributionsReq = new DistributionsQuery();
        distributionsReq.setGoodsIds(workTableUpDownEventMq.getSpuIdList());
        distributionsReq.setStateNot(state);
        distributionsReq.setNeedUpdate(workTableUpDownEventMq.getNeedUpdate());
        List<Long> distributedIdList = distributionRepository.idByQuery(distributionsReq);
        if (CollUtil.isNotEmpty(distributedIdList)) {
            List<DistributionsUpdateReq> updateReqs = distributedIdList.stream().map(id -> {
                DistributionsUpdateReq spuUpdateReq = new DistributionsUpdateReq();
                spuUpdateReq.setId(id);
                spuUpdateReq.setGoodsState(state);
                return spuUpdateReq;
            }).collect(Collectors.toList());
            req.setUpdateReqs(updateReqs);
        }
        return req;
    }

    @Override
    public StateNotifyDTO batchUpdateDistributions(DistributionsBatchUpdateReq req) {
        List<StoreDistributionDTO> distributions = TransferUtils.transfers(req.getUpdateReqs(), StoreDistributionDTO::new, (source, target) -> {
            target.setNeedUpdate(CommonEnum.YesOrNo.NO.getCode());
        });
        if (CollUtil.isNotEmpty(distributions)) {
            StoreDistributionDTO spuDistribution = CollUtil.getFirst(distributions);
            Integer goodsState = spuDistribution.getGoodsState();

            if (DistributionEnum.State.LISTED.getCode().equals(goodsState)) {
                Integer sellPrice = spuDistribution.getSellPrice();
                // 若spu的sellPrice为空或者0,则找最大的sellPrice,没有报错
                if (sellPrice == null || sellPrice <= 0) {
                    OptionalInt maxSellPrice = distributions.stream().filter(it -> it.getSellPrice() != null).mapToInt(StoreDistributionDTO::getSellPrice).max();
                    if (!maxSellPrice.isPresent() || maxSellPrice.getAsInt() <= 0) {
                        throw new ScmException(BaseErrorCode.PARAM, "销售价为空或小于0");
                    } else {
                        spuDistribution.setSellPrice(maxSellPrice.getAsInt());
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
            throw new ScmException(MarketErrorCode.NOT_EXIST_OR_STATE_ERROR);
        }

        List<StoreDistributionDTO> storeDistributionList = new ArrayList<>();
        storeDistributionList.add(buildStoreDistribution(distributionGoodsInfoVO, channelId));
        // 查询商品对应的sku信息
        List<DistributionGoodsInfoVO> distributionGoodsInfos = distributionRepository.queryGoodsDistributionSku(goodsId);
        distributionGoodsInfos.forEach(x->{
            storeDistributionList.add(buildStoreDistribution(x,channelId));
        });
        // 批量保存铺货数据
        distributionRepository.batchSave(storeDistributionList);
    }

    @Override
    public List<StoreDistributionDTO> getDistributionDetailList(Long channelId, Long goodsId, boolean isSpu) {
        StoreDistributionQuery query = new StoreDistributionQuery();
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
        distributionRepository.updateDistributions(TransferUtils.transfer(req, StoreDistributionDTO::new));
    }

    private StoreDistributionDTO buildStoreDistribution(DistributionGoodsInfoVO distributionGoodsInfoVO, Long channelId){
        StoreDistributionDTO storeDistribution = TransferUtils.transfer(distributionGoodsInfoVO, StoreDistributionDTO::new);
        storeDistribution.setGoodsId(distributionGoodsInfoVO.getSpuId());
        storeDistribution.setMarketId(distributionGoodsInfoVO.getMarketId() == null ? 0 : distributionGoodsInfoVO.getMarketId());
        storeDistribution.setDataType(distributionGoodsInfoVO.getSkuId() == 0 ? 0: 1);
        storeDistribution.setStoreId(channelId);
        storeDistribution.setGoodsState(DistributionEnum.State.PENDING_LISTING.getCode());
        storeDistribution.setSupplierPrice(distributionGoodsInfoVO.getSellPrice());
        storeDistribution.setUpTime(LocalDateTime.now());
        return storeDistribution;
    }

    @Override
    public void delGoods(Long goodsId) {
        distributionRepository.delGoods(goodsId,SecurityUtils.getAccountId());
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
            throw new ScmException(BaseErrorCode.CUSTOM,
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
        List<StoreDistributionDTO> storeDistributionList = new ArrayList<>();
        // 查询商品对应的sku信息
        List<DistributionGoodsInfoVO> distributionGoodsInfos = distributionRepository.queryGoodsDistributionSku(goodsId);
        if (distributionRepository.queryChannelGoodsIsExist(goodsId, channelId, channelId) != null) {
            distributionRepository.alterChannelGoodsSellPrice(goodsId, channelId, channelId, distributionGoodsInfoVO.getSellPrice());
            distributionGoodsInfos.forEach(x -> {
                if (distributionRepository.queryChannelSkuIsExist(x.getSkuId(), channelId, channelId) != null) {
                    distributionRepository.alterChannelSkuSellPrice(x.getSkuId(), channelId, channelId, distributionGoodsInfoVO.getSellPrice());
                } else {
                    storeDistributionList.add(buildStoreDistribution(x, channelId));
                }
            });
        } else {
            storeDistributionList.add(buildStoreDistribution(distributionGoodsInfoVO, channelId));
            distributionGoodsInfos.forEach(x -> {
                storeDistributionList.add(buildStoreDistribution(x, channelId));
            });
        }
        if (!storeDistributionList.isEmpty()) {
            // 批量保存铺货数据
            distributionRepository.batchSave(storeDistributionList);
        }
    }

    @Override
    public void copyChannelDistribution(Long channelId, Long targetChannelId, Set<Long> goodsIdList) {
        distributionRepository.copyChannelDistribution(channelId, targetChannelId, goodsIdList);
    }

    @Override
    public Map<Long, List<DistributionRandomRPCVO>> queryRandomDistributionByStoreIdList(List<Long> storeIdList, Integer limitNum) {
        return distributionRepository.queryRandomDistributionByStoreIdList(storeIdList, limitNum);
    }

    @Override
    public List<StoreDistributionDTO> queryDistributionsList(StoreDistributionQuery query) {
        return distributionRepository.queryDistributionsList(query);
    }

    @Override
    public void increaseSellNum(Long skuId, Long storeId, Integer num) {
        StoreDistributionQuery query = new StoreDistributionQuery();
        query.setSkuId(skuId);
        query.setChannelId(storeId);
        query.setStoreId(storeId);
        StoreDistributionDTO storeDistributionDTO = distributionRepository.queryOneDistributions(query);
        if (storeDistributionDTO != null) {
            // 增加铺货sku销量
            distributionRepository.increaseSellNum(storeDistributionDTO.getId(), num);
            StoreDistributionQuery spuQuery = new StoreDistributionQuery();
            spuQuery.setChannelId(storeId);
            spuQuery.setStoreId(storeId);
            spuQuery.setGoodsId(storeDistributionDTO.getGoodsId());
            spuQuery.setDataType(0);
            StoreDistributionDTO spuDistribution = distributionRepository.queryOneDistributions(spuQuery);
            // 增加铺货spu销量
            distributionRepository.increaseSellNum(spuDistribution.getId(), num);
        }
    }

    @Override
    public Boolean checkGoodsIsDistributed(Long storeId, Long goodsId) {
        StoreDistributionQuery query = new StoreDistributionQuery();
        query.setGoodsId(goodsId);
        query.setChannelId(storeId);
        query.setStoreId(storeId);
        query.setDataType(0);
        StoreDistributionDTO storeDistributionDTO = distributionRepository.queryOneDistributions(query);
        return storeDistributionDTO != null;
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
    public StoreDistributionDTO queryDistributionsByCondition(StoreDistributionQuery query) {
        return distributionRepository.queryOneDistributions(query);
    }

    @Override
    public List<StoreDistributionDTO> getByIds(List<Long> ids) {
        return distributionRepository.getByIds(ids);
    }

    @Override
    public List<DistributionDetailVO> queryDistributionDetailByIds(List<Long> ids) {
        return distributionRepository.queryDistributionDetailByIds(ids);
    }

}
