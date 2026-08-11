package com.newzkl.platform.base.biz.socialbang.domain.adapt.repository;


import com.newzkl.platform.base.biz.socialbang.model.award.req.RecordAwardOrderReq;

import java.util.List;

/**
 * 奖品数据仓库
 * @Author: niu
 * @Date: 2024/1/17 15:22
 */
public interface AwardRepository {

    /**
     * 保存奖品单
     * @param req
     */
    void recordAwardOrder(List<RecordAwardOrderReq> req);
}
