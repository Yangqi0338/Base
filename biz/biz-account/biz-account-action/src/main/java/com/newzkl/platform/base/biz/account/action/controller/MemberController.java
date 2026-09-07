package com.newzkl.platform.base.biz.account.action.controller;

import com.newzkl.platform.base.biz.account.application.service.AccountService;
import com.newzkl.platform.base.biz.account.domain.service.UserClientDomain;
import com.newzkl.platform.base.biz.account.model.dto.MemberDTO;
import com.newzkl.platform.base.biz.account.model.req.AdminRegisterIdentityReq;
import com.newzkl.platform.base.biz.account.model.req.MemberRegisterReq;
import com.newzkl.platform.base.biz.account.model.req.MemberReq;
import com.newzkl.platform.base.biz.account.model.res.MemberRes;
import com.newzkl.platform.base.common.core.utils.common.CommonUtil;
import com.newzkl.platform.base.common.core.model.check.UpdateCommand;
import com.newzkl.platform.base.common.core.model.exception.EasyExcelErrorVO;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import com.newzkl.platform.base.common.ddd.action.auth.RoleLimit;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 中台-用户管理
 * @author KC
 */
@Slf4j
@RestController
@RequestMapping("/account/member")
@RequiredArgsConstructor
@RoleLimit(client = AccountEnum.Client.ADMIN)
@FuncPermission("中台-用户管理")
public class MemberController {

    private final AccountService accountService;
    private final UserClientDomain userClientDomain;

    /**
     * 添加会员
     *
     * <p>收会员专用入参 {@link MemberRegisterReq}, 端内转 {@link AdminRegisterIdentityReq}:
     * identity 固定 MEMBER。会员无角色/状态/登录账号字段, 昵称空时注册链补手机号。</p>
     *
     * @param req 会员注册请求
     * @return 空结果
     */
    @PostMapping("/add")
    @FuncPermission("添加会员")
    public PlatformResult<Object> add(@RequestBody MemberRegisterReq req) {
        CommonUtil.validate(req);
        log.info("添加会员");
        AdminRegisterIdentityReq registerReq = TransferUtils.transfer(req, AdminRegisterIdentityReq.class);
        registerReq.setIdentity(AccountEnum.Identity.MEMBER);
        accountService.identityCreate(registerReq);
        return PlatformResult.success();
    }

    /**
     * 编辑会员
     *
     * @param req 会员修改请求
     * @return 空结果
     * @ext 主数据 member, 副数据 account(单副, 副数据不再向下关联)。{@code nickname} / {@code head}
     *      落 account 表, 其余字段落 member 表
     */
    @PostMapping("/edit")
    @FuncPermission("编辑会员")
    public PlatformResult<Void> edit(@Validated(UpdateCommand.class) @RequestBody MemberReq req) {
        userClientDomain.memberEdit(req.getId(), req);
        return PlatformResult.success();
    }

    /**
     * 会员纯净详情
     *
     * <p>只需会员自有列时走本端点, 相比 {@code detail} 少一次 account 查询</p>
     *
     * @param id 会员账号ID
     * @return 会员纯净视图
     * @ext 主数据 member (无副数据)
     */
    @GetMapping("/base")
    public PlatformResult<MemberDTO> base(@RequestParam("id") Long id) {
        return PlatformResult.success(userClientDomain.memberBase(id));
    }

    /**
     * 会员详情
     *
     * @param id 会员账号ID
     * @return 会员聚合视图
     * @ext 主数据 member, 副数据 account(单副, 副数据不再向下关联)。方向与
     *      {@code AccountController.identityDetail}(主 account / 副身份) 相反, 两者不可互相替代
     */
    @GetMapping("/detail")
    public PlatformResult<MemberRes> detail(@RequestParam("id") Long id) {
        return PlatformResult.success(userClientDomain.memberDetail(id));
    }
}
