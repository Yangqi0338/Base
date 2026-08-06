package com.newzkl.platform.base.biz.finance.action.controller;

import com.newzkl.platform.base.biz.finance.application.purse.service.TripartitePurseService;
import com.newzkl.platform.base.biz.finance.domain.purse.service.TripartitePurseDomain;
import com.newzkl.platform.base.biz.finance.model.purse.req.EntUserApplyAccountReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.UserApplyAccountReq;
import com.newzkl.platform.base.biz.finance.model.purse.res.EntUserApplyAccountRes;
import com.newzkl.platform.base.biz.finance.model.purse.res.UserApplyAccountRes;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountTripartitePurseVO;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * 汇付三方账户控制器
 *
 * @author niu
 */
@RestController
@RequestMapping("/huiFu")
@RequiredArgsConstructor
public class HuiFuApiController {

    private final TripartitePurseService tripartitePurseService;
    private final TripartitePurseDomain tripartitePurseDomain;

    /**
     * 汇付企业开户
     *
     * @param request 企业开户申请
     * @return 开户结果
     */
    @PostMapping("/entOpenAccount")
    public PlatformResult<EntUserApplyAccountRes> entOpenAccount(@RequestBody @Valid EntUserApplyAccountReq request) {
        return PlatformResult.success(tripartitePurseService.addEntAccountTripartitePurse(request));
    }

    /**
     * 用户汇付开户
     *
     * @param request 个人开户申请
     * @return 开户结果
     */
    @PostMapping("/userOpenAccount")
    public PlatformResult<UserApplyAccountRes> userOpenAccount(@RequestBody @Valid UserApplyAccountReq request) {
        return PlatformResult.success(tripartitePurseService.addAccountTripartitePurse(request));
    }

    /**
     * 三方钱包信息
     *
     * @return 三方账户
     */
    @GetMapping("/detail")
    public PlatformResult<AccountTripartitePurseVO> detail() {
        return PlatformResult.success(tripartitePurseDomain.queryAccountTripartitePurse(SecurityUtils.getAccountId()));
    }
}
