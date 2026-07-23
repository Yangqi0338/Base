package com.newzkl.platform.base.biz.finance.action.controller;

import com.newzkl.platform.base.biz.finance.application.purse.service.WithdrawService;
import com.newzkl.platform.base.biz.finance.domain.purse.service.WithdrawDomain;
import com.newzkl.platform.base.biz.finance.model.enums.user.identity.RoleEnum;
import com.newzkl.platform.base.biz.finance.model.pay.res.huifu.HuiFuRollOutRes;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountWithdrawReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.RollOutApplyAuditReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.RollOutApplyQuery;
import com.newzkl.platform.base.biz.finance.model.purse.req.RollOutApplyReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.TripartiteWithdrawRecordQuery;
import com.newzkl.platform.base.biz.finance.model.purse.vo.ConfigWithdrawVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.RollOutApplyVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.WithdrawRecordVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 提现业务控制器。
 *
 * @author niu
 */
@RestController
@RequestMapping("/withdraw")
@RequiredArgsConstructor
public class WithdrawController {

    private final WithdrawDomain withdrawDomain;
    private final WithdrawService withdrawService;

    /**
     * 查询提现配置。
     *
     * @return 提现配置
     */
    @PostMapping("/queryWithdrawConfig")
    public ScmResult<ConfigWithdrawVO> queryWithdrawConfig() {
        return ScmResult.success(withdrawDomain.defaultWithdrawConfig());
    }

    /**
     * 更新提现配置。
     *
     * @param saveCommand 提现配置
     * @return 成功结果
     */
    @PostMapping("/alterWithdrawConfig")
    public ScmResult<Object> alterWithdrawConfig(@RequestBody ConfigWithdrawVO saveCommand) {
        withdrawDomain.alterWithdrawConfig(saveCommand);
        return ScmResult.success();
    }

    /**
     * 转出申请。
     *
     * @param req 转出申请
     * @return 成功结果
     */
    @PostMapping("/rollOutApply")
    public ScmResult<Object> rollOutApply(@RequestBody @Valid RollOutApplyReq req) {
        withdrawService.rollOutApply(req);
        return ScmResult.success();
    }

    /**
     * 转出申请审核。
     *
     * @param req 审核请求
     * @return 汇付转出结果
     */
    @PostMapping("/rollOutApplyAudit")
    public ScmResult<HuiFuRollOutRes> rollOutApplyAudit(@RequestBody RollOutApplyAuditReq req) {
        return ScmResult.success(withdrawService.rollOutApplyAudit(req));
    }

    /**
     * 查询转出申请。
     *
     * @param req 转出申请查询
     * @return 转出申请列表
     */
    @PostMapping("/queryWithdrawRecords")
    public ScmResult<List<RollOutApplyVO>> queryWithdrawRecords(@RequestBody RollOutApplyQuery req) {
        return ScmResult.success(withdrawDomain.queryRollOutApplyPage(req));
    }

    /**
     * 客户查询转出申请。
     *
     * @param req 转出申请查询
     * @return 转出申请列表
     */
    @PostMapping("/queryClientWithdrawRecords")
    public ScmResult<List<RollOutApplyVO>> queryClientWithdrawRecords(@RequestBody RollOutApplyQuery req) {
        req.setAccountId(SecurityUtils.getAccountId());
        if (req.getPurseType() == null) {
            return ScmResult.fail();
        }
        return ScmResult.success(withdrawDomain.queryRollOutApplyPage(req));
    }

    /**
     * 因打款余额不足, 可更新转出 ID 重新发起。
     *
     * @param id 转出申请主键
     * @return 成功结果
     */
    @PostMapping("/alterRollOutApplyId/{id}")
    public ScmResult<Object> alterRollOutApplyId(@PathVariable Long id) {
        withdrawDomain.alterRollOutApplyId(id);
        return ScmResult.success();
    }

    /**
     * 客户三方账户提现。
     *
     * @param req 提现请求
     * @return 处理结果
     */
    @PostMapping("/accountTripartiteWithdraw")
    public ScmResult<Object> accountTripartiteWithdraw(@RequestBody AccountWithdrawReq req) {
        return withdrawService.accountTripartiteWithdraw(req);
    }

    /**
     * 查询提现记录。
     *
     * @param req 三方提现记录查询
     * @return 提现记录列表
     */
    @PostMapping("/queryTripartiteWithdrawRecordList")
    public ScmResult<List<WithdrawRecordVO>> queryTripartiteWithdrawRecordList(@RequestBody TripartiteWithdrawRecordQuery req) {
        if (req.getAccountId() == null) {
            req.setAccountId(SecurityUtils.getAccountId());
        }
        return ScmResult.success(withdrawDomain.queryTripartiteWithdrawRecordList(req));
    }

    /**
     * 查询提现金额。
     *
     * @return 提现金额
     */
    @PostMapping("/queryWithdrawAmount")
    public ScmResult<Object> queryWithdrawAmount() {
        RoleEnum.CompanyRole role = RoleEnum.CompanyRole.getByCode(SecurityUtils.getRoleId());
        return ScmResult.success(withdrawDomain.queryWithdrawAmount(role, SecurityUtils.getAccountId()));
    }
}
