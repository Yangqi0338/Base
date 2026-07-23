package com.newzkl.platform.base.biz.goods.application.goods.service.approval;

import com.newzkl.platform.base.biz.goods.model.goods.req.audit.ApprovalResultReq;

public interface WorkTableWorkflowService {

    void approval(ApprovalResultReq req);

}
