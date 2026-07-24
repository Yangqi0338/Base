package com.newzkl.platform.base.biz.finance.action.controller;

import com.newzkl.platform.base.biz.finance.domain.purse.service.AccountPurseDomain;
import com.newzkl.platform.base.biz.finance.domain.purse.service.TripartitePurseDomain;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountPurseAlterRecordQuery;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountPurseQuery;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountTripartitePurseQuery;
import com.newzkl.platform.base.biz.finance.model.purse.req.BatchAccountPurseQuery;
import com.newzkl.platform.base.biz.finance.model.purse.res.BatchQueryAccountPurseRes;
import com.newzkl.platform.base.biz.finance.model.purse.res.TotalSupplierSettleDataRes;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountPurseAlterRecordVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountPurseVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountTripartitePurseVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 客户账户控制器。
 *
 * @author niu
 */
@RestController
@RequestMapping("/purse")
@RequiredArgsConstructor
public class PurseController {

    private final AccountPurseDomain accountPurseDomain;
    private final TripartitePurseDomain tripartitePurseDomain;

    /**
     * 查询客户账户。
     *
     * @param req 客户账户查询
     * @return 客户账户列表
     */
    @PostMapping("/queryAccountPurse")
    public ScmResult<List<AccountPurseVO>> queryAccountPurse(@RequestBody AccountPurseQuery req) {
        req.setAccountId(SecurityUtils.getAccountId());
        return ScmResult.success(accountPurseDomain.queryAccountPurse(req));
    }

    /**
     * 查询采购金账户。
     *
     * @param req 客户账户查询
     * @return 采购金账户
     */
    @PostMapping("/queryAccountPurchasePurse")
    public ScmResult<AccountPurseVO> queryAccountPurchasePurse(@RequestBody AccountPurseQuery req) {
        req.setAccountId(SecurityUtils.getAccountId());
        return ScmResult.success(accountPurseDomain.queryAccountPurchasePurse(req));
    }

    /**
     * 批量查询客户收益。
     *
     * @param req 批量客户账户查询
     * @return 批量收益列表
     */
    @PostMapping("/batchQueryAccountEarning")
    public ScmResult<List<BatchQueryAccountPurseRes>> batchQueryAccountEarning(@RequestBody BatchAccountPurseQuery req) {
        return ScmResult.success(accountPurseDomain.batchQueryAccountEarning(req));
    }

    /**
     * 查询三方账户。
     *
     * @return 三方账户
     * @deprecated [DEAD-ENDPOINT #128 审计 2026-07-24] 前端7仓零引用 + 后端无caller。
     *   待删: 若项目完成后仍未被接线调用, 则删除本方法。详见
     *   docs/planning/dead-endpoint-audit/README.md。
     */
    @Deprecated
    @PostMapping("/queryAccountTripartitePurse")
    public ScmResult<AccountTripartitePurseVO> queryAccountTripartitePurse() {
        AccountTripartitePurseVO accountTripartitePurseVO = tripartitePurseDomain.queryAccountTripartitePurse(SecurityUtils.getAccountId());
        if (accountTripartitePurseVO != null) {
            accountTripartitePurseVO.subBankNo();
        }
        return ScmResult.success(accountTripartitePurseVO);
    }

    /**
     * 查询三方账户分页列表。
     *
     * @param query 三方账户查询
     * @return 三方账户列表
     * @deprecated [DEAD-ENDPOINT #128 审计 2026-07-24] 前端7仓零引用 + 后端无caller。
     *   待删: 若项目完成后仍未被接线调用, 则删除本方法。详见
     *   docs/planning/dead-endpoint-audit/README.md。
     */
    @Deprecated
    @PostMapping("/queryTripartitePursePage")
    public ScmResult<List<AccountTripartitePurseVO>> queryTripartitePursePage(@RequestBody @Valid AccountTripartitePurseQuery query) {
        return ScmResult.success(tripartitePurseDomain.queryPageAccountTripartitePurse(query));
    }

    /**
     * 查询客户账户变动记录。
     *
     * @param req 变动记录查询
     * @return 变动记录列表
     */
    @PostMapping("/queryAccountPurseAlterRecords")
    public ScmResult<List<AccountPurseAlterRecordVO>> queryAccountPurseAlterRecords(@RequestBody AccountPurseAlterRecordQuery req) {
        if (req.getAccountId() == null) {
            req.setAccountId(SecurityUtils.getAccountId());
        }
        return ScmResult.success(accountPurseDomain.queryAccountPurseAlterRecords(req));
    }

    /**
     * 查询供应商累计结算数据。
     *
     * @return 供应商累计结算数据
     */
    @PostMapping("/querySupplierSettleData")
    public ScmResult<TotalSupplierSettleDataRes> querySupplierSettleData() {
        return ScmResult.success(accountPurseDomain.querySupplierSettleData());
    }

    /**
     * 查询动账明细。
     *
     * @param req 变动记录查询
     * @return 动账明细列表
     * @deprecated [DEAD-ENDPOINT #128 审计 2026-07-24] 前端7仓零引用 + 后端无caller。
     *   待删: 若项目完成后仍未被接线调用, 则删除本方法。详见
     *   docs/planning/dead-endpoint-audit/README.md。
     */
    @Deprecated
    @PostMapping("/accountMovementDetails")
    public ScmResult<List<AccountPurseAlterRecordVO>> accountMovementDetails(@RequestBody AccountPurseAlterRecordQuery req) {
        if (req.getAccountId() == null) {
            req.setAccountId(SecurityUtils.getAccountId());
        }
        return ScmResult.success(accountPurseDomain.queryAccountPurseAlterRecords(req));
    }
}
