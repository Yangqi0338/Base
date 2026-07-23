package com.newzkl.platform.base.biz.finance.domain.adapt.repository;

import com.newzkl.platform.base.biz.finance.model.purse.req.AlterWithdrawStateReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.RollOutApplyAuditReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.RollOutApplyQuery;
import com.newzkl.platform.base.biz.finance.model.purse.req.TripartiteWithdrawRecordQuery;
import com.newzkl.platform.base.biz.finance.model.purse.vo.ConfigWithdrawVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.RollOutApplyVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.WithdrawRecordVO;

import java.util.List;

/**
 * @author niu
 * @description: 提现业务数据服务接口
 * @date 2023/12/23 13:59
 */
public interface WithdrawRepository {

    /**
     * 查询默认提现配置
     *
     * @return
     */
    ConfigWithdrawVO defaultWithdrawConfig();

    /**
     * 更新提现配置
     *
     * @return
     */
    void alterWithdrawConfig(ConfigWithdrawVO incomeWithdraw);

    /**
     * 转出申请
     *
     * @param req
     * @return
     */
    void rollOutApply(RollOutApplyVO req);

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
     * @return
     */
    Long saveAccountWithdraw(WithdrawRecordVO req);

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

}
