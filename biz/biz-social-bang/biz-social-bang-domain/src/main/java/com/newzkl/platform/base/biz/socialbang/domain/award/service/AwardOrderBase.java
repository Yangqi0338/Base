package com.newzkl.platform.base.biz.socialbang.domain.award.service;

import com.newzkl.platform.base.biz.socialbang.domain.adapt.repository.AwardRepository;
import com.newzkl.platform.base.biz.socialbang.model.award.req.RecordAwardOrderReq;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 奖品单服务实现
 * @Author: niu
 * @Date: 2024/1/17 15:13
 */
@Service
public class AwardOrderBase implements AwardOrder {

    @Resource
    private AwardRepository awardRepository;

    @Override
    public void recordAwardOrder(List<RecordAwardOrderReq> req) {
        awardRepository.recordAwardOrder(req);
    }
}
