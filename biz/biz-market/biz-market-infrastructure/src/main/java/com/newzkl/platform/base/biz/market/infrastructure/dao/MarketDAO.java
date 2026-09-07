package com.newzkl.platform.base.biz.market.infrastructure.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.newzkl.platform.base.biz.market.infrastructure.entity.MarketDO;
import com.newzkl.platform.base.biz.market.infrastructure.entity.StoreGoodsDO;
import com.newzkl.platform.base.biz.market.model.query.distribution.StoreGoodsQuery;
import com.newzkl.platform.base.biz.market.model.query.market.MarketQuery;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * MarketDAO继承基类
 */
@Mapper
@Repository
public interface MarketDAO extends BaseMapper<MarketDO> {

    default BaseLambdaQueryWrapper<MarketDO> getLw(MarketQuery query) {
        BaseLambdaQueryWrapper<MarketDO> wrapper = new BaseLambdaQueryWrapper<MarketDO>()
                .notEmptyIn(MarketDO::getId, query.getIdList())
                .notEmptyIn(MarketDO::getCreatorId, query.getAccountIdList())
                .notEmptyLike(MarketDO::getMarketName, query.getMarketName())
                .notEmptyEq(MarketDO::getCategoryId, query.getCategoryId())
                .notEmptyEq(MarketDO::getMarketType, query.getMarketType())
                ;
        return wrapper;
    }

}