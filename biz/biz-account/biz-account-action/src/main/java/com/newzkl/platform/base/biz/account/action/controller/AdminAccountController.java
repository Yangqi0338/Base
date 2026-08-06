package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.policy.AbsAccountPolicySupport;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.model.auth.req.AccountCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.AccountQuery;
import com.newzkl.platform.base.biz.account.model.req.AccountReq;
import com.newzkl.platform.base.biz.account.model.req.AccountRegisterRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.req.IdListCommand;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 平台-账号控制器
 *
 * <p>迁移自旧 biz-sys {@code AdminAccountController}。旧 {@code admin_account} 表已合并至
 * {@code account} 表 ({@code client=ADMIN}, {@code roleIdList} 含 PLATFORM(1)), 故本控制器
 * 统一走 {@link AccountDomain}, 平台账号即 {@code client=ADMIN} 的账号行。</p>
 *
 * <p>契约变更:
 * <ul>
 *   <li>旧 {@code face} 字段语义等同 {@code account.head} (头像), 出入参保留业务名 {@code face}, 内部映射 {@code head}。</li>
 *   <li>旧 {@code state} 为 {@code Integer}(0=正常,1=冻结), 新契约改用 {@code AccountEnum.State}(ENABLE/DISABLE)。</li>
 *   <li>旧 {@code aroleIdList} 已废弃: 平台账号恒为 PLATFORM 角色, 创建时由策略固定写入, 不再由入参指定。</li>
 * </ul></p>
 *
 * <p>[AUTH] {@code detail}/{@code passwordUpdate} 的当前登录 id 由入口 {@code SecurityContextFilter} 注入,
 * 本域经 {@code SecurityUtils} 读取。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/admin/account")
@RequiredArgsConstructor
public class AdminAccountController {

    private final AccountDomain accountDomain;

    /**
     * 账号创建
     *
     * <p>平台账号恒为 PLATFORM 角色、ADMIN 端、启用状态, 由 {@code AdminAccountPolicy.customRegister} 固定装配。</p>
     *
     * @param req 账号请求 (明文密码, domain 内 BCrypt 加密落库)
     * @return 账号 id
     */
    @PostMapping("createAccount")
    public PlatformResult<Long> createAccount(@Validated @RequestBody AdminAccountCreateReq req) {
        AccountCustomSaveReq saveReq = new AccountCustomSaveReq();
        saveReq.setUsername(req.getUsername());
        saveReq.setPassword(req.getPassword());
        saveReq.setNickname(req.getNickname());
        saveReq.setHeadImg(req.getFace());
        saveReq.setPhone(req.getPhone());

        AccountRegisterRes res = AbsAccountPolicySupport.getPolicy(CommonEnum.Client.ADMIN).customRegister(saveReq);
        return PlatformResult.success(res.getId());
    }

    /**
     * 账号删除
     *
     * @param idList 账号 id 列表
     * @return 空结果
     */
    @PostMapping("delete")
    public PlatformResult<Void> delete(@Validated @RequestBody IdListCommand idList) {
        accountDomain.accountDelete(idList.getIdList());
        return PlatformResult.success();
    }

    /**
     * 账号修改
     *
     * @param req 账号请求 (id 必填)
     * @return 空结果
     */
    @PostMapping("update")
    public PlatformResult<Void> update(@Validated @RequestBody AdminAccountUpdateReq req) {
        if (req.getId() == null) {
            ThrowsException.exception(BaseErrorCode.PARAM, "缺少ID");
        }
        AccountReq accountReq = new AccountReq();
        accountReq.setId(req.getId());
        accountReq.setNickname(req.getNickname());
        accountReq.setHead(req.getFace());
        accountReq.setPhone(req.getPhone());
        accountReq.setState(req.getState());
        accountDomain.accountEdit(accountReq);
        return PlatformResult.success();
    }

    /**
     * 账号详情
     *
     * <p>入参为空时取当前登录账号 (入口注入)。</p>
     *
     * @param idList 账号 id 列表 (可空; 取首个)
     * @return 账号详情
     */
    @PostMapping("detail")
    public PlatformResult<AdminAccountRes> detail(@RequestBody(required = false) IdListCommand idList) {
        Long accountId = (idList == null || idList.getId() == null)
                ? SecurityUtils.getAccountId()
                : idList.getId();
        AccountVO accountVO = accountDomain.account(CommonEnum.Client.ADMIN, accountId);
        return PlatformResult.success(toRes(accountVO));
    }

    /**
     * 账号列表
     *
     * @param query 账号查询
     * @return 账号分页
     */
    @PostMapping("page")
    public PlatformResult<Page<AdminAccountRes>> page(@RequestBody AccountQuery query) {
        query.setClient(CommonEnum.Client.ADMIN);
        Page<AccountVO> page = accountDomain.accountPage(query);
        return PlatformResult.success((Page<AdminAccountRes>) page.convert(this::toRes));
    }

    /**
     * AccountVO -> AdminAccountRes (head -> face)
     */
    private AdminAccountRes toRes(AccountVO accountVO) {
        AdminAccountRes res = new AdminAccountRes();
        res.setId(accountVO.getId());
        res.setUsername(accountVO.getUsername());
        res.setNickname(accountVO.getNickname());
        res.setFace(accountVO.getHead());
        res.setPhone(accountVO.getPhone());
        res.setState(accountVO.getState());
        res.setRoleIdList(accountVO.getRoleIdList());
        return res;
    }

    /**
     * 平台账号创建请求
     */
    @Data
    public static class AdminAccountCreateReq {
        /**
         * 用户名
         */
        private String username;
        /**
         * 密码
         */
        private String password;
        /**
         * 昵称
         */
        private String nickname;
        /**
         * 头像
         */
        private String face;
        /**
         * 手机号
         */
        private String phone;
    }

    /**
     * 平台账号修改请求
     */
    @Data
    public static class AdminAccountUpdateReq {
        /**
         * 账号ID
         */
        private Long id;
        /**
         * 昵称
         */
        private String nickname;
        /**
         * 头像
         */
        private String face;
        /**
         * 手机号
         */
        private String phone;
        /**
         * 账号状态
         */
        private AccountEnum.State state;
    }

    /**
     * 平台账号详情响应
     */
    @Data
    public static class AdminAccountRes {
        /**
         * 账号ID
         */
        private Long id;
        /**
         * 用户名
         */
        private String username;
        /**
         * 昵称
         */
        private String nickname;
        /**
         * 头像
         */
        private String face;
        /**
         * 手机号
         */
        private String phone;
        /**
         * 账号状态
         */
        private AccountEnum.State state;
        /**
         * 角色ID列表
         */
        private String roleIdList;
    }
}
