package com.newzkl.platform.base.biz.finance.action.controller;

import com.newzkl.platform.base.biz.finance.application.purse.service.TripartitePurseService;
import com.newzkl.platform.base.biz.finance.model.purse.req.EntUserApplyAccountReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.UserApplyAccountReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.huifu.HuiFuEntUserApplyAccountReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.huifu.HuiFuUserApplyAccountReq;
import com.newzkl.platform.base.biz.finance.model.purse.res.EntUserApplyAccountRes;
import com.newzkl.platform.base.biz.finance.model.purse.res.UserApplyAccountRes;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
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
@FuncPermission("汇付三方账户")
public class HuiFuApiController {

    private final TripartitePurseService tripartitePurseService;

    /**
     * 汇付企业开户
     *
     * @param request 企业开户申请
     * @return 开户结果
     */
    @FuncPermission("汇付企业开户")
    @PostMapping("/entOpenAccount")
    public PlatformResult<EntUserApplyAccountRes> entOpenAccount(@RequestBody @Valid HuiFuEntUserApplyAccountReq request) {
        return PlatformResult.success(tripartitePurseService.addEntAccountTripartitePurse(request));
    }

    /**
     * 用户汇付开户
     *
     * @param request 个人开户申请
     * @return 开户结果
     */
    @FuncPermission("用户汇付开户")
    @PostMapping("/userOpenAccount")
    public PlatformResult<UserApplyAccountRes> userOpenAccount(@RequestBody @Valid HuiFuUserApplyAccountReq request) {
        return PlatformResult.success(tripartitePurseService.addAccountTripartitePurse(request));
    }
}
