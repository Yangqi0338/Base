package com.newzkl.platform.base.biz.finance.infrastructure.dao;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.EarningContributeDO;
import com.newzkl.platform.base.biz.finance.model.earnings.req.AccountContributeQuery;
import com.newzkl.platform.base.biz.finance.model.earnings.req.AccountContributeRpcQuery;
import com.newzkl.platform.base.biz.finance.model.earnings.req.AlterAccountContributeDataReq;
import org.apache.ibatis.annotations.Mapper;

/**
 * EarningContributeDAO继承基类
 */
@Mapper
public interface EarningContributeDAO extends BaseMapper<EarningContributeDO> {

    default LambdaQueryWrapper<EarningContributeDO> getLw(AccountContributeQuery query) {
        LambdaQueryWrapper<EarningContributeDO> queryWrapper = new BaseLambdaQueryWrapper<EarningContributeDO>()
                .notEmptyEq(EarningContributeDO::getParentId, query.getAccountId())
                .notEmptyEq(EarningContributeDO::getAccountId, query.getMyAccountId())
                .eq(EarningContributeDO::getAccountType, query.getAccountType());
        return queryWrapper;
    }

    default LambdaQueryWrapper<EarningContributeDO> getBatchLw(AccountContributeRpcQuery query) {
        LambdaQueryWrapper<EarningContributeDO> queryWrapper = new BaseLambdaQueryWrapper<EarningContributeDO>()
                .notEmptyIn(EarningContributeDO::getAccountId, query.getAccountIds())
                .eq(EarningContributeDO::getAccountType, query.getAccountType());
        return queryWrapper;
    }

    /**
     * 更新客户贡献数据
     *
     * @param req
     */
    void alterAccountContribute(AlterAccountContributeDataReq req);
}
