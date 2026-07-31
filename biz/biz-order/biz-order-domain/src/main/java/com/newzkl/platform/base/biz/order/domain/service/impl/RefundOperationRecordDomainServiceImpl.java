package com.newzkl.platform.base.biz.order.domain.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.IRefundOperationRecordRepository;
import com.newzkl.platform.base.biz.order.domain.service.IRefundOperationRecordDomainService;
import com.newzkl.platform.base.biz.order.model.dto.RefundOperationRecordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 售后操作记录 领域服务实现
 *
 * @author 开发者名称
 * @since 2026-01-23
 */
@Service
public class RefundOperationRecordDomainServiceImpl implements IRefundOperationRecordDomainService {

    @Autowired
    private IRefundOperationRecordRepository refundOperationRecordRepository;

    @Override
    public RefundOperationRecordEntity create(RefundOperationRecordEntity entity) {
        // 领域规则校验
        Assert.notNull(entity, "售后操作记录不能为空");
        Assert.notNull(entity.getRefundId(), "售后单ID不能为空");
        Assert.notNull(entity.getOperatorRoleCode(), "操作方角色编码不能为空");
        Assert.notNull(entity.getOperatorClient(), "操作方客户端类型不能为空");
        Assert.notNull(entity.getAfterState(), "操作后状态不能为空");
        Assert.hasText(entity.getOperationContent(), "操作内容描述不能为空");
        Assert.isNull(entity.getId(), "新增时ID必须为空");

        // 初始化默认值
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());

        // 调用仓储层保存
        return refundOperationRecordRepository.save(entity);
    }

    @Override
    public List<RefundOperationRecordEntity> listByRefundId(Long refundId) {
        Assert.notNull(refundId, "售后单ID不能为空");
        return refundOperationRecordRepository.listByRefundId(refundId);
    }

    @Override
    public IPage<RefundOperationRecordEntity> pageQuery(Page<RefundOperationRecordEntity> page, Long refundId, Long operatorRoleCode) {
        Assert.notNull(page, "分页参数不能为空");
        // 可选参数无需强制校验，空则不参与筛选
        return refundOperationRecordRepository.pageQuery(page, refundId, operatorRoleCode);
    }
}