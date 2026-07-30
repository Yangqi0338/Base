package com.newzkl.platform.base.biz.order.domain.service;

import com.newzkl.platform.base.biz.order.model.order.res.IndexCountRes;
import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;

/**
 * 交易-订单统计领域服务
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.application.service.IQueryService#indexCount}。
 * 旧实现落在 application 层并直连 {@code SpuOrderDAO}, 中台改为领域服务 + 仓储接口。</p>
 *
 * @author KC
 */
public interface OrderCountDomain {

    /**
     * 首页分组统计 (分组曲线 + 全量订单量 + 全量订单金额)
     *
     * @param timeQuery 时间分组查询
     * @return 首页统计结果
     */
    IndexCountRes indexCount(TimeQuery timeQuery);
}
