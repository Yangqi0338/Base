package com.newzkl.platform.base.biz.account.action.controller;

import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.service.UserClientDomain;
import com.newzkl.platform.base.biz.account.model.req.CancelMemberReq;
import com.newzkl.platform.base.biz.account.model.req.MemberInfoReq;
import com.newzkl.platform.base.biz.account.model.res.AccountOutRes;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消费者-信息管理
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.ConsumerInfoController}。
 * 类级路径与方法级路径逐字沿用旧契约。</p>
 *
 * <p>迁移补充: 旧 {@code /getByPhone} 已补齐, 落在 {@code UserQueryService.accountOutVO(String)}。
 * 旧 {@code /switchRoleLogin} / {@code /historyAccount} 分别依赖
 * {@code IAccountLoginDomain.switchCompanyRoleLogin}、{@code IAccountDomain.historyAccount},
 * 中台无对应方法, 未迁移, 见迁移报告「能力缺失」。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/user/consume/info")
@RequiredArgsConstructor
public class ConsumerInfoController {

    private final UserClientDomain userClientDomain;
    private final UserQueryService userQueryService;

    /**
     * 查用户信息
     *
     * <p>迁移补充: 旧 {@code IUserQueryService.getByPhone(MemberInfoReq)} 中台化后对应
     * {@link UserQueryService#accountOutVO}, 由手机号定位账号再复用账号详情装配。
     * 迁移差异: 旧实现账号不存在时抛 {@code PARAM_ERROR("账号不存在")},
     * 中台侧由 {@code accountDomain.accountInfo} 抛账号不存在异常, 语义等价</p>
     *
     * @param req 用户信息查询入参
     * @return 账号外部视图
     */
    @PostMapping("/getByPhone")
    public PlatformResult<AccountOutRes> getByPhone(@Validated @RequestBody MemberInfoReq req) {
        if (StrUtil.isEmpty(req.getPhone())) {
            ThrowsException.exception(BaseErrorCode.PARAM, "手机号不能为空");
        }
        return PlatformResult.success(userQueryService.accountOutVO(req.getPhone()));
    }

    /**
     * 注销账号
     *
     * <p>验证码校验与注销落库仍在领域层完成, 未简化。</p>
     *
     * @param command 注销命令
     * @return 空结果
     */
    @PostMapping("/cancelMember")
    public PlatformResult<Void> cancelMember(@Validated @RequestBody CancelMemberReq command) {
        userClientDomain.cancelMember(SecurityUtils.getAccountId(), command);
        return PlatformResult.success();
    }
}
