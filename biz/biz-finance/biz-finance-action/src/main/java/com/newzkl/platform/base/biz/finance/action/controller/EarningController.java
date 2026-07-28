package com.newzkl.platform.base.biz.finance.action.controller;

import com.newzkl.platform.base.biz.finance.domain.earnings.service.EarningDomain;
import com.newzkl.platform.base.biz.finance.model.earnings.req.EarningRecordQuery;
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
     * 查询累计分润数据。
     *
     * @return 累计分润数据
     */
    @PostMapping("/queryTotalEarning")
    public PlatformResult<TotalEarningVO> queryTotalEarning() {
        return PlatformResult.success(earningDomain.queryTotalEarning());
    }
}
