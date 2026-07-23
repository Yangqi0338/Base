package com.newzkl.platform.base.biz.goods.application.goods.service.approval.impl;

import com.newzkl.platform.base.biz.goods.application.goods.service.approval.IWorkTableWorkflowService;
import com.newzkl.platform.base.biz.goods.application.goods.service.approval.utils.WorktableFactory;
import com.newzkl.platform.base.biz.goods.domain.spu.service.IWorkTableWorkflowDomain;
import com.newzkl.platform.base.biz.goods.model.goods.req.audit.ApprovalResultReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.audit.AuditDataWorkTableVO;
import com.newzkl.platform.base.biz.goods.model.enums.AuditEnum;
import com.newzkl.platform.base.biz.goods.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkTableWorkflowServiceImpl implements IWorkTableWorkflowService {

    private final IWorkTableWorkflowDomain workTableWorkflowDomain;
    private final WorktableFactory worktableFactory;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approval(ApprovalResultReq req) {
        AuditDataWorkTableVO messageData = workTableWorkflowDomain.detail(req.getId());
        if (AuditEnum.State.SUCCESS.name().equals(req.getState())) {
            //审核成功处理
            //修改审批数据
            workTableWorkflowDomain.editBusinessData(req.getId(), req.getEditCommand());
            if (SpuEnum.OperateType.ADD.getCode().equals(messageData.getOperateType())) {
                worktableFactory.getPolicy(messageData.getOperateTarget()).add(messageData.getSpuEditInfoJson(), messageData.getSkuSalePriceJson());
            } else if (SpuEnum.OperateType.UPDATE.getCode().equals(messageData.getOperateType())) {
                worktableFactory.getPolicy(messageData.getOperateTarget()).update(messageData.getSpuEditInfoJson(), messageData.getSkuSalePriceJson());
            } else if (SpuEnum.OperateType.DELETE.getCode().equals(messageData.getOperateType())) {
                worktableFactory.getPolicy(messageData.getOperateTarget()).delete(messageData.getSpuEditInfoJson(), messageData.getSkuSalePriceJson());
            } else {
                throw new ScmException(BaseErrorCode.PARAM);
            }
        }
    }
}
