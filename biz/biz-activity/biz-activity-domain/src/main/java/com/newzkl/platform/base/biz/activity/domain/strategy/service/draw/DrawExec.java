package com.newzkl.platform.base.biz.activity.domain.strategy.service.draw;


import com.newzkl.platform.base.biz.activity.model.strategy.req.DrawReq;
import com.newzkl.platform.base.biz.activity.model.strategy.res.DrawMethodRes;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;

import java.util.List;

/**
 * 执行活动抽取接口
 * @Author: niu
 * @Date: 2024/1/8 17:11
 */
public interface DrawExec {

    /**
     * 执行活动抽取
     * @param req
     * @return
     */
    PlatformResult<List<DrawMethodRes>> doDrawExec(DrawReq req);

}
