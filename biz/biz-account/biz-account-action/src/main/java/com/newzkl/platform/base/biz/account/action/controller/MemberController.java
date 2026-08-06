package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.model.req.MemberQuery;
import com.newzkl.platform.base.biz.account.model.vo.MemberVO;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户-c端客户
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.MemberController}。
 * 类级路径与方法级路径逐字沿用旧契约。</p>
 *
 * <p>迁移补充: 旧 {@code /wxLogin} 与 {@code /wxLoginV2} 依赖 {@code IAccountDomain.wxLogin},
 * 中台无微信登录能力, 两端点未迁移, 见迁移报告「能力缺失」。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/user/member")
@RequiredArgsConstructor
public class MemberController {

    private final UserQueryService userQueryService;

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
