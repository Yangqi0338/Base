package com.newzkl.platform.base.biz.goods.infrastructure.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.ExecuteLogDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志
 *
 * @author fang
 */
@Mapper
public interface ExecuteLogDAO extends BaseMapper<ExecuteLogDO> {

}