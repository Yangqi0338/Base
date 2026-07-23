package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.model.auth.req.AccountSaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.CodeUpdatePasswordReq;
import com.newzkl.platform.base.biz.account.model.req.AccountAwardUserQuery;
import com.newzkl.platform.base.biz.account.model.req.AccountParentQuery;
import com.newzkl.platform.base.biz.account.model.req.AccountQuery;
import com.newzkl.platform.base.biz.account.model.req.AccountReq;
import com.newzkl.platform.base.biz.account.model.req.CodeUpdateUsernameReq;
import com.newzkl.platform.base.biz.account.model.req.DestroyRoleReq;
import com.newzkl.platform.base.biz.account.model.req.SimpleAccountQuery;
import com.newzkl.platform.base.biz.account.model.req.SubEditReq;
import com.newzkl.platform.base.biz.account.model.req.SubProxySaveReq;
import com.newzkl.platform.base.biz.account.model.res.AccountInfo;
import com.newzkl.platform.base.biz.account.model.res.RoleVO;
import com.newzkl.platform.base.biz.account.model.res.SimpleAccountRes;
import com.newzkl.platform.base.biz.account.model.res.SubAccountVO;
import com.newzkl.platform.base.biz.account.model.vo.AccountAwardUserVO;
import com.newzkl.platform.base.biz.account.model.vo.AccountStructureTreeVO;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.vo.NameAuthVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.req.IdListCommand;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
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

    /**
     * 当前账号信息。
     *
     * @return 账号 VO
     */
    @PostMapping("account")
    public ScmResult<AccountVO> account() {
        return ScmResult.success(accountDomain.account(SecurityUtils.getClient(), SecurityUtils.getAccountId()));
    }

    /**
     * 根据条件查询账号信息。
     *
     * @param query 账号查询
     * @return 账号信息
     */
    @PostMapping("accountInfo")
    public ScmResult<AccountInfo> accountInfo(@RequestBody AccountQuery query) {
        return ScmResult.success(accountDomain.accountInfo(query));
    }

    /**
     * 修改密码。
     *
     * @param req 修改密码请求
     * @return 成功结果
     */
    @PostMapping("editPassword")
    public ScmResult<Void> editPassword(@Validated @RequestBody CodeUpdatePasswordReq req) {
        accountDomain.editPassword(req);
        return ScmResult.success();
    }

    /**
     * 修改基本信息。
     *
     * @param req 账号基本信息请求
     * @return 修改结果
     */
    @PostMapping("accountEdit")
    public ScmResult<Boolean> accountEdit(@Validated @RequestBody AccountReq req) {
        return ScmResult.success(accountDomain.accountEdit(req));
    }

    /**
     * 修改账号名称。
     *
     * @param req 修改用户名请求
     * @return 成功结果
     */
    @PostMapping("editUsername")
    public ScmResult<Void> editUsername(@Validated @RequestBody CodeUpdateUsernameReq req) {
        accountDomain.editUsername(req);
        return ScmResult.success();
    }

    /**
     * 编辑子账号基本信息。
     *
     * @param req 子账号编辑请求
     * @return 成功结果
     */
    @PostMapping("subEditBase")
    public ScmResult<Void> subEditBase(@Validated @RequestBody SubEditReq req) {
        accountDomain.subEditBase(SecurityUtils.getAccountId(), req.getId(), req);
        return ScmResult.success();
    }

    /**
     * 实名认证审核信息提交。
     *
     * @param nameAuthVO 实名认证信息
     * @return 成功结果
     */
    @PostMapping("nameAuthSubmit")
    public ScmResult<Void> nameAuthSubmit(@Validated @RequestBody NameAuthVO nameAuthVO) {
        accountDomain.nameAuthSubmit(SecurityUtils.getAccountId(), nameAuthVO);
        return ScmResult.success();
    }

    /**
     * 账号删除。
     *
     * @param idListObj ID 列表
     * @return 成功结果
     */
    @PostMapping("accountDelete")
    public ScmResult<Void> accountDelete(@Validated @RequestBody IdListCommand idListObj) {
        accountDomain.accountDelete(idListObj.getIdList());
        return ScmResult.success();
    }

    /**
     * 注销角色。
     *
     * @param destroyRoleReq 注销角色请求
     * @return 成功结果
     */
    @PostMapping("destroy")
    public ScmResult<Void> destroy(@Validated @RequestBody DestroyRoleReq destroyRoleReq) {
        accountDomain.destroy(SecurityUtils.getAccountId(), destroyRoleReq);
        return ScmResult.success();
    }

    /**
     * 查询范围内子账号结构。
     *
     * @return 子账号结构列表
     */
    @PostMapping("subAccountStructure")
    public ScmResult<List<AccountStructureTreeVO>> subAccountStructure() {
        return ScmResult.success(accountDomain.findScopeSubAccountStructure(SecurityUtils.getClient(), SecurityUtils.getAccountId()));
    }

    /**
     * 个人注册。
     *
     * @param customSaveReq 注册请求
     * @return 账号 VO
     */
    @PostMapping("customSave")
    public ScmResult<AccountVO> customSave(@Validated @RequestBody AccountSaveReq customSaveReq) {
        return ScmResult.success(accountDomain.customSave(customSaveReq));
    }

    /**
     * 代理注册子账号。
     *
     * @param proxySaveReq 代理注册请求
     * @return 账号 VO
     */
    @PostMapping("proxySave")
    public ScmResult<AccountVO> proxySave(@Validated @RequestBody SubProxySaveReq proxySaveReq) {
        return ScmResult.success(accountDomain.proxySave(SecurityUtils.getAccountId(), proxySaveReq));
    }

    /**
     * 查询账号列表。
     *
     * @param query 账号查询
     * @return 账号列表
     */
    @PostMapping("accountList")
    public ScmResult<List<AccountVO>> accountList(@RequestBody AccountQuery query) {
        return ScmResult.success(accountDomain.accountList(query));
    }

    /**
     * 查询账号角色列表。
     *
     * @return 角色列表
     */
    @PostMapping("accountRoleList")
    public ScmResult<List<RoleVO>> accountRoleList() {
        return ScmResult.success(accountDomain.accountRoleList(SecurityUtils.getAccountId(), SecurityUtils.getClient()));
    }

    /**
     * 下级账号列表。
     *
     * @param query 父账号查询
     * @return 下级账号列表
     */
    @PostMapping("subAccountList")
    public ScmResult<List<SubAccountVO>> subAccountList(@RequestBody AccountParentQuery query) {
        return ScmResult.success(accountDomain.subAccountList(query));
    }

    /**
     * 简易账号分页。
     *
     * @param accountQuery 简易账号查询
     * @return 简易账号分页
     */
    @PostMapping("simpleAccountPage")
    public ScmResult<Page<SimpleAccountRes>> simpleAccountPage(@RequestBody SimpleAccountQuery accountQuery) {
        return ScmResult.success(accountDomain.simpleAccountPage(accountQuery));
    }

    /**
     * 账号分页。
     *
     * @param accountQuery 账号查询
     * @return 账号分页
     */
    @PostMapping("accountPage")
    public ScmResult<Page<AccountVO>> accountPage(@RequestBody AccountQuery accountQuery) {
        return ScmResult.success(accountDomain.accountPage(accountQuery));
    }

    /**
     * 奖金池获取用户分页。
     *
     * @param query 奖金池用户查询
     * @return 用户分页
     */
    @PostMapping("awardUserPage")
    public ScmResult<Page<AccountAwardUserVO>> awardUserPage(@RequestBody AccountAwardUserQuery query) {
        return ScmResult.success(accountDomain.awardUserPage(query));
    }
}
