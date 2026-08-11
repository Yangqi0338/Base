package com.newzkl.platform.base.biz.socialbang.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.socialbang.infrastructure.entity.StrategyDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 策略数据访问
 *
 * @author niu
 */
@Mapper
@Repository
public interface StrategyDAO extends BaseMapper<StrategyDO> {
}
