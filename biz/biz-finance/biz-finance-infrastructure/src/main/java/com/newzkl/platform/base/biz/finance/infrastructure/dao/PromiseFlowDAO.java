package com.newzkl.platform.base.biz.finance.infrastructure.dao;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.PromiseFlowDO;
import com.newzkl.platform.base.biz.finance.model.pay.req.PromiseFlowQuery;
import org.apache.ibatis.annotations.Mapper;

/**
 * 保证金流水
 *
 * @author fang
 */
@Mapper
public interface PromiseFlowDAO extends BaseMapper<PromiseFlowDO> {

    default BaseLambdaQueryWrapper<PromiseFlowDO> getLw(PromiseFlowQuery query) {
        return new BaseLambdaQueryWrapper<PromiseFlowDO>();
    }
}
