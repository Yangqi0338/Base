package com.newzkl.platform.base.biz.socialbang.infrastructure.adapt.repository;

import com.newzkl.platform.base.biz.socialbang.domain.adapt.repository.StrategyRepository;
import com.newzkl.platform.base.biz.socialbang.infrastructure.dao.StrategyDAO;
import com.newzkl.platform.base.biz.socialbang.infrastructure.entity.StrategyDO;
import com.newzkl.platform.base.biz.socialbang.model.strategy.aggregates.StrategyRich;
import com.newzkl.platform.base.biz.socialbang.model.strategy.vo.StrategyBriefVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 策略数据仓库实现
 *
 * @author niu
 */
@Repository
public class StrategyRepositoryImpl implements StrategyRepository {

    private final StrategyDAO strategyDAO;

    @Autowired
    public StrategyRepositoryImpl(StrategyDAO strategyDAO) {
        this.strategyDAO = strategyDAO;
    }

    @Override
    public StrategyRich queryStrategyRich(Long strategyId) {
        StrategyDO strategy = strategyDAO.selectById(strategyId);
        StrategyBriefVO strategyBrief = new StrategyBriefVO();
        strategyBrief.setStrategyDesc(strategy.getStrategyDesc());
        strategyBrief.setStrategyMode(strategy.getStrategyMode());
        strategyBrief.setGrantType(strategy.getGrantType());
        strategyBrief.setGrantDate(strategy.getGrantDate());
        strategyBrief.setExtInfo(strategy.getExtInfo());
        StrategyRich strategyRich = new StrategyRich();
        strategyRich.setStrategy(strategyBrief);
        return strategyRich;
    }
}
