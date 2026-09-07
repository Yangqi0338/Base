package com.newzkl.platform.base.biz.market.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.infrastructure.entity.MarketBindDO;
import com.newzkl.platform.base.biz.market.model.query.market.AppBindMarketGoodsPageQuery;
import com.newzkl.platform.base.biz.market.model.vo.market.BindMarketGoodsRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * MarketBindDAO继承基类
 *
 * @author 86176
 */
@Mapper
@Repository
public interface MarketBindDAO extends BaseMapper<MarketBindDO> {

    Page<BindMarketGoodsRes> queryMarketGoods(Page<?> page, @Param("query") AppBindMarketGoodsPageQuery query);

}