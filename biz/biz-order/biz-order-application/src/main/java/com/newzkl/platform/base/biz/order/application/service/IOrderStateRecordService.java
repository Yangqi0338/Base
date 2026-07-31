package com.newzkl.platform.base.biz.order.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.order.model.req.OrderStateRecordPageReq;
import com.newzkl.platform.base.biz.order.model.vo.OrderStateRecordVO;

import java.util.List;

/**
 * 订单状态记录 应用服务
 *
 * <p>迁移: 承接 new-scm scm-sale OrderStateRecordController 的读侧编排。
 * 仅声明契约, 实现待 domain 查询端口(IOrderStateRecordDomainService 现仅 create)补齐后落地</p>
 *
 * @author KC
 * @since 2026-07-31
 */
public interface IOrderStateRecordService {

    /**
     * 按 spuOrderId 查询订单状态记录列表(按操作时间倒序)
     *
     * @param spuOrderId 商品订单 ID
     * @return 订单状态记录列表
     */
    List<OrderStateRecordVO> listBySpuOrderId(Long spuOrderId);

    /**
     * 分页查询订单状态记录
     *
     * @param req 分页查询参数
     * @return 分页结果
     */
    IPage<OrderStateRecordVO> pageQuery(OrderStateRecordPageReq req);
}
