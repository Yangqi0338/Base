package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.model.auth.req.CodeUpdatePasswordReq;
import com.newzkl.platform.base.biz.account.model.req.AccountParentQuery;
import com.newzkl.platform.base.biz.account.model.req.CodeUpdateUsernameReq;
import com.newzkl.platform.base.biz.account.model.req.SimpleAccountQuery;
import com.newzkl.platform.base.biz.account.model.res.AccountOutRes;
import com.newzkl.platform.base.biz.account.model.res.SimpleAccountRes;
import com.newzkl.platform.base.biz.account.model.res.SubAccountVO;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 账号控制器。
 *
 * @author fang
 */
@RestController
@RequestMapping("/user/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountDomain accountDomain;

    private final UserQueryService userQueryService;

    /**
     * 当前账号详情。
     *
     * @return 账号外部视图
     */
    @PostMapping("accountDetail")
    public PlatformResult<AccountOutRes> accountDetail() {
        return PlatformResult.success(
                userQueryService.accountOutVO(SecurityUtils.getClient(), SecurityUtils.getAccountId()));
    }

    /**
     * 当前账号信息。
     *
     * @return 账号 VO
     */
    @PostMapping("accountBase")
    public PlatformResult<AccountVO> account() {
        return PlatformResult.success(accountDomain.account(SecurityUtils.getClient(), SecurityUtils.getAccountId()));
    }

    /**
     * 修改密码。
     *
     * @param req 修改密码请求
     * @return 成功结果
     */
    @PostMapping("codeUpdatePassword")
    public PlatformResult<Void> editPassword(@Validated @RequestBody CodeUpdatePasswordReq req) {
        accountDomain.editPassword(req);
        return PlatformResult.success();
    }

    /**
     * 修改账号名称。
     *
     * @param req 修改用户名请求
     * @return 成功结果
     */
    @PostMapping("updateUsername")
    public PlatformResult<Void> editUsername(@Validated @RequestBody CodeUpdateUsernameReq req) {
        accountDomain.editUsername(req);
        return PlatformResult.success();
    }

    /**
     * 下级账号列表。
     *
     * @param query 父账号查询
     * @return 下级账号列表
     */
    @PostMapping("subAccountList")
    public PlatformResult<List<SubAccountVO>> subAccountList(@RequestBody AccountParentQuery query) {
        return PlatformResult.success(accountDomain.subAccountList(query));
    }

    /**
     * 简易账号分页。
     *
     * @param accountQuery 简易账号查询
     * @return 简易账号分页
     */
    @PostMapping("simpleAccountPage")
    public PlatformResult<Page<SimpleAccountRes>> simpleAccountPage(@RequestBody SimpleAccountQuery accountQuery) {
        return PlatformResult.success(accountDomain.simpleAccountPage(accountQuery));
    }
}
