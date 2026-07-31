package com.newzkl.platform.base.biz.activity.infrastructure.adapt.repository;

import com.newzkl.platform.base.biz.activity.domain.adapt.repository.AwardRepository;
import com.newzkl.platform.base.biz.activity.infrastructure.dao.AwardRecordDAO;
import com.newzkl.platform.base.biz.activity.model.award.req.RecordAwardOrderReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 奖品单数据仓库实现
 *
 * @author niu
 */
@Repository
public class AwardRepositoryImpl implements AwardRepository {

    private final AwardRecordDAO awardRecordDAO;

    @Autowired
    public AwardRepositoryImpl(AwardRecordDAO awardRecordDAO) {
        this.awardRecordDAO = awardRecordDAO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recordAwardOrder(List<RecordAwardOrderReq> req) {
        awardRecordDAO.recordAwardOrder(req);
    }
}
