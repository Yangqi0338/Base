package com.newzkl.platform.base.biz.account.action.controller;

import com.newzkl.platform.base.biz.account.domain.service.UserClientDomain;
import com.newzkl.platform.base.biz.account.model.req.CancelMemberReq;
import com.newzkl.platform.base.biz.account.model.req.UpdateMemberInfoCommand;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会员控制器。
 *
 * @author fang
 */
@RestController
@RequestMapping("/user/member")
@RequiredArgsConstructor
public class MemberController {

    private final UserClientDomain userClientDomain;

    /**
     * 注销会员。
     *
     * @param command 注销会员请求
     * @return 成功结果
     */
    @PostMapping("cancelMember")
    public PlatformResult<Void> cancelMember(@Validated @RequestBody CancelMemberReq command) {
        userClientDomain.cancelMember(SecurityUtils.getAccountId(), command);
        return PlatformResult.success();
    }

    /**
     * 修改会员信息。
     *
     * @param command 会员信息修改命令
     * @return 成功结果
     */
    @PostMapping("updateMemberInfo")
    public PlatformResult<Void> updateMemberInfo(@Validated @RequestBody UpdateMemberInfoCommand command) {
        userClientDomain.updateMemberInfo(SecurityUtils.getAccountId(), command);
        return PlatformResult.success();
    }
}
