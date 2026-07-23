package com.newzkl.platform.base.biz.finance.infrastructure.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.BankBranchDO;
import com.newzkl.platform.base.biz.finance.model.purse.req.BankQuery;
import org.apache.ibatis.annotations.Mapper;

/**
 * 银行支行(bank_branch)表数据库访问层
 *
 * @author kc
 * @since 2025-09-18 11:37:36
 */
@Mapper
public interface BankBranchDAO extends BaseMapper<BankBranchDO> {

    default LambdaQueryWrapper<BankBranchDO> getLw(BankQuery query) {
        LambdaQueryWrapper<BankBranchDO> queryWrapper = new LambdaQueryWrapper<BankBranchDO>()
                .eq(BankBranchDO::getBankCode, query.getBankCode());
        return queryWrapper;
    }
}

