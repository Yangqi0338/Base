package com.newzkl.platform.base.biz.account.infrastructure.dao;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.account.infrastructure.entity.EmpDO;
import com.newzkl.platform.base.biz.account.model.req.EmpQuery;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmpDAO extends BaseMapper<EmpDO> {

    default BaseLambdaQueryWrapper<EmpDO> getLw(EmpQuery query) {
        return new BaseLambdaQueryWrapper<EmpDO>()
                .notEmptyIn(EmpDO::getId, query.getIdList());
    }
}
