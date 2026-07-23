package com.newzkl.platform.base.biz.finance.infrastructure.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.BankDO;
import com.newzkl.platform.base.biz.finance.model.purse.req.BankQuery;
import org.apache.ibatis.annotations.Mapper;

/**
 * 银行(bank)表数据库访问层
 *
 * @author kc
 * @since 2025-09-18 09:58:31
 */
@Mapper
public interface BankDAO extends BaseMapper<BankDO> {

    default LambdaQueryWrapper<BankDO> getLw(BankQuery query) {
        LambdaQueryWrapper<BankDO> wrapper = new LambdaQueryWrapper<BankDO>()
                .eq(BankDO::getBankCode, query.getBankCode())
                .eq(BankDO::getBankName, query.getBankName());
        return wrapper;
    }

}

