package com.newzkl.platform.base.biz.auth.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.newzkl.platform.base.biz.auth.infrastructure.entity.AccountLoginLogDO;
import com.newzkl.platform.base.biz.auth.model.oauth.query.AccountLoginLogQuery;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 登录记录
 *
 * @author fang
 */
@Mapper
public interface AccountLoginLogDAO extends BaseMapper<AccountLoginLogDO> {

    default BaseLambdaQueryWrapper<AccountLoginLogDO> getLw(AccountLoginLogQuery query) {
        return new BaseLambdaQueryWrapper<AccountLoginLogDO>()
                .notEmptyIn(AccountLoginLogDO::getAccountId, query.getAccountIdList())
                .notEmptyIn(AccountLoginLogDO::getId, query.getIdList())
                ;
    }

}
