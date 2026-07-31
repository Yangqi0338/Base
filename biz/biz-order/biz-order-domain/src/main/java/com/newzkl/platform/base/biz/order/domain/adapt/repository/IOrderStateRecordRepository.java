package com.newzkl.platform.base.biz.order.domain.adapt.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.order.model.dto.OrderStateRecordEntity;
import com.newzkl.platform.base.biz.order.model.req.OrderStateRecordPageReq;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 订单状态记录仓储接口
 *
 * @author sijiwang
 * @since 2026-01-30
 */
public interface IOrderStateRecordRepository {

    /**
     * 保存/更新订单状态记录
     */
    OrderStateRecordEntity save(OrderStateRecordEntity record);
}
