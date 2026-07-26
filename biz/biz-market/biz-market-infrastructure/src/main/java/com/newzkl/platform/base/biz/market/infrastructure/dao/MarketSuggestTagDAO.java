package com.newzkl.platform.base.biz.market.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.market.infrastructure.entity.MarketSuggestTagDO;
import com.newzkl.platform.base.biz.market.model.suggest.query.SuggestTagQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 建议标签提交记录 DAO。
 *
 * @author KC
 */
@Mapper
@Repository
public interface MarketSuggestTagDAO extends BaseMapper<MarketSuggestTagDO> {

    /**
     * 组装查询条件。
     *
     * <p>迁移: 替代旧 {@code MarketSuggestTagDAO.xml#queryByAccountId} 的 operator 分支 SQL。
     * operator=true 按 {@code opeartor_id} + 可选 mobile 过滤; 否则按 {@code account_id} 过滤。
     * 排序恒为 {@code create_time desc}, 与旧 SQL 一致。</p>
     *
     * @param query 查询条件
     * @return 条件包装器
     */
    default BaseLambdaQueryWrapper<MarketSuggestTagDO> getLw(SuggestTagQuery query) {
        BaseLambdaQueryWrapper<MarketSuggestTagDO> lw = new BaseLambdaQueryWrapper<>();
        if (Boolean.TRUE.equals(query.getOperator())) {
            lw.notEmptyEq(MarketSuggestTagDO::getOpeartorId, query.getAccountId())
                    .notEmptyEq(MarketSuggestTagDO::getMobile, query.getMobile());
        } else {
            lw.notEmptyEq(MarketSuggestTagDO::getAccountId, query.getAccountId());
        }
        lw.orderByDesc(MarketSuggestTagDO::getCreateTime);
        return lw;
    }
}
