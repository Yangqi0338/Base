package com.newzkl.platform.base.biz.order.domain.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.RefundOperationRecordRepository;
import com.newzkl.platform.base.biz.order.domain.service.RefundOperationRecordDomain;
import com.newzkl.platform.base.biz.order.model.order.dto.RefundOperationRecord;
import com.newzkl.platform.base.biz.order.model.order.req.RefundOperationRecordPageReq;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 售后操作记录 (协商记录) 领域服务实现。
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.domain.refund.service.impl.RefundOperationRecordDomainServiceImpl}。
 * 旧 {@code org.springframework.util.Assert} 校验改抛 {@code PlatformException},
 * 创建/更新时间交由 MyBatis-Plus 自动填充 (BaseDO), 不再在领域层手工赋值。</p>
 *
 * @author sijiwang
 * @since 2026-01-23
 */
@Service
@RequiredArgsConstructor
public class RefundOperationRecordDomainImpl implements RefundOperationRecordDomain {

    private final RefundOperationRecordRepository refundOperationRecordRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RefundOperationRecord create(RefundOperationRecord record) {
        if (record == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "售后操作记录不能为空");
        }
        if (record.getRefundId() == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "售后单ID不能为空");
        }
        if (record.getOperatorRoleCode() == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "操作方角色编码不能为空");
        }
        if (StrUtil.isBlank(record.getOperatorClient())) {
            throw new PlatformException(BaseErrorCode.PARAM, "操作方客户端类型不能为空");
        }
        if (record.getAfterState() == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "操作后状态不能为空");
        }
        if (StrUtil.isBlank(record.getOperationContent())) {
            throw new PlatformException(BaseErrorCode.PARAM, "操作内容描述不能为空");
        }
        if (record.getId() != null) {
            throw new PlatformException(BaseErrorCode.PARAM, "新增时ID必须为空");
        }
        return refundOperationRecordRepository.save(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RefundOperationRecord update(RefundOperationRecord record) {
        if (record == null || record.getId() == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "修改时ID不能为空");
        }
        if (!refundOperationRecordRepository.existsById(record.getId())) {
            throw new PlatformException(BaseErrorCode.NODATA, "售后操作");
        }
        return refundOperationRecordRepository.updateById(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        if (id == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "删除时ID不能为空");
        }
        if (!refundOperationRecordRepository.existsById(id)) {
            throw new PlatformException(BaseErrorCode.NODATA, "售后操作");
        }
        refundOperationRecordRepository.deleteById(id);
    }

    @Override
    public RefundOperationRecord findById(Long id) {
        if (id == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "查询时ID不能为空");
        }
        return refundOperationRecordRepository.findById(id);
    }

    @Override
    public List<RefundOperationRecord> listByRefundId(Long refundId) {
        if (refundId == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "售后单ID不能为空");
        }
        return refundOperationRecordRepository.listByRefundId(refundId);
    }

    @Override
    public List<RefundOperationRecord> listBySpuOrderNo(String spuOrderNo) {
        if (StrUtil.isBlank(spuOrderNo)) {
            throw new PlatformException(BaseErrorCode.PARAM, "SPU订单号不能为空");
        }
        return refundOperationRecordRepository.listBySpuOrderNo(spuOrderNo);
    }

    @Override
    public Page<RefundOperationRecord> pageQuery(RefundOperationRecordPageReq query) {
        if (ObjectUtil.isNull(query)) {
            throw new PlatformException(BaseErrorCode.PARAM, "分页参数不能为空");
        }
        return refundOperationRecordRepository.pageByQuery(query);
    }
}
