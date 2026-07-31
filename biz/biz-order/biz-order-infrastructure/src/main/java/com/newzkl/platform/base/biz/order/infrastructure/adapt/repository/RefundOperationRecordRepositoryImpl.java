package com.newzkl.platform.base.biz.order.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.newzkl.platform.base.biz.order.domain.adapt.repository.IRefundOperationRecordRepository;
import com.newzkl.platform.base.biz.order.infrastructure.assembler.RefundOperationRecordAssembler;
import com.newzkl.platform.base.biz.order.infrastructure.dao.RefundOperationRecordDAO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.RefundOperationRecordDO;
import com.newzkl.platform.base.biz.order.model.dto.RefundOperationRecordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 售后操作记录 仓储实现类
 *
 * @author 开发者名称
 * @since 2026-01-23
 */
@Repository
public class RefundOperationRecordRepositoryImpl implements IRefundOperationRecordRepository {

    @Autowired
    private RefundOperationRecordDAO refundOperationRecordDAO;

    @Resource
    private RefundOperationRecordAssembler refundOperationRecordAssembler;

    @Override
    public RefundOperationRecordEntity save(RefundOperationRecordEntity entity) {
        RefundOperationRecordDO recordDO = refundOperationRecordAssembler.domainToDO(entity);
        baseMapper.insert(recordDO);
        return refundOperationRecordAssembler.doToDomain(recordDO);
    }

    @Override
    public List<RefundOperationRecordEntity> listByRefundId(Long refundId) {
        LambdaQueryWrapper<RefundOperationRecordDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RefundOperationRecordDO::getRefundId, refundId).orderByDesc(RefundOperationRecordDO::getCreateTime);
        
        List<RefundOperationRecordDO> doList = baseMapper.selectList(wrapper);
        
        return doList.stream().map(refundOperationRecordAssembler::doToDomain).collect(Collectors.toList());
    }

    @Override
    public IPage<RefundOperationRecordEntity> pageQuery(Page<RefundOperationRecordEntity> page, Long refundId, Long operatorRoleCode) {
        // 构建DO分页对象
        Page<RefundOperationRecordDO> doPage = new Page<>(page.getCurrent(), page.getSize());

        // 构造查询条件
        LambdaQueryWrapper<RefundOperationRecordDO> wrapper = Wrappers.lambdaQuery();
        if (refundId != null) {
            wrapper.eq(RefundOperationRecordDO::getRefundId, refundId);
        }
        if (operatorRoleCode != null) {
            wrapper.eq(RefundOperationRecordDO::getOperatorRoleCode, operatorRoleCode);
        }
        wrapper.orderByDesc(RefundOperationRecordDO::getCreateTime);

        // 执行分页查询
        IPage<RefundOperationRecordDO> resultDoPage = baseMapper.selectPage(doPage, wrapper);

        // 转换为领域模型分页对象
        Page<RefundOperationRecordEntity> domainPage = new Page<>(page.getCurrent(), page.getSize());
        domainPage.setTotal(resultDoPage.getTotal());
        domainPage.setRecords(resultDoPage.getRecords().stream().map(refundOperationRecordAssembler::doToDomain).collect(Collectors.toList()));

        return domainPage;
    }
}