package com.newzkl.platform.base.biz.market.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.domain.market.repository.MarketRepository;
import com.newzkl.platform.base.biz.market.infrastructure.dao.MarketBindDAO;
import com.newzkl.platform.base.biz.market.infrastructure.dao.MarketCategoryDAO;
import com.newzkl.platform.base.biz.market.infrastructure.dao.MarketDAO;
import com.newzkl.platform.base.biz.market.infrastructure.entity.MarketBindDO;
import com.newzkl.platform.base.biz.market.infrastructure.entity.MarketDO;
import com.newzkl.platform.base.biz.market.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.market.model.enums.MarketTypeEnum;
import com.newzkl.platform.base.biz.market.model.dto.market.MarketBindDTO;
import com.newzkl.platform.base.biz.market.model.dto.market.MarketDTO;
import com.newzkl.platform.base.biz.market.model.query.market.AppBindMarketGoodsPageQuery;
import com.newzkl.platform.base.biz.market.model.query.market.ChannelMarketPageQuery;
import com.newzkl.platform.base.biz.market.model.query.market.MarketPageQuery;
import com.newzkl.platform.base.biz.market.model.req.market.BindMarketListReq;
import com.newzkl.platform.base.biz.market.model.req.market.ClientBindMarketReq;
import com.newzkl.platform.base.biz.market.model.req.market.MarketUserReq;
import com.newzkl.platform.base.biz.market.model.req.market.UpdateMarketDataReq;
import com.newzkl.platform.base.biz.market.model.vo.market.*;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.base.biz.market.model.enums.MarketEnum;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MarketRepositoryImpl implements MarketRepository {

    private static final String CATEGORY_CACHE_KEY = "category:";

    private final MarketBindDAO marketBindDAO;
    private final MarketCategoryDAO marketCategoryDAO;
    private final MarketDAO marketDAO;

    @Override
    public Page<AppBindMarketVO> queryChannelBindMarket(ChannelMarketPageQuery query) {
        return marketBindDAO.queryChannelBindMarket(RepositorySupport.page(query),query);
    }

    @Override
    public Page<AppBindMarketGoodsVO> queryChannelBindMarketGoods(AppBindMarketGoodsPageQuery query) {
        return marketBindDAO.queryChannelBindMarketGoods(RepositorySupport.page(query),query);
    }

    @Override
    public Page<MarketVO> queryMarketList(MarketPageQuery query) {
        // subBindUser 不为空时，先查 market_bind 得到符合条件的 market_id 列表
        List<Long> subBindMarketIds = null;
        if (query.getSubBindUser() != null) {
            List<Long> ids = marketBindDAO.selectList(
                    new LambdaQueryWrapper<MarketBindDO>()
                            .eq(MarketBindDO::getUserId, query.getSubBindUser())
                            .eq(query.getSubBindType() != null, MarketBindDO::getBindType, query.getSubBindType())
                            .select(MarketBindDO::getMarketId)
            ).stream().map(MarketBindDO::getMarketId).collect(Collectors.toList());
            // 无匹配绑定记录则直接返回空页
            if (CollectionUtils.isEmpty(ids)) {
                return new Page<>(query.getPageNo(), query.getPageSize());
            }
            subBindMarketIds = ids;
        }

        final List<Long> finalSubBindMarketIds = subBindMarketIds;
        Page<MarketDO> page = marketDAO.selectPage(
                RepositorySupport.page(query),
                new LambdaQueryWrapper<MarketDO>()
                        .eq(query.getMarketLevel() != null, MarketDO::getMarketLevel, query.getMarketLevel())
                        .eq(query.getClientId() != null && query.getClientId() > 0, MarketDO::getClientId, query.getClientId())
                        .like(query.getMarketName() != null, MarketDO::getMarketName, query.getMarketName())
                        .in(finalSubBindMarketIds != null, MarketDO::getId, finalSubBindMarketIds != null ? finalSubBindMarketIds : Collections.emptyList())
                        .orderByDesc(MarketDO::getId)
        );
        return TransferUtils.transferPage(page, MarketVO::new);
    }

    @Override
    public MarketVO queryMarket(Long marketId) {
        return TransferUtils.transfer(
                marketDAO.selectById(marketId),
                MarketVO::new
        );
    }

    @Override
    public void saveMarket(MarketDTO marketDTO) {
        marketBindDAO.insert(TransferUtils.transfer(marketDTO, MarketBindDO::new));
    }

    @Override
    public void updateMarket(MarketDTO marketDTO) {
        marketBindDAO.updateById(TransferUtils.transfer(marketDTO, MarketBindDO::new));
    }

    @Override
    public Long queryAccountIsBindMarket(ClientBindMarketReq req) {
        return marketBindDAO.selectList(
                new LambdaQueryWrapper<MarketBindDO>()
                        .eq(MarketBindDO::getMarketId, req.getMarketId())
                        .eq(MarketBindDO::getBindType, req.getBindType())
                        .eq(MarketBindDO::getUserId, req.getUserId())
        ).stream().findFirst().map(MarketBindDO::getId).orElse(null);
    }

    @Override
    public void updateMarketBind(MarketBindDTO marketBindDTO) {
        marketBindDAO.updateById(TransferUtils.transfer(marketBindDTO, MarketBindDO::new));
    }

    @Override
    public void createMarketBind(MarketBindDTO marketBindDTO) {
        marketBindDAO.insert(TransferUtils.transfer(marketBindDTO, MarketBindDO::new));
    }

    @Override
    public void alterMarketData(UpdateMarketDataReq req) {
        MarketEnum.NumType type = req.getType();
        Integer alterNum = req.getAlterNum();
        marketDAO.update(
                new LambdaUpdateWrapper<MarketDO>()
                        .eq(MarketDO::getId, req.getMarketId())
                        .setSql(type == MarketEnum.NumType.SUB_BIND_NUM, "sub_bind_num = sub_bind_num + " + alterNum)
                        .setSql(type == MarketEnum.NumType.SELL_NUM, "sell_num = sell_num + " + alterNum)
                        .setSql(type == MarketEnum.NumType.SELL_AMOUNT, "sell_amount = sell_amount + " + alterNum)
                        .setSql(type == MarketEnum.NumType.GOODS_NUM, "goods_num = goods_num + " + alterNum)
        );
    }

    @Override
    public void deBindMarket(Long id) {
        MarketBindDO marketBind = new MarketBindDO();
        marketBind.setId(id);
        marketBind.setState(CommonEnum.YesOrNo.NO);
        marketBind.setDebindTime(LocalDateTime.now());
        marketBindDAO.updateById(marketBind);
    }

    @Override
    public List<BindMarketVO> queryBindMarket(BindMarketListReq req) {
        // 1. 查询符合条件的绑定记录，获取 market_id 列表
        List<MarketBindDO> binds = marketBindDAO.selectList(
                new LambdaQueryWrapper<MarketBindDO>()
                        .eq(MarketBindDO::getUserId, req.getClientId())
                        .eq(MarketBindDO::getState, 1)
                        .eq(req.getBindType() != null, MarketBindDO::getBindType, req.getBindType())
        );
        if (CollectionUtils.isEmpty(binds)) {
            return Collections.emptyList();
        }
        // market_id -> bind_id 映射（用于返回 id = mb.market_id）
        Map<Long, Long> marketIdToBindId = binds.stream()
                .collect(Collectors.toMap(MarketBindDO::getMarketId, MarketBindDO::getId, (a, b) -> a));

        // 2. 根据 market_id 查询市场信息，支持名称和分类过滤
        List<MarketDO> markets = marketDAO.selectList(
                new LambdaQueryWrapper<MarketDO>()
                        .in(MarketDO::getId, marketIdToBindId.keySet())
                        .like(req.getMarketName() != null, MarketDO::getMarketName, req.getMarketName())
                        .eq(req.getCategoryId() != null, MarketDO::getCategoryId, req.getCategoryId())
        );
        if (CollectionUtils.isEmpty(markets)) {
            return Collections.emptyList();
        }

        // 3. 动态排序
        Comparator<MarketDO> comparator = null;
        if (req.getTimeSort() != null) {
            comparator = req.getTimeSort() == 1
                    ? Comparator.comparing(MarketDO::getCreateTime, Comparator.nullsLast(Comparator.reverseOrder()))
                    : Comparator.comparing(MarketDO::getCreateTime, Comparator.nullsLast(Comparator.naturalOrder()));
        } else if (req.getNumSort() != null) {
            comparator = req.getNumSort() == 1
                    ? Comparator.comparingInt((MarketDO m) -> m.getGoodsNum() == null ? 0 : m.getGoodsNum()).reversed()
                    : Comparator.comparingInt(m -> (m.getGoodsNum() == null ? 0 : m.getGoodsNum()));
        } else {
            // 默认按创建时间降序
            comparator = Comparator.comparing(MarketDO::getCreateTime, Comparator.nullsLast(Comparator.reverseOrder()));
        }
        markets.sort(comparator);

        // 4. 转换为 VO
        return markets.stream().map(m -> {
            BindMarketVO vo = new BindMarketVO();
            vo.setId(marketIdToBindId.get(m.getId()));
            vo.setMarketName(m.getMarketName());
            vo.setMarketLogo(m.getMarketLogo());
            vo.setMarketDesc(m.getMarketDesc());
            vo.setGoodsNum(m.getGoodsNum());
            vo.setSubBindNum(m.getSubBindNum());
            vo.setMarketType(m.getMarketType() == null ? null : m.getMarketType().getCode());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<MarketUserVO> queryMarketUser(MarketUserReq req) {
        return marketBindDAO.selectList(
                new LambdaQueryWrapper<MarketBindDO>()
                        .eq(MarketBindDO::getMarketId, req.getMarketId())
                        .eq(MarketBindDO::getBindType, req.getBindType())
                        .eq(MarketBindDO::getState, 1)
                        .in(!CollectionUtils.isEmpty(req.getUserIds()), MarketBindDO::getUserId, req.getUserIds())
                        .orderByDesc(MarketBindDO::getId)
        ).stream().map(bind -> {
            MarketUserVO vo = new MarketUserVO();
            vo.setId(bind.getId());
            vo.setClientId(bind.getUserId());
            vo.setUserName(bind.getUserName());
            vo.setBindTime(bind.getCreateTime());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<MarketGoodsCategoryVO> queryMarketGoodsCategory(Long marketId, Long userId) {
        String key = marketId != null ? CATEGORY_CACHE_KEY + marketId : CATEGORY_CACHE_KEY + userId;
        List<MarketGoodsCategoryVO> cached = RedisUtil.get(key);
        if (cached != null) {
            return cached;
        }
        List<MarketGoodsCategoryVO> result = marketCategoryDAO.queryMarketGoodsCategory(marketId, userId);
        // 分类缓存30分钟
        if (!CollectionUtils.isEmpty(result)) {
            RedisUtil.set(key, result, 30L, TimeUnit.MINUTES);
        }
        return result;
    }

}
