package com.newzkl.platform.base.biz.order.application.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.newzkl.platform.base.biz.order.application.service.IRefundOperationRecordService;
import com.newzkl.platform.base.biz.order.domain.service.IRefundOperationRecordDomainService;
import com.newzkl.platform.base.biz.order.model.dto.RefundOperationRecordEntity;
import com.newzkl.platform.base.biz.order.model.req.RefundOperationRecordCreateReq;
import com.newzkl.platform.base.biz.order.model.req.RefundOperationRecordPageReq;
import com.newzkl.platform.base.biz.order.model.req.RefundOperationRecordUpdateReq;
import com.newzkl.platform.base.biz.order.model.vo.RefundOperationRecordVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 售后操作记录 应用层服务实现类
 *
 * @author sijiwang
 * @since 2026-01-23
 */
@Service
@Slf4j
public class RefundOperationRecordServiceImpl implements IRefundOperationRecordService {

    @Autowired
    private IRefundOperationRecordDomainService refundOperationRecordDomainService;

    /**
     * 按售后单ID查询操作记录列表
     */
    @Override
    public List<RefundOperationRecordVO> listByRefundId(Long refundId) {
        try {
            log.info("查询售后操作记录列表，售后单ID：{}", refundId);

            List<RefundOperationRecordEntity> entityList = refundOperationRecordDomainService.listByRefundId(refundId);
            // Entity列表转换为VO列表
            return entityList.stream().map(entity -> {
                RefundOperationRecordVO vo = new RefundOperationRecordVO();
                BeanUtils.copyProperties(entity, vo);
                return vo;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("查询售后操作记录列表失败，售后单ID：{}，原因：{}", refundId, e.getMessage(), e);
            ThrowsException.exception(BaseErrorCode.OPERATE_FAIL, "查询售后操作记录列表失败：" + e.getMessage());
            return null;
        }
    }

    /**
     * 分页查询售后操作记录
     */
    @Override
    public IPage<RefundOperationRecordVO> pageQuery(RefundOperationRecordPageReq req) {
        try {
            log.info("分页查询售后操作记录，页码：{}，页大小：{}，售后单ID：{}，SPU订单ID：{}，操作人ID：{}，操作人角色编码：{}",
                    req.getCurrent(), req.getSize(), req.getRefundId(), req.getSpuOrderId(), req.getOperatorId(), req.getOperatorRoleCode());

            // 1. 构建分页参数
            Page<RefundOperationRecordEntity> page = new Page<>(req.getCurrent(), req.getSize());

            // 2. 调用领域服务分页查询
            IPage<RefundOperationRecordEntity> entityPage = refundOperationRecordDomainService.pageQuery(
                    page, req.getRefundId(), req.getOperatorRoleCode()
            );

            // 3. 转换为VO分页结果
            IPage<RefundOperationRecordVO> voPage = new Page<>();
            BeanUtils.copyProperties(entityPage, voPage);

            // 4. 转换列表数据
            List<RefundOperationRecordVO> voList = entityPage.getRecords().stream().map(entity -> {
                RefundOperationRecordVO vo = new RefundOperationRecordVO();
                BeanUtils.copyProperties(entity, vo);
                return vo;
            }).collect(Collectors.toList());
            voPage.setRecords(voList);

            return voPage;
        } catch (Exception e) {
            log.error("分页查询售后操作记录失败，原因：{}", e.getMessage(), e);
            ThrowsException.exception(BaseErrorCode.OPERATE_FAIL, "分页查询售后操作记录失败：" + e.getMessage());
            return null;
        }
    }
}