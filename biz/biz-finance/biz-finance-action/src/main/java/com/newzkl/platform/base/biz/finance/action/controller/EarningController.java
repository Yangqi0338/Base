package com.newzkl.platform.base.biz.finance.action.controller;

import com.newzkl.platform.base.biz.finance.domain.earnings.service.AccountContributeDomain;
import com.newzkl.platform.base.biz.finance.domain.earnings.service.EarningDomain;
import com.newzkl.platform.base.biz.finance.model.earnings.req.AccountContributeQuery;
import com.newzkl.platform.base.biz.finance.model.earnings.req.EarningRecordQuery;
import com.newzkl.platform.base.biz.finance.model.earnings.res.AccountContributeRes;
import com.newzkl.platform.base.biz.finance.model.earnings.res.AppEarningRecordRes;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.EarningRecordVO;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.TotalEarningVO;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 分润记录控制器。
 *
 * @author niu
 */
@RestController
@RequestMapping("/earningRecord")
@RequiredArgsConstructor
public class EarningController {

    private final EarningDomain earningDomain;
    private final AccountContributeDomain accountContributeDomain;

    /**
     * 查询分润记录。
     *
     * @param req 分润记录查询
     * @return 分润记录列表
     */
    @PostMapping("/queryEarningRecord")
    public PlatformResult<List<EarningRecordVO>> queryEarningRecord(@RequestBody EarningRecordQuery req) {
        if (req.getAccountId() == null) {
            req.setAccountId(SecurityUtils.getAccountId());
            req.setRoleId(RoleEnum.CompanyRole.getByCode(SecurityUtils.getRoleId()));
        }
        return PlatformResult.success(earningDomain.queryEarningRecord(req));
    }

    /**
     * APP 查询分润记录。
     *
     * @param req 分润记录查询
     * @return APP 分润记录列表
     * @deprecated [DEAD-ENDPOINT #128 审计 2026-07-24] 前端7仓零引用 + 后端无caller。
     *   待删: 若项目完成后仍未被接线调用, 则删除本方法。详见
     *   docs/planning/dead-endpoint-audit/README.md。
     */
    @Deprecated
    @PostMapping("/queryAppEarningRecord")
    public PlatformResult<List<AppEarningRecordRes>> queryAppEarningRecord(@RequestBody EarningRecordQuery req) {
        if (req.getAccountId() == null) {
            req.setAccountId(SecurityUtils.getAccountId());
        }
        return PlatformResult.success(earningDomain.queryAppEarningRecord(req));
    }

    /**
     * 查询累计分润数据。
     *
     * @return 累计分润数据
     */
    @PostMapping("/queryTotalEarning")
    public PlatformResult<TotalEarningVO> queryTotalEarning() {
        return PlatformResult.success(earningDomain.queryTotalEarning());
    }

    /**
     * 查询客户下级贡献数据。
     *
     * @param req 客户贡献查询
     * @return 客户贡献列表
     * @deprecated [DEAD-ENDPOINT #128 审计 2026-07-24] 前端7仓零引用 + 后端无caller。
     *   待删: 若项目完成后仍未被接线调用, 则删除本方法。详见
     *   docs/planning/dead-endpoint-audit/README.md。
     */
    @Deprecated
    @PostMapping("/queryAccountContribute")
    public PlatformResult<List<AccountContributeRes>> queryAccountContribute(@RequestBody AccountContributeQuery req) {
        if (req.getAccountId() == null) {
            req.setAccountId(SecurityUtils.getAccountId());
        }
        return PlatformResult.success(accountContributeDomain.queryAccountContribute(req));
    }
}
