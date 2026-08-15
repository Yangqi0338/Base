package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.domain.service.UserClientDomain;
import com.newzkl.platform.base.biz.account.model.req.*;
import com.newzkl.platform.base.biz.account.model.res.AccountOutRes;
import com.newzkl.platform.base.biz.account.model.res.AppAccountVO;
import com.newzkl.platform.base.biz.account.model.res.SimpleAccountRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.vo.MemberVO;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户-账号
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.AccountController}。
 * 类级路径与方法级路径逐字沿用旧契约: 类级为 {@code /user}, 方法级自带二级段 {@code /account/**}
 * 与 {@code /emp/**}, 不做合并改写。</p>
 *
 * @author KC
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AccountController {


    private final UserQueryService userQueryService;
    private final AccountDomain accountDomain;
    private final UserClientDomain userClientDomain;

    /**
     * 消费者修改账号信息
     *
     * @param updateMemberInfoCommand 会员信息修改命令
     * @return 空结果
     */
    @PostMapping("/account/updateMemberInfo")
    public PlatformResult<Void> updateMemberInfo(@Validated @RequestBody UpdateMemberInfoCommand updateMemberInfoCommand) {
        userClientDomain.updateMemberInfo(SecurityUtils.getAccountId(), updateMemberInfoCommand);
        return PlatformResult.success();
    }

    /**
     * 账号详情
     *
     * @return 账号外部视图
     */
    @PostMapping("/account/accountDetail")
    public PlatformResult<AccountOutRes> account() {
        return PlatformResult.success(userQueryService.accountOutVO(SecurityUtils.getClient(), SecurityUtils.getAccountId()));
    }

    /**
     * 账号信息
     *
     * @return 账号视图
     */
    @PostMapping("/account/accountBase")
    public PlatformResult<AccountVO> accountVO() {
        return PlatformResult.success(accountDomain.account(SecurityUtils.getClient(), SecurityUtils.getAccountId()));
    }

    /**
     * app账号信息
     *
     * @param accountId 账号ID, 不传取当前登录账号
     * @return app账号视图
     */
    @PostMapping("/account/app")
    public PlatformResult<AppAccountVO> accountAppVO(@RequestParam(required = false) Long accountId) {
        AccountKeyQuery query = AccountKeyQuery.build();
        if (accountId != null) {
            query.setAccountId(accountId);
        }
        return PlatformResult.success(userQueryService.appVO(query));
    }

    /**
     * 简易账号列表
     *
     * @param accountQuery 简易账号查询
     * @return 简易账号分页
     */
    @PostMapping("/account/simpleAccountPage")
    public PlatformResult<Page<SimpleAccountRes>> simpleAccountPage(@RequestBody SimpleAccountQuery accountQuery) {
        return PlatformResult.success(accountDomain.simpleAccountPage(accountQuery));
    }

    /**
     * 账号列表
     *
     * <p>保留旧前置改写语义: {@code accountType} 为主账号时把 {@code mainAccountId} 钉为
     * {@link AccountEnum#MAIN_ACCOUNT_PID}; 平台角色额外把 {@code stateOver} 钉为
     * {@link AccountEnum.State#DESTROY}, 即平台可见含已销毁账号。</p>
     *
     * <p>迁移补充: 旧 {@code @EnableIdParse}(ID 明文/密文互转切面)未迁入 Base, 出参 ID 不再脱敏。</p>
     *
     * @param accountQuery 账号查询
     * @return 账号分页
     * @deprecated 前端零引用, 已确认死端点 (2026-07-27 交叉比对); 仅为契约完整性迁入
     */
    @Deprecated
    @PostMapping("/account/accountPage")
    public PlatformResult<Page<AccountVO>> accountPage(@RequestBody AccountQuery accountQuery) {
        if (AccountEnum.SubUserType.MAIN == accountQuery.getAccountType()) {
            accountQuery.setMainAccountId(AccountEnum.MAIN_ACCOUNT_PID);
        }
        if (RoleEnum.CompanyRole.PLATFORM == SecurityUtils.getRole()) {
            accountQuery.setStateOver(AccountEnum.State.DESTROY);
        }
        return PlatformResult.success(accountDomain.accountPage(accountQuery));
    }

    /**
     * c端客户分页
     *
     * @param memberQuery c端客户查询
     * @return c端客户分页
     */
    @PostMapping("memberPage")
    public PlatformResult<Page<MemberVO>> memberPage(@RequestBody MemberQuery memberQuery) {
        return PlatformResult.success(userQueryService.memberPage(memberQuery));
    }

}
