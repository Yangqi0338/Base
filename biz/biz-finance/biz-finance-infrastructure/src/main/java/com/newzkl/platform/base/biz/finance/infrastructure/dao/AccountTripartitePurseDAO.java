package com.newzkl.platform.base.biz.finance.infrastructure.dao;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.AccountTripartitePurseDO;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountTripartitePurseQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * AccountTripartitePurseDAO继承基类
 */
@Mapper
public interface AccountTripartitePurseDAO extends BaseMapper<AccountTripartitePurseDO> {

    default LambdaQueryWrapper<AccountTripartitePurseDO> getPrimaryLw(Long accountId) {
        return new BaseLambdaQueryWrapper<AccountTripartitePurseDO>().eq(AccountTripartitePurseDO::getAccountId, accountId);
    }

    default LambdaQueryWrapper<AccountTripartitePurseDO> getLw(AccountTripartitePurseQuery query) {
        return new BaseLambdaQueryWrapper<AccountTripartitePurseDO>()
                .notEmptyEq(AccountTripartitePurseDO::getOidApplySeqNo, query.getOidApplySeqNo())
                .notEmptyEq(AccountTripartitePurseDO::getAccountId, query.getAccountId())
                ;
    }

    /**
     * 增加客户三方账户余额
     *
     * @param accountId
     * @param amount
     */
    void addAccountTripartitePurseAmount(@Param("accountId") Long accountId, @Param("amount") Integer amount);

    /**
     * 扣减客户三方账户余额
     *
     * @param accountId
     * @param amount
     */
    void subAccountTripartitePurseAmount(@Param("accountId") Long accountId, @Param("amount") Integer amount);

    /**
     * 查新提交资料信息
     *
     * @param accountId
     * @return
     */
    String queryCommitInfo(@Param("accountId") Long accountId);

}
