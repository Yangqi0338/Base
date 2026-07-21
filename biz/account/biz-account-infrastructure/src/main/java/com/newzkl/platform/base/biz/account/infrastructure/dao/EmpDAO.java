package com.newzkl.platform.base.biz.account.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.account.infrastructure.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.biz.account.infrastructure.entity.EmpDO;
import com.newzkl.platform.base.biz.account.model.req.EmpQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmpDAO extends BaseMapper<EmpDO> {

    Integer countUserByRole(@Param("roleIdList") List<Long> roleIdList);

    default BaseLambdaQueryWrapper<EmpDO> getLw(EmpQuery query) {
        return new BaseLambdaQueryWrapper<EmpDO>()
                .notEmptyIn(EmpDO::getId, query.getIdList());
    }
}
