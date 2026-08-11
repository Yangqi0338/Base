package com.newzkl.platform.base.biz.finance.infrastructure.dao;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.BillOrderAwardDO;
import com.newzkl.platform.base.biz.finance.model.account.req.BillOrderAwardQuery;
import com.newzkl.platform.base.biz.finance.model.account.res.BillOrderAwardAmountRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * AccountPurseDAO继承基类
 *
 * @author 86176
 */
@Mapper
public interface BillOrderAwardDAO extends BaseMapper<BillOrderAwardDO> {

    /**
     * 根据条件查询流水金额
     *
     * @param req 查询参数
     * @return 账单奖励记录总数
     */
    BillOrderAwardAmountRes selectSumAmountByCondition(@Param(Constants.WRAPPER) Wrapper<BillOrderAwardDO> req);

    int updateByIdAndAmount(@Param("record") BillOrderAwardDO record, @Param("oldAmount") Integer oldAmount);

    default LambdaQueryWrapper<BillOrderAwardDO> getLw(BillOrderAwardQuery query) {
        return new BaseLambdaQueryWrapper<BillOrderAwardDO>()
                .notEmptyIn(BillOrderAwardDO::getAccountId, query.getAccountIdList())
                .between(BillOrderAwardDO::getCreateTime, query.getCreateTime())
                .notEmptyEq(BillOrderAwardDO::getAccountType, query.getAccountType())
                .notEmptyEq(BillOrderAwardDO::getPurseType, query.getPurseType())
                ;
    }

}
