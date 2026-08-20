package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.application.service.AccountService;
import com.newzkl.platform.base.biz.account.domain.service.UserClientDomain;
import com.newzkl.platform.base.biz.account.model.req.AccountQuery;
import com.newzkl.platform.base.biz.account.model.req.AdminDisableAccountReq;
import com.newzkl.platform.base.biz.account.model.req.AdminRegisterIdentityReq;
import com.newzkl.platform.base.biz.account.model.vo.MemberAccountVO;
import com.newzkl.platform.base.common.core.model.exception.EasyExcelErrorVO;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.ddd.action.auth.RoleLimit;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class MemberController {

    private final AccountService accountService;
    private final UserClientDomain userClientDomain;

    /**
     * 分页查询
     *
     * @param query 账号查询
     * @return 会员账号分页
     */
    @PostMapping("/page")
    public PlatformResult<Page<MemberAccountVO>> page(@RequestBody AccountQuery query) {
        return PlatformResult.success(accountService.pageAccount(query));
    }

    /**
     * 添加会员
     *
     * <p>迁移补充: 旧 {@code adminCreateAccount(AdminRegisterAccountReq)} 中台化后为
     * {@code identityCreate(AdminRegisterIdentityReq)}, 入参字段为旧入参的超集 (多出角色与岗位),
     * 旧字段全部保留。旧实现打印整个入参 (含手机号), 迁移后只打印角色, 避免日志落敏感信息。</p>
     *
     * @param query 身份创建请求
     * @return 空结果
     */
    @PostMapping("/add")
    public PlatformResult<Object> add(@RequestBody @Valid AdminRegisterIdentityReq query) {
        log.info("添加会员, role:{}", query.getIdentity());
        accountService.identityCreate(query);
        return PlatformResult.success();
    }

    /**
     * 禁用或者启用
     *
     * @param req 禁用/启用请求
     * @return 空结果
     */
    @PostMapping("/disable")
    public PlatformResult<Object> disableMember(@RequestBody AdminDisableAccountReq req) {
        accountService.disableAccount(req);
        return PlatformResult.success();
    }

    /**
     * 批量导入会员 (Excel)
     *
     * <p>迁移补充: 旧 {@code IAccountService.importAccount(file)} 回包装结果并按 {@code isSuccess} 分流,
     * 中台化后由 {@link UserClientDomain#adminImportAccount} 承担, 直接回导入错误明细,
     * 失败信息由出参承载而非结果包装的 message。</p>
     *
     * @param file 上传的 Excel 文件
     * @return 导入错误明细
     */
    @PutMapping("/importMember")
    public PlatformResult<EasyExcelErrorVO> importMember(@RequestParam("file") MultipartFile file) {
        return PlatformResult.success(userClientDomain.adminImportAccount(file));
    }
}
