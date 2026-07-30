package com.newzkl.platform.base.biz.order.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.RefundOperationRecordRepository;
import com.newzkl.platform.base.biz.order.infrastructure.dao.RefundOperationRecordDAO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.RefundOperationRecordDO;
import com.newzkl.platform.base.biz.order.model.order.dto.RefundOperationRecord;
import com.newzkl.platform.base.biz.order.model.order.req.RefundOperationRecordPageReq;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * 售后操作记录 仓储实现
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.infrastructure.repository.RefundOperationRecordRepositoryImpl}。
 * 旧 MapStruct assembler 改为 {@code TransferUtils}; 旧分页三参签名收敛为
 * {@code RefundOperationRecordPageReq} 单参, 检索条件与旧实现一致 (仅 refundId / operatorRoleCode 参与筛选)。</p>
 *
 * @author sijiwang
 * @since 2026-01-23
 */
@Repository
@RequiredArgsConstructor
public class RefundOperationRecordRepositoryImpl implements RefundOperationRecordRepository {

    private final RefundOperationRecordDAO refundOperationRecordDAO;

    @Override
    public RefundOperationRecord save(RefundOperationRecord record) {
        RefundOperationRecordDO recordDO = TransferUtils.transfer(record, RefundOperationRecordDO::new);
        refundOperationRecordDAO.insert(recordDO);
        record.setId(recordDO.getId());
        return record;
    }

    @Override
    public RefundOperationRecord updateById(RefundOperationRecord record) {
        RefundOperationRecordDO recordDO = TransferUtils.transfer(record, RefundOperationRecordDO::new);
        refundOperationRecordDAO.updateById(recordDO);
        return record;
    }

    @Override
    public boolean deleteById(Long id) {
        return refundOperationRecordDAO.deleteById(id) > 0;
    }

    @Override
    public RefundOperationRecord findById(Long id) {
        RefundOperationRecordDO recordDO = refundOperationRecordDAO.selectById(id);
        if (recordDO == null) {
            return null;
        }
        return TransferUtils.transfer(recordDO, RefundOperationRecord::new);
    }

    @Override
    public List<RefundOperationRecord> listByRefundId(Long refundId) {
        LambdaQueryWrapper<RefundOperationRecordDO> wrapper = new LambdaQueryWrapper<RefundOperationRecordDO>()
                .eq(RefundOperationRecordDO::getRefundId, refundId)
                .orderByDesc(RefundOperationRecordDO::getCreateTime);
        return transfers(refundOperationRecordDAO.selectList(wrapper));
    }

    @Override
    public List<RefundOperationRecord> listBySpuOrderNo(String spuOrderNo) {
        LambdaQueryWrapper<RefundOperationRecordDO> wrapper = new LambdaQueryWrapper<RefundOperationRecordDO>()
                .eq(RefundOperationRecordDO::getSpuOrderNo, spuOrderNo)
                .orderByDesc(RefundOperationRecordDO::getCreateTime);
        return transfers(refundOperationRecordDAO.selectList(wrapper));
    }

    @Override
    public Page<RefundOperationRecord> pageByQuery(RefundOperationRecordPageReq query) {
        Page<RefundOperationRecordDO> doPage = new Page<>(query.getCurrent(), query.getSize());
        LambdaQueryWrapper<RefundOperationRecordDO> wrapper = new LambdaQueryWrapper<>();
        if (query.getRefundId() != null) {
            wrapper.eq(RefundOperationRecordDO::getRefundId, query.getRefundId());
        }
        if (query.getOperatorRoleCode() != null) {
            wrapper.eq(RefundOperationRecordDO::getOperatorRoleCode, query.getOperatorRoleCode());
        }
        wrapper.orderByDesc(RefundOperationRecordDO::getCreateTime);
        return TransferUtils.transferPage(refundOperationRecordDAO.selectPage(doPage, wrapper),
                RefundOperationRecord::new);
    }

    @Override
    public boolean existsById(Long id) {
        return refundOperationRecordDAO.exists(new LambdaQueryWrapper<RefundOperationRecordDO>()
                .eq(RefundOperationRecordDO::getId, id));
    }

    /**
     * DO 列表转领域模型列表 (空集合安全)
     *
     * @param doList DO 列表
     * @return 领域模型列表, 永不为 null
     */
    private List<RefundOperationRecord> transfers(List<RefundOperationRecordDO> doList) {
        if (doList == null || doList.isEmpty()) {
            return Collections.emptyList();
        }
        return TransferUtils.transfers(doList, RefundOperationRecord::new);
    }
}
