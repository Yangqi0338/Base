package com.newzkl.platform.base.biz.finance.action.controller;

import com.newzkl.platform.base.biz.finance.domain.earnings.service.AccountContributeDomain;
import com.newzkl.platform.base.biz.finance.domain.earnings.service.EarningDomain;
import com.newzkl.platform.base.biz.finance.model.earnings.req.AccountContributeQuery;
import com.newzkl.platform.base.biz.finance.model.earnings.req.EarningRecordQuery;
import com.newzkl.platform.base.biz.finance.model.earnings.res.AccountContributeRes;
import com.newzkl.platform.base.biz.finance.model.earnings.res.AppEarningRecordRes;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.EarningRecordVO;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.TotalEarningVO;
import com.newzkl.platform.base.biz.finance.model.enums.user.identity.RoleEnum;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
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
    public ScmResult<List<EarningRecordVO>> queryEarningRecord(@RequestBody EarningRecordQuery req) {
        if (req.getAccountId() == null) {
            req.setAccountId(SecurityUtils.getAccountId());
            req.setRoleId(RoleEnum.CompanyRole.getByCode(SecurityUtils.getRoleId()));
        }
        return ScmResult.success(earningDomain.queryEarningRecord(req));
    }

    /**
     * APP 查询分润记录。
     *
     * @param req 分润记录查询
     * @return APP 分润记录列表
     */
    @PostMapping("/queryAppEarningRecord")
    public ScmResult<List<AppEarningRecordRes>> queryAppEarningRecord(@RequestBody EarningRecordQuery req) {
        if (req.getAccountId() == null) {
            req.setAccountId(SecurityUtils.getAccountId());
        }
        return ScmResult.success(earningDomain.queryAppEarningRecord(req));
    }

    /**
     * 查询累计分润数据。
     *
     * @return 累计分润数据
     */
    @PostMapping("/queryTotalEarning")
    public ScmResult<TotalEarningVO> queryTotalEarning() {
        return ScmResult.success(earningDomain.queryTotalEarning());
    }

    /**
     * 查询客户下级贡献数据。
     *
     * @param req 客户贡献查询
     * @return 客户贡献列表
     */
    @PostMapping("/queryAccountContribute")
    public ScmResult<List<AccountContributeRes>> queryAccountContribute(@RequestBody AccountContributeQuery req) {
        if (req.getAccountId() == null) {
            req.setAccountId(SecurityUtils.getAccountId());
        }
        return ScmResult.success(accountContributeDomain.queryAccountContribute(req));
    }
}
