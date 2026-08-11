package com.newzkl.platform.base.biz.socialbang.domain.award.service;


import com.newzkl.platform.base.biz.socialbang.model.award.req.RecordAwardOrderReq;

import java.util.List;

/**
 * 奖品单接口
 * @Author: niu
 * @Date: 2024/1/17 15:05
 */
public interface AwardOrder {


    /**
     * 保存奖品单
     * @param req 请求对象
     */
    void recordAwardOrder(List<RecordAwardOrderReq> req);
}
