package com.newzkl.platform.base.biz.market.infrastructure.adapt.repository;
import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.domain.adapt.repository.MarketRepository;
import com.newzkl.platform.base.biz.market.infrastructure.dao.MarketBindDAO;
import com.newzkl.platform.base.biz.market.infrastructure.dao.MarketDAO;
import com.newzkl.platform.base.biz.market.infrastructure.entity.MarketBindDO;
import com.newzkl.platform.base.biz.market.infrastructure.entity.MarketDO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.market.model.dto.market.MarketBindDTO;
import com.newzkl.platform.base.biz.market.model.dto.market.MarketDTO;
import com.newzkl.platform.base.biz.market.model.query.market.AppBindMarketGoodsPageQuery;
import com.newzkl.platform.base.biz.market.model.query.market.MarketQuery;
import com.newzkl.platform.base.biz.market.model.req.market.BindMarketListReq;
import com.newzkl.platform.base.biz.market.model.req.market.ClientBindMarketReq;
import com.newzkl.platform.base.biz.market.model.req.market.MarketUserReq;
import com.newzkl.platform.base.biz.market.model.req.market.UpdateMarketDataReq;
import com.newzkl.platform.base.biz.market.model.vo.market.*;
import com.newzkl.platform.base.common.ddd.model.enums.market.MarketEnum;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MarketRepositoryImpl implements MarketRepository {

    private final MarketBindDAO marketBindDAO;
    private final MarketDAO marketDAO;

    @Override
    public Page<BindMarketGoodsRes> queryMarketGoods(AppBindMarketGoodsPageQuery query) {
        return marketBindDAO.queryMarketGoods(RepositorySupport.page(query),query);
    }

    @Override
    public Page<MarketRes> queryMarketList(MarketQuery query) {
        Page<MarketDO> page = marketDAO.selectPage(
                RepositorySupport.page(query),
                marketDAO.getLw(query)
        );
        return TransferUtils.transferPage(page, MarketRes::new);
    }

    @Override
    public MarketRes queryMarket(Long marketId) {
        return TransferUtils.transfer(
                marketDAO.selectById(marketId),
                MarketRes::new
        );
    }

    @Override
    public void saveMarket(MarketDTO marketDTO) {
        marketDAO.insert(TransferUtils.transfer(marketDTO, MarketDO::new));
    }

    @Override
    public void updateMarket(MarketDTO marketDTO) {
        marketDAO.updateById(TransferUtils.transfer(marketDTO, MarketDO::new));
    }

    @Override
    public MarketBindDTO queryAccountIsBindMarket(ClientBindMarketReq req) {
        return marketBindDAO.selectList(
                new LambdaQueryWrapper<MarketBindDO>()
                        .eq(MarketBindDO::getMarketId, req.getMarketId())
                        .eq(MarketBindDO::getBindType, req.getBindType())
                        .eq(MarketBindDO::getUserId, req.getUserId())
        ).stream().findFirst()
                .map(bind -> TransferUtils.transfer(bind, MarketBindDTO::new))
                .orElse(null);
    }

    @Override
    public MarketBindDTO queryMarketBind(Long id) {
        return TransferUtils.transfer(marketBindDAO.selectById(id), MarketBindDTO::new);
    }

    @Override
    public void updateMarketBind(MarketBindDTO marketBindDTO) {
        marketBindDAO.update(new LambdaUpdateWrapper<MarketBindDO>()
                .eq(MarketBindDO::getId, marketBindDTO.getId())
                .set(MarketBindDO::getState, marketBindDTO.getState())
                .set(MarketBindDO::getDebindTime, null)
                .set(marketBindDTO.getUserName() != null,
                        MarketBindDO::getUserName, marketBindDTO.getUserName()));
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
                        .eq(MarketBindDO::getUserId, req.getBindAccountId())
                        .eq(MarketBindDO::getState, CommonEnum.YesOrNo.YES)
                        .eq(req.getBindIdentity() != null, MarketBindDO::getBindType, req.getBindIdentity())
        );
        if (CollectionUtils.isEmpty(binds)) {
            return Collections.emptyList();
        }
        // market_id -> bind_id 映射（用于返回 id = mb.market_id）
        Map<Long, Long> marketIdToBindId = binds.stream()
                .collect(Collectors.toMap(MarketBindDO::getMarketId, MarketBindDO::getId, (a, b) -> a));

        // 2. 根据 market_id 查询市场信息，支持名称和分类过滤
        MarketQuery marketQuery = new MarketQuery();
        marketQuery.resetQueryList();
        marketQuery.setIdList(CollUtil.newArrayList(marketIdToBindId.keySet()));
        marketQuery.setMarketName(req.getMarketName());
        marketQuery.setCategoryId(req.getCategoryId());
        List<MarketDO> markets = marketDAO.selectList(marketDAO.getLw(marketQuery));
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
                        .eq(req.getBindType() != null, MarketBindDO::getBindType, req.getBindType())
                        .eq(MarketBindDO::getState, CommonEnum.YesOrNo.YES)
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

}
