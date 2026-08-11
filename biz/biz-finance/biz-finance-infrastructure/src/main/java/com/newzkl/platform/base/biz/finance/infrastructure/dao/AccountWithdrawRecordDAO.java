package com.newzkl.platform.base.biz.finance.infrastructure.dao;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.AccountWithdrawRecordDO;
import com.newzkl.platform.base.biz.finance.model.purse.req.TripartiteWithdrawRecordQuery;
import org.apache.ibatis.annotations.Mapper;

/**
 * AccountWithdrawRecordDAO继承基类
 *
 * @author 86176
 */
@Mapper
public interface AccountWithdrawRecordDAO extends BaseMapper<AccountWithdrawRecordDO> {

    default BaseLambdaQueryWrapper<AccountWithdrawRecordDO> getLw(TripartiteWithdrawRecordQuery query) {
        return new BaseLambdaQueryWrapper<AccountWithdrawRecordDO>()
                .notEmptyIn(AccountWithdrawRecordDO::getAccountId, query.getAccountIdList())
                ;
    }
}
