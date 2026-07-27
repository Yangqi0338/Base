package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.application.service.AccountService;
import com.newzkl.platform.base.biz.account.domain.service.UserClientDomain;
import com.newzkl.platform.base.biz.account.model.req.AccountQuery;
import com.newzkl.platform.base.biz.account.model.req.AdminDisableAccountReq;
import com.newzkl.platform.base.biz.account.model.req.AdminRegisterIdentityReq;
import com.newzkl.platform.base.biz.account.model.vo.MemberAccountVO;
import com.newzkl.platform.base.common.core.model.exception.EasyExcelErrorVO;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 中台-用户管理控制器。
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.AccountAdminController},
 * 路径 {@code /user/admin} 与 4 个端点的 HTTP 方法保持不变。</p>
 *
 * <p>迁移要点:</p>
 * <ul>
 *   <li>旧 {@code IAccountService.adminCreateAccount(AdminRegisterAccountReq)} 内部把角色硬编码为
 *       C 端会员; 中台把"平台侧建会员"收敛在 {@link UserClientDomain#adminCreateMember} (同样强制
 *       会员角色), 故本端点直接复用该领域能力, 不再另建一条注册链路。
 *       入参改用中台统一的 {@link AdminRegisterIdentityReq}, 字段 {@code phone} / {@code nickname} /
 *       {@code headImg} / {@code superiorAccount} / {@code state} 与旧入参一一对应。</li>
 *   <li>旧 {@code importAccount} 用 easypoi 自行解析并拼接文字版结果串; 中台已有
 *       {@link UserClientDomain#adminImportAccount} (easyexcel + 逐条复用建会员逻辑), 直接复用,
 *       出参改为结构化的 {@link EasyExcelErrorVO}。</li>
 * </ul>
 *
 * @author KC
 */
@Slf4j
@RestController
@RequestMapping("/user/admin")
@RequiredArgsConstructor
public class AccountAdminController {

    private final AccountService accountService;
    private final UserClientDomain userClientDomain;

    /**
     * 会员账号分页。
     *
     * @param query 账号查询
     * @return 会员账号分页
     */
    @PostMapping("/page")
    public PlatformResult<Page<MemberAccountVO>> page(@RequestBody AccountQuery query) {
        return PlatformResult.success(accountService.pageAccount(query));
    }

    /**
     * 添加会员。
     *
     * <p>保留旧语义: 角色恒为 C 端会员, 请求体内即便带角色也被领域层覆盖。
     * 旧入参仅校验手机号非空, 故此处不加 {@code @Validated} (中台入参的 {@code role} 非空校验
     * 与 {@code certificateCheck} 断言面向平台侧通用建号场景, 套在本端点上会破坏旧前端契约)。</p>
     *
     * @param req 会员注册入参
     * @return 成功结果 (与旧接口一致, 不回传主键)
     */
    @PostMapping("/add")
    public PlatformResult<Void> add(@RequestBody AdminRegisterIdentityReq req) {
        log.info("添加会员：{}", req);
        userClientDomain.adminCreateMember(req);
        return PlatformResult.success();
    }

    /**
     * 启用或禁用会员。
     *
     * @param req 启用禁用入参
     * @return 成功结果
     */
    @PostMapping("/disable")
    public PlatformResult<Void> disableMember(@Validated @RequestBody AdminDisableAccountReq req) {
        accountService.disableAccount(req);
        return PlatformResult.success();
    }

    /**
     * 批量导入会员 (Excel)。
     *
     * @param file 上传的 Excel 文件
     * @return 导入结果 (总数 / 成功数 / 失败数 / 失败行明细)
     */
    @PutMapping("/importMember")
    public PlatformResult<EasyExcelErrorVO> importMember(@RequestParam("file") MultipartFile file) {
        return PlatformResult.success(userClientDomain.adminImportAccount(file));
    }
}
