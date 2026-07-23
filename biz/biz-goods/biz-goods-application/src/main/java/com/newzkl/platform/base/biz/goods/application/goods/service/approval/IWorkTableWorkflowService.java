package com.newzkl.platform.base.biz.goods.application.goods.service.approval;

import com.newzkl.platform.base.biz.goods.model.goods.req.audit.ApprovalResultReq;

public interface IWorkTableWorkflowService {

    void approval(ApprovalResultReq req);

}
