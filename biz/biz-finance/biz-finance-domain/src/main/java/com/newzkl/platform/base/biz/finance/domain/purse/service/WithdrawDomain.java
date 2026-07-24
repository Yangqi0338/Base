package com.newzkl.platform.base.biz.finance.domain.purse.service;


import com.newzkl.platform.base.biz.finance.model.purse.req.*;
import com.newzkl.platform.base.biz.finance.model.purse.vo.*;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;

import java.util.List;

/**
 * @author niu
 * @description: 提现接口
 * @date 2023/12/23 13:55
 */
public interface WithdrawDomain {

    /**
     * 查询运营商默认提现配置
     *
     * @return
     */
    ConfigWithdrawVO defaultWithdrawConfig();

    /**
     * 更新提现配置
     *
     * @return
     */
    void alterWithdrawConfig(ConfigWithdrawVO saveCommand);

    /**
     * 转出申请
     *
     * @param req
     * @return
     */
    void rollOutApply(RollOutApplyReq req);

    /**
     * 转出申请审核
     *
     * @param req
     * @return
     */
    boolean rollOutApplyAudit(RollOutApplyAuditReq req);

    /**
     * 查询转出申请
     *
     * @param req
     * @return
     */
    List<RollOutApplyVO> queryRollOutApplyPage(RollOutApplyQuery req);

    /**
     * 查询转出申请根据id
     *
     * @param id
     * @return
     */
    RollOutApplyVO rollOutApplyDetail(Long id);

    /**
     * 更新转出申请主键流水号 三方打款余额不足时使用
     *
     * @param id
     */
    void alterRollOutApplyId(Long id);

    /**
     * 更新转出申请三方到账结果
     *
     * @param applyId
     * @param tripartiteState
     * @param tripartiteTradeNo
     * @return
     */
    boolean alterRollOutTripartiteState(Long applyId, Integer tripartiteState, String tripartiteTradeNo);

    /**
     * 保存提现记录
     *
     * @param accountId
     * @param amount
     * @return
     */
    Long saveAccountWithdraw(Long accountId, Integer amount);

    /**
     * 更新提现状态
     *
     * @param req
     * @return
     */
    boolean alterWithdrawState(AlterWithdrawStateReq req);

    /**
     * 查询提现记录
     *
     * @param id
     * @return
     */
    WithdrawRecordVO queryTripartiteWithdrawRecord(Long id);

    /**
     * 查询提现记录
     *
     * @param req
     * @return
     */
    List<WithdrawRecordVO> queryTripartiteWithdrawRecordList(TripartiteWithdrawRecordQuery req);

    /**
     * 查询转出申请
     *
     * @param req
     * @return
     */
    List<RollOutApplyVO> queryWithdrawRecords(RollOutApplyQuery req);

    /**
     * 查询提现金额
     * @param role
     * @param accountId
     */
    WithdrawAmountVO queryWithdrawAmount(RoleEnum.CompanyRole role, Long accountId);

    /**
     * 提现申请记录导出
     *
     * @param req
     */
    List<RollOutApplyExportVO> withdrawRecordsExport(RollOutApplyQuery req);
}
