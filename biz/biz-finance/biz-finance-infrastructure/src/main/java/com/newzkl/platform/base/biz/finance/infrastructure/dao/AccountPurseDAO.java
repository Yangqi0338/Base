package com.newzkl.platform.base.biz.finance.infrastructure.dao;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.AccountPurseDO;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountPurseQuery;
import com.newzkl.platform.base.biz.finance.model.purse.req.BatchAccountPurseQuery;
import org.apache.ibatis.annotations.Mapper;

/**
 * AccountPurseDAO继承基类
 *
 * @author 86176
 */
@Mapper
public interface AccountPurseDAO extends BaseMapper<AccountPurseDO> {

    default BaseLambdaQueryWrapper<AccountPurseDO> getLw(AccountPurseQuery req) {
        return new BaseLambdaQueryWrapper<AccountPurseDO>()
                .notEmptyIn(AccountPurseDO::getAccountId, req.getAccountIdList())
                .notEmptyEq(AccountPurseDO::getAccountType, req.getAccountType())
                .notEmptyIn(AccountPurseDO::getPurseType, req.getPurseTypeList())
                ;
    }

    default LambdaQueryWrapper<AccountPurseDO> getBatchQueryAccountEarningLw(BatchAccountPurseQuery req) {
        return new BaseLambdaQueryWrapper<AccountPurseDO>()
                .eq(AccountPurseDO::getAccountId, req.getAccountId())
                .eq(AccountPurseDO::getAccountType, req.getAccountType())
                .eq(AccountPurseDO::getPurseType, 0);
    }

}
