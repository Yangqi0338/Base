package com.zkl.scm.goods.infrastructure.market.repository;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkl.scm.goods.domain.market.relation.repository.IGoodsRelationRepository;
import com.zkl.scm.goods.infrastructure.goods.dao.SpuDAO;
import com.zkl.scm.goods.infrastructure.goods.entity.SpuDO;
import com.zkl.scm.goods.infrastructure.market.dao.MarketGoodsRelationDAO;
import com.zkl.scm.goods.infrastructure.market.entity.MarketGoodsRelationDO;
import com.zkl.scm.goods.model.market.dto.relation.GoodsRelationQueryDTO;
import com.zkl.scm.goods.model.market.dto.relation.MarketGoodsRelationDTO;
import com.zkl.scm.goods.model.market.query.relation.GoodsListPageQuery;
import com.zkl.scm.goods.model.market.req.relation.PlatformQueryMarketNotAddGoodsReq;
import com.zkl.scm.goods.model.market.req.relation.UpdateGoodsRelationReq;
import com.zkl.scm.goods.model.market.vo.relation.GoodsRelationListVO;
import com.zkl.scm.goods.rpc.model.openapi.ApiChannelSpuRelationVO;
import com.zkl.scm.goods.rpc.model.relation.AlterChannelSelectorSellDataReq;
import com.zkl.scm.goods.rpc.model.relation.SpuRelevancyMarketVO;
import com.zkl.scm.infrastructure.redis.utils.RedisUtil;
import com.zkl.scm.model.enums.RedisEnum;
import com.zkl.scm.model.enums.goods.GoodsRelationEnum;
import com.zkl.scm.model.enums.goods.MarketEnum;
import com.zkl.scm.util.biz.SecurityUtils;
import com.zkl.scm.util.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author niu
 * @description: 商品关系数据仓库
 * @date 2023/12/7 16:57
 */
@Repository
@RequiredArgsConstructor
public class GoodsRelationRepositoryImpl implements IGoodsRelationRepository {

    private final MarketGoodsRelationDAO marketGoodsRelationDAO;
    private final SpuDAO spuDAO;

    @Override
    public List<SpuRelevancyMarketVO> getSpuRelevancyMarketNum(List<Long> spuIdList) {
        if (CollectionUtil.isEmpty(spuIdList)) {
            return null;
        }
        return marketGoodsRelationDAO.getSpuRelevancyMarketNum(spuIdList);
    }

    @Override
    public Page<MarketGoodsRelationDTO> queryGoodsRelationPage(GoodsRelationQueryDTO query) {
        Page<MarketGoodsRelationDO> page = marketGoodsRelationDAO.selectPage(
                query.getPage(),
                marketGoodsRelationDAO.buildQueryWrapper(query)
        );
        return TransferUtils.transferPage(page, MarketGoodsRelationDTO::new);
    }

    @Override
    public List<MarketGoodsRelationDTO> queryGoodsRelationListByDTO(GoodsRelationQueryDTO query) {
        List<MarketGoodsRelationDO> list = marketGoodsRelationDAO.selectList(
                marketGoodsRelationDAO.buildQueryWrapper(query)
        );
        if (CollectionUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return TransferUtils.transfers(list, MarketGoodsRelationDTO::new);
    }

    @Override
    public void batchSaveGoodsRelation(List<MarketGoodsRelationDTO> marketGoodsRelations) {
        marketGoodsRelationDAO.insert(TransferUtils.transfers(marketGoodsRelations, MarketGoodsRelationDO::new));
    }

    @Override
    public Page<GoodsRelationListVO> queryGoodsRelationList(GoodsListPageQuery req) {
        return marketGoodsRelationDAO.queryGoodsRelationList(req.getPage(), req);
    }

    @Override
    public Page<GoodsRelationListVO> appQueryMarketGoodList(GoodsListPageQuery req) {
        // 1. 分页查询市场商品
        Page<GoodsRelationListVO> goodsPage = marketGoodsRelationDAO.queryGoodsRelationList(req.getPage(), req);

        // 2. 不分页查询当前用户选品列表（独立构建查询对象，避免污染原分页参数）
        GoodsListPageQuery selectQuery = new GoodsListPageQuery();
        selectQuery.setUserId(req.getUserId());
        selectQuery.setRelationType(3);
        selectQuery.resetQueryList();
        Page<GoodsRelationListVO> selectPage = marketGoodsRelationDAO.queryGoodsRelationList(selectQuery.getPage(), selectQuery);

        // 3. 将选品商品的 goodsId 构建 Set，提升匹配效率
        Set<Long> selectedGoodsIds = selectPage.getRecords().stream()
                .map(GoodsRelationListVO::getGoodsId)
                .collect(Collectors.toSet());

        // 4. 标记已选品商品
        goodsPage.getRecords().forEach(vo -> {
            if (selectedGoodsIds.contains(vo.getGoodsId())) {
                vo.setChoose(true);
            }
        });

        // 5. marketId 为空时，对分页结果按 goodsId 去重
        if (ObjectUtil.isNull(req.getMarketId())) {
            List<GoodsRelationListVO> deduplicated = goodsPage.getRecords().stream()
                    .collect(Collectors.collectingAndThen(
                            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(GoodsRelationListVO::getGoodsId))),
                            ArrayList::new));
            Page<GoodsRelationListVO> deduplicatedPage = new Page<>(goodsPage.getCurrent(), goodsPage.getSize(), goodsPage.getTotal());
            deduplicatedPage.setRecords(deduplicated);
            return deduplicatedPage;
        }

        return goodsPage;
    }

    @Override
    public Page<GoodsRelationListVO> queryClientBindMarketGoodsRelationList(GoodsListPageQuery req) {
        return marketGoodsRelationDAO.queryClientBindMarketGoodsRelationList(req.getPage(), req);
    }

    @Override
    public Page<GoodsRelationListVO> platformQueryMarketNotAddGoodsList(PlatformQueryMarketNotAddGoodsReq req) {
        Page<GoodsRelationListVO> page = marketGoodsRelationDAO.platformQueryMarketNotAddGoodsList(req.getPage(), req);
        for (GoodsRelationListVO goodsRelationListVO : page.getRecords()) {
            if (ObjectUtil.equals(req.getMarketType(), MarketEnum.Type.SPECIAL.getCode()) && goodsRelationListVO.getDiscountRate() == null) {
                goodsRelationListVO.setDiscountRate(20); // 专区市场 且让利比例为空的给个默认值20%
            }
        }
        return page;
    }

    @Override
    public Page<GoodsRelationListVO> operateQueryMarketNotAddGoodsList(PlatformQueryMarketNotAddGoodsReq req) {
        return marketGoodsRelationDAO.operateQueryMarketNotAddGoodsList(req.getPage(), req);
    }

    @Override
    public Page<GoodsRelationListVO> channelMarketNotSelectedGoodsList(PlatformQueryMarketNotAddGoodsReq req) {
        req.initSortField("createTime", true);
        return marketGoodsRelationDAO.channelMarketNotSelectedGoodsList(req.getPage(), req);
    }

    @Override
    public Page<GoodsRelationListVO> channelDistributionSelectedGoodsList(PlatformQueryMarketNotAddGoodsReq query) {
        return marketGoodsRelationDAO.channelDistributionSelectedGoodsList(query.getPage(), query);
    }

    @Override
    public Page<ApiChannelSpuRelationVO> channelSpuRelationList(GoodsListPageQuery query) {
        if (ObjectUtil.isEmpty(query.getSortField())) {
            query.addDescSortField("mgr.create_time");
        }
        return TransferUtils.transferPage(marketGoodsRelationDAO.channelSpuRelationList(query.getPage(), query), ApiChannelSpuRelationVO::new);
    }

    @Override
    public void alterChannelSelectorSellData(List<AlterChannelSelectorSellDataReq> req) {
        if (CollectionUtil.isEmpty(req)) {
            return;
        }
        for (AlterChannelSelectorSellDataReq item : req) {
            marketGoodsRelationDAO.update(
                new LambdaUpdateWrapper<MarketGoodsRelationDO>()
                    .eq(MarketGoodsRelationDO::getUserId, item.getChannelId())
                    .eq(MarketGoodsRelationDO::getMarketId, item.getMarketId())
                    .eq(MarketGoodsRelationDO::getGoodsId, item.getGoodsId())
                    .eq(MarketGoodsRelationDO::getRelationType, GoodsRelationEnum.GoodsRelation.SELECT_GOODS.getRelationType())
                    .set(MarketGoodsRelationDO::getSellNum, item.getSellNum())
                    .set(MarketGoodsRelationDO::getSellAmount, item.getSellAmount())
            );
        }
    }

    @Override
    public void updateMarketGoodsLabel(UpdateGoodsRelationReq req) {
        marketGoodsRelationDAO.update(
            new LambdaUpdateWrapper<MarketGoodsRelationDO>()
                .eq(MarketGoodsRelationDO::getId, req.getId())
                .set(MarketGoodsRelationDO::getGoodsInfo, req.getGoodsInfo())
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void channelCancelSelected(Long id) {
        Long accountId = SecurityUtils.getAccountId();
        int cancelCount = marketGoodsRelationDAO.delete(
                new LambdaQueryWrapper<MarketGoodsRelationDO>()
                        .eq(MarketGoodsRelationDO::getUserId, accountId)
                        .eq(MarketGoodsRelationDO::getGoodsId, id)
                        .eq(MarketGoodsRelationDO::getRelationType, 3)
        );
        if (cancelCount > 0) {
            spuDAO.update(
                    new LambdaUpdateWrapper<SpuDO>()
                            .eq(SpuDO::getId, id)
                            .setSql("selection_num = selection_num - " + cancelCount)
            );
        }
        //刷新redis
        String relationRedisKey = RedisEnum.Key.CHANNEL_RELATION.getCode(accountId.toString());
        RedisUtil.del(relationRedisKey, id.toString());
    }

}
