package com.newzkl.platform.base.biz.account.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.account.infrastructure.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.biz.account.infrastructure.entity.AccountJobDO;
import com.newzkl.platform.base.biz.account.model.req.AccountJobQuery;
import org.apache.ibatis.annotations.Mapper;

/**
 * 后台角色
 *
 * @author fang
 */
@Mapper
public interface AccountJobDAO extends BaseMapper<AccountJobDO> {

    default BaseLambdaQueryWrapper<AccountJobDO> getLw(AccountJobQuery accountJobQuery) {
        return new BaseLambdaQueryWrapper<AccountJobDO>()
                .notEmptyLike(AccountJobDO::getName, accountJobQuery.getName())
                .notEmptyIn(AccountJobDO::getId, accountJobQuery.getIdList())
                ;
    }
}