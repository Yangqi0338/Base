package com.newzkl.platform.base.biz.finance.infrastructure.dao;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.EarningRecordDO;
import com.newzkl.platform.base.biz.finance.model.earnings.req.EarningRecordQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * EarningRecordDAO继承基类
 *
 * @author 86176
 */
@Mapper
public interface EarningRecordDAO extends BaseMapper<EarningRecordDO> {

    default LambdaQueryWrapper<EarningRecordDO> getLw(EarningRecordQuery query) {
        return new BaseLambdaQueryWrapper<EarningRecordDO>()
                .notEmptyEq(EarningRecordDO::getAccountId, query.getAccountId())
                .notEmptyIn(EarningRecordDO::getEarningType, query.getEarningTypeList())
                .notEmptyEq(EarningRecordDO::getJoinOrderNo, query.getOrderNo())
                .notEmptyEq(EarningRecordDO::getState, query.getState())
                .notNullNe(EarningRecordDO::getState, query.getStateNot());
    }

    /**
     * 查询总分润金额
     *
     * @param state
     * @return
     */
    Integer queryTotalEarning(Integer state);

    /**
     * 更新分润记录结算状态
     *
     * @param skuOrderId
     * @param state
     */
    void alterEarningRecordState(@Param("skuOrderId") Long skuOrderId, @Param("state") Integer state);
}
