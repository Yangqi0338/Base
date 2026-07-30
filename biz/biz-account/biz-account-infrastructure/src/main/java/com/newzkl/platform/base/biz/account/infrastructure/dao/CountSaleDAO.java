package com.newzkl.platform.base.biz.account.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.account.infrastructure.entity.CountSaleDO;
import com.newzkl.platform.base.biz.account.model.req.CountSaleQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 销售统计 DAO
 *
 * @author KC
 */
@Mapper
public interface CountSaleDAO extends BaseMapper<CountSaleDO> {

    /**
     * 构建销售统计查询条件
     *
     * <p>对齐旧 mapper {@code CountSaleDAO.xml} 的 where 片段: {@code idList} /
     * {@code accountIdList} in 匹配, {@code role} 与 {@code date} 等值匹配,
     * 且时间范围 {@code createStartTime} / {@code createEndTime} 作用在业务列
     * {@code date} 上 (而非 {@code create_time}), 区间为左闭右开。
     * 旧 SQL 无 order by, 本仓照旧不加排序。</p>
     *
     * @param query 查询条件
     * @return 查询包装器
     */
    default BaseLambdaQueryWrapper<CountSaleDO> getLw(CountSaleQuery query) {
        BaseLambdaQueryWrapper<CountSaleDO> wrapper = new BaseLambdaQueryWrapper<CountSaleDO>()
                .notEmptyIn(CountSaleDO::getId, query.getIdList())
                .notEmptyIn(CountSaleDO::getAccountId, query.getAccountIdList())
                .notNullEq(CountSaleDO::getRole, query.getRole())
                .notNullEq(CountSaleDO::getDate, query.getDate());
        String startTime = query.getCreateStartTime();
        String endTime = query.getCreateEndTime();
        wrapper.apply(startTime != null && !startTime.isBlank(), "`date` >= {0}", startTime)
                .apply(endTime != null && !endTime.isBlank(), "`date` < {0}", endTime);
        return wrapper;
    }
}
