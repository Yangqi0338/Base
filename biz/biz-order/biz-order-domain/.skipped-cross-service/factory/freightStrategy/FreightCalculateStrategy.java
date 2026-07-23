package com.newzkl.platform.base.biz.order.domain.factory.freightStrategy;

import com.newzkl.platform.base.biz.order.model.order.req.FreightCalculateReq;
import com.newzkl.platform.base.biz.order.model.order.res.FreightCalculateRes;

/**
 * 运费计算策略接口
 * @author sijiwang
 */
public interface FreightCalculateStrategy {
    /**
     * 计算运费
     * @param req 运费计算请求
     * @return 运费计算结果
     */
    FreightCalculateRes calculate(FreightCalculateReq req);

    /**
     * 匹配模板ID
     * @return 模板ID
     */
    Long getTemplateId();
}