package com.newzkl.platform.base.biz.order.application.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.application.service.RefundOperationRecordService;
import com.newzkl.platform.base.biz.order.domain.service.RefundOperationRecordDomain;
import com.newzkl.platform.base.biz.order.model.enums.order.RefundEnum;
import com.newzkl.platform.base.biz.order.model.order.dto.RefundOperationRecord;
import com.newzkl.platform.base.biz.order.model.order.req.RefundOperationRecordCreateReq;
import com.newzkl.platform.base.biz.order.model.order.req.RefundOperationRecordPageReq;
import com.newzkl.platform.base.biz.order.model.order.req.RefundOperationRecordUpdateReq;
import com.newzkl.platform.base.biz.order.model.order.vo.RefundOperationRecordVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 售后操作记录 (协商记录) 应用服务实现
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.application.service.impl.RefundOperationRecordServiceImpl}。
 * 偏离说明: 旧实现每个方法用 try/catch 把任意异常包成 {@code OPERATE_FAIL} 并 {@code return null},
 * 会掩盖领域校验语义, 本实现改为异常直抛 (由全局异常处理器统一包装);
 * 旧 {@code @GlobalTransactional} (seata) 改为本地 {@code @Transactional}, 本切片无跨域写。</p>
 *
 * @author sijiwang
 * @since 2026-01-23
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RefundOperationRecordServiceImpl implements RefundOperationRecordService {

    private final RefundOperationRecordDomain refundOperationRecordDomain;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RefundOperationRecordVO create(RefundOperationRecordCreateReq req) {
        RefundOperationRecord record = TransferUtils.transfer(req, RefundOperationRecord::new);
        record.setBeforeState(RefundEnum.State.getByCode(req.getBeforeState()));
        record.setAfterState(RefundEnum.State.getByCode(req.getAfterState()));
        return toVO(refundOperationRecordDomain.create(record));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RefundOperationRecordVO update(RefundOperationRecordUpdateReq req) {
        RefundOperationRecord old = refundOperationRecordDomain.findById(req.getId());
        if (old == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "售后操作");
        }
        // 旧实现: 仅描述性字段可改, 业务主数据从原记录回填
        old.setOperationContent(req.getOperationContent());
        old.setRefundAmount(req.getRefundAmount());
        old.setFreightCompanyName(req.getFreightCompanyName());
        old.setFreightNo(req.getFreightNo());
        old.setReason(req.getReason());
        old.setExt(req.getExt());
        return toVO(refundOperationRecordDomain.update(old));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        refundOperationRecordDomain.delete(id);
    }

    @Override
    public List<RefundOperationRecordVO> listByRefundId(Long refundId) {
        return TransferUtils.transfers(refundOperationRecordDomain.listByRefundId(refundId),
                RefundOperationRecordVO::new);
    }

    @Override
    public Page<RefundOperationRecordVO> pageQuery(RefundOperationRecordPageReq req) {
        return TransferUtils.transferPage(refundOperationRecordDomain.pageQuery(req),
                RefundOperationRecordVO::new);
    }

    /**
     * 领域模型转视图对象 (null 安全)
     *
     * @param record 领域模型
     * @return 视图对象, 入参为 null 时返回 null
     */
    private RefundOperationRecordVO toVO(RefundOperationRecord record) {
        if (record == null) {
            return null;
        }
        return TransferUtils.transfer(record, RefundOperationRecordVO::new);
    }
}
