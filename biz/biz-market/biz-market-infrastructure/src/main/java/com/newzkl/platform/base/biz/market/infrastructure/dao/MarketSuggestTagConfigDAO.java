package com.newzkl.platform.base.biz.market.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.market.infrastructure.entity.MarketSuggestTagConfigDO;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 运营商建议标签配置 DAO。
 *
 * @author KC
 */
@Mapper
@Repository
public interface MarketSuggestTagConfigDAO extends BaseMapper<MarketSuggestTagConfigDO> {

    /**
     * 按账户组装查询条件。
     *
     * <p>迁移: 替代旧 {@code MarketSuggestTagConfigDAO.queryByAccountId} 手写 SQL。</p>
     *
     * @param accountId 账户ID
     * @return 条件包装器
     */
    default BaseLambdaQueryWrapper<MarketSuggestTagConfigDO> getLwByAccount(Long accountId) {
        return new BaseLambdaQueryWrapper<MarketSuggestTagConfigDO>()
                .notEmptyEq(MarketSuggestTagConfigDO::getAccountId, accountId);
    }
}
