package com.newzkl.platform.base.biz.socialbang.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.socialbang.infrastructure.entity.StrategyDetailDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 策略明细数据访问
 *
 * @author niu
 */
@Mapper
@Repository
public interface StrategyDetailDAO extends BaseMapper<StrategyDetailDO> {
}
