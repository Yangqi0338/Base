package com.newzkl.platform.base.biz.order.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.order.model.req.RefundOperationRecordCreateReq;
import com.newzkl.platform.base.biz.order.model.req.RefundOperationRecordPageReq;
import com.newzkl.platform.base.biz.order.model.req.RefundOperationRecordUpdateReq;
import com.newzkl.platform.base.biz.order.model.vo.RefundOperationRecordVO;


import java.util.List;

/**
 * 售后操作记录 应用层服务接口
 * 封装领域服务调用，处理跨领域/第三方交互、事务编排等应用层逻辑
 *
 * @author sijiwang
 * @since 2026-01-23
 */
public interface IRefundOperationRecordService {

    /**
     * 按售后单ID查询操作记录列表（按操作时间倒序）
     * @param refundId 售后单ID
     * @return 视图对象列表
     */
    List<RefundOperationRecordVO> listByRefundId(Long refundId);

    /**
     * 分页查询售后操作记录
     * @param req 分页查询请求对象
     * @return 分页结果（视图对象）
     */
    IPage<RefundOperationRecordVO> pageQuery(RefundOperationRecordPageReq req);
}