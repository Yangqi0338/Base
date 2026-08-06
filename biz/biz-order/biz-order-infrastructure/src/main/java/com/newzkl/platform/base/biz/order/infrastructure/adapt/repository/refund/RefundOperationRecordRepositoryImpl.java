package com.newzkl.platform.base.biz.order.infrastructure.adapt.repository.refund;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.RefundOperationRecordRepository;
import com.newzkl.platform.base.biz.order.infrastructure.dao.refund.RefundOperationRecordDAO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.RefundOperationRecordDO;
import com.newzkl.platform.base.biz.order.model.dto.RefundOperationRecordDTO;
import com.newzkl.platform.base.biz.order.model.req.query.RefundOperationRecordQuery;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 售后操作记录 仓储实现类
 *
 * @author 开发者名称
 * @since 2026-01-23
 */
@Repository
public class RefundOperationRecordRepositoryImpl implements RefundOperationRecordRepository {

    @Autowired
    private RefundOperationRecordDAO refundOperationRecordDAO;

    @Override
    public Long save(RefundOperationRecordDTO entity) {
        RefundOperationRecordDO recordDO = TransferUtils.transfer(entity, RefundOperationRecordDO.class);
        refundOperationRecordDAO.insert(recordDO);
        entity.setId(recordDO.getId());
        return recordDO.getId();
    }

    @Override
    public List<RefundOperationRecordDTO> listByRefundId(Long refundId) {
        LambdaQueryWrapper<RefundOperationRecordDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RefundOperationRecordDO::getRefundId, refundId);
        
        List<RefundOperationRecordDO> doList = refundOperationRecordDAO.selectList(wrapper);
        
        return TransferUtils.transfers(doList, RefundOperationRecordDTO.class);
    }

    @Override
    public Page<RefundOperationRecordDTO> pageQuery(RefundOperationRecordQuery query) {
        // 执行分页查询
        Page<RefundOperationRecordDO> resultDoPage = refundOperationRecordDAO.selectPage(RepositorySupport.page(query), refundOperationRecordDAO.getLw(query));
        return TransferUtils.transferPage(resultDoPage, RefundOperationRecordDTO.class);
    }
}