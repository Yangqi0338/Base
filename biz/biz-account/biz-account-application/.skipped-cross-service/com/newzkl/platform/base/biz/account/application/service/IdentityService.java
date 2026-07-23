package com.newzkl.platform.base.biz.account.application.service;


import com.zkl.scm.finance.model.account.vo.ServiceFeeConfigVO;
import com.zkl.scm.finance.model.pay.vo.PromiseFlowVO;
import com.newzkl.platform.base.biz.account.model.enums.identity.RoleEnum;
import com.newzkl.platform.base.biz.account.model.req.OperatorReq;
import com.newzkl.platform.base.biz.account.model.req.RoleApplyCommand;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/4/814:28
 */
public interface IdentityService {
    /**
     * 申请角色
     *
     * @param roleApplyCommand
     */
    Long applyRole(RoleApplyCommand roleApplyCommand);

    void updateAuditState(Long accountId, RoleEnum.CompanyRole role);

    /**
     * 提交保证金缴纳流水
     *
     * @param promiseFlowVO
     */
    Long submitPromiseFlow(PromiseFlowVO promiseFlowVO);

    /**
     * 服务费修改
     *
     * @param accountId
     * @param serviceFeeConfigVO
     */
    void serviceFeeConfigEdit(Long accountId, ServiceFeeConfigVO serviceFeeConfigVO);

    /**
     * 渠道商上级交易师修改
     *
     * @param channelId
     * @param dealerId
     */
    void channelUpEdit(Long channelId, Long dealerId);

    /**
     * 运营商修改
     *
     * @param operatorCommand
     */
    void operatorEdit(OperatorReq operatorCommand);

    /**
     * 查询服务费
     *
     * @param channelId
     */
    ServiceFeeConfigVO queryServiceFeeConfig(Long channelId);


}
