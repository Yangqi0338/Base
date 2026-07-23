package com.newzkl.platform.base.biz.market.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.market.infrastructure.entity.MarketDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * MarketDAO继承基类
 */
@Mapper
@Repository
public interface MarketDAO extends BaseMapper<MarketDO> {

}