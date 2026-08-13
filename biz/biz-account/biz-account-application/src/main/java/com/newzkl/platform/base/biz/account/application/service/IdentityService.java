package com.newzkl.platform.base.biz.account.application.service;


import com.newzkl.platform.base.biz.account.model.vo.ServiceFeeConfigVO;
import com.newzkl.platform.base.biz.account.model.vo.PromiseFlowVO;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/4/814:28
 */
public interface IdentityService {

    /**
     * 更新角色审核状态
     *
     * @param accountId 账号 ID
     * @param role      申请角色
     */
    void updateAuditState(Long accountId, RoleEnum.CompanyRole role);

//    /**
//     * 提交保证金缴纳流水
//     *
//     * @param promiseFlowVO
//     */
//    Long submitPromiseFlow(PromiseFlowVO promiseFlowVO);

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

    /**
     * 查询服务费
     *
     * @param channelId
     */
    ServiceFeeConfigVO queryServiceFeeConfig(Long channelId);


}
