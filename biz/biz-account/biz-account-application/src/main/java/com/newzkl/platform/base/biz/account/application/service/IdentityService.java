package com.newzkl.platform.base.biz.account.application.service;


import com.newzkl.platform.base.biz.account.model.vo.ServiceFeeConfigVO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;

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
    void updateAuditState(Long accountId, AccountEnum.Identity identity);

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
     * 查询服务费
     *
     * @param channelId
     */
    ServiceFeeConfigVO queryServiceFeeConfig(Long channelId);


}
