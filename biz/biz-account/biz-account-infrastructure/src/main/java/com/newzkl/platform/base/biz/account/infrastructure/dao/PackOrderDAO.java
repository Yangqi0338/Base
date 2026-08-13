package com.newzkl.platform.base.biz.account.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.newzkl.platform.base.biz.account.infrastructure.entity.PackOrderDO;
import com.newzkl.platform.base.biz.account.model.pack.query.PackOrderQuery;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 入会礼包订单 Mapper
 *
 * @author KC
 */
@Mapper
public interface PackOrderDAO extends BaseMapper<PackOrderDO> {

    /**
     * 组装礼包订单查询条件
     *
     * @param query 查询条件
     * @return 条件构造器
     */
    default BaseLambdaQueryWrapper<PackOrderDO> getLw(PackOrderQuery query) {
        BaseLambdaQueryWrapper<PackOrderDO> lw = new BaseLambdaQueryWrapper<>();
        lw.notNullEq(PackOrderDO::getId, query.getId())
                .notNullEq(PackOrderDO::getState, query.getState())
                .notNullEq(PackOrderDO::getAccountId, query.getAccountId())
                .notNullEq(PackOrderDO::getPackId, query.getPackId())
                .notNullEq(PackOrderDO::getPackType, query.getPackType());
        lw.notEmptyIn(PackOrderDO::getId, query.getIdList());
        lw.notEmptyGe(PackOrderDO::getCreateTime, query.getCreateTimeGreater());
        lw.notEmptyLt(PackOrderDO::getCreateTime, query.getCreateTimeLess());
        lw.orderBy(query);
        return lw;
    }

    /**
     * 乐观状态跃迁（仅当当前状态等于 fromState 时更新为 toState）
     *
     * @param idList    订单ID集合
     * @param fromState 期望的当前状态
     * @param toState   目标状态
     * @return 更新条数
     */
    default int updateState(List<Long> idList, Integer fromState, Integer toState) {
        LambdaUpdateWrapper<PackOrderDO> uw = Wrappers.<PackOrderDO>lambdaUpdate()
                .set(PackOrderDO::getState, toState)
                .in(PackOrderDO::getId, idList)
                .eq(PackOrderDO::getState, fromState);
        return this.update(null, uw);
    }
}
