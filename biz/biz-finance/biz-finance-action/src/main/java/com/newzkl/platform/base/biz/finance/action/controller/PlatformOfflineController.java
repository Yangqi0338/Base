package com.newzkl.platform.base.biz.finance.action.controller;

import com.newzkl.platform.base.biz.finance.application.purse.service.PurseService;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.biz.finance.model.purse.req.AmountDistributionReq;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 平台采购金分配控制器。
 *
 * @author niu
 */
@RestController
@RequestMapping("/PlatformOffline")
@RequiredArgsConstructor
public class PlatformOfflineController {

    private final PurseService purseService;

    /**
     * 平台给运营商分配采购金。
     *
     * @param req 分配请求
     * @return 处理结果
     */
    @PostMapping("/platformToOperator")
    public ScmResult<Object> platformToOperator(@RequestBody AmountDistributionReq req) {
        if (!RoleEnum.CompanyRole.PLATFORM.getCode().equals(SecurityUtils.getRoleId())) {
            return ScmResult.fail();
        }
        return purseService.platformToOperator(req);
    }

    /**
     * 运营商给渠道商分配采购金。
     *
     * @param req 分配请求
     * @return 处理结果
     */
    @PostMapping("/operatorToChannel")
    public ScmResult<Object> operatorToChannel(@RequestBody AmountDistributionReq req) {
        if (!RoleEnum.CompanyRole.OPERATOR.getCode().equals(SecurityUtils.getRoleId())) {
            return ScmResult.fail();
        }
        return purseService.operatorToChannel(req);
    }
}
