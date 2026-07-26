package com.newzkl.platform.base.biz.finance.infrastructure.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.CashPayeeInfoDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 现金支付收款方配置 DAO。
 *
 * <p>迁移自 new-scm {@code CashPayeeInfoDAO}。旧三个自定义方法
 * ({@code savePayeeInfo} / {@code queryPayeeInfos} / {@code delOldPayeeInfo})
 * 均可由 MyBatis-Plus 原生 API 表达, 故不保留 XML。</p>
 *
 * @author KC
 */
@Mapper
public interface CashPayeeInfoDAO extends BaseMapper<CashPayeeInfoDO> {

    /**
     * 按消费类型构建查询条件。
     *
     * @param consumeType 消费类型
     * @return 查询条件
     */
    default LambdaQueryWrapper<CashPayeeInfoDO> getLw(Integer consumeType) {
        return new LambdaQueryWrapper<CashPayeeInfoDO>()
                .eq(CashPayeeInfoDO::getConsumeType, consumeType);
    }
}
