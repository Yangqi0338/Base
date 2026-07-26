package com.newzkl.platform.base.biz.sys.domain.service.impl;

import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.biz.sys.domain.adapt.repository.AdminAccountRepository;
import com.newzkl.platform.base.biz.sys.domain.service.AdminAccountDomain;
import com.newzkl.platform.base.biz.sys.model.adminaccount.query.AdminAccountQuery;
import com.newzkl.platform.base.biz.sys.model.adminaccount.req.AdminAccountReq;
import com.newzkl.platform.base.biz.sys.model.adminaccount.req.PasswordUpdateReq;
import com.newzkl.platform.base.biz.sys.model.adminaccount.res.AdminAccountRes;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 平台账号领域服务实现。
 *
 * <p>BCrypt 加密收敛在本层 (旧 controller {@code new BCryptPasswordEncoder().encode} 剥离);
 * 校验复用 {@link SecurityUtils#matchesPassword}。登录 token 签发归入口 starter。</p>
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class AdminAccountDomainImpl implements AdminAccountDomain {

    private final AdminAccountRepository adminAccountRepository;

    @Override
    public Long adminAccountCreate(AdminAccountReq req) {
        encodePasswordIfPresent(req);
        return adminAccountRepository.adminAccountSave(req);
    }

    @Override
    public void adminAccountUpdate(AdminAccountReq req) {
        encodePasswordIfPresent(req);
        adminAccountRepository.adminAccountSave(req);
    }

    @Override
    public void adminAccountDelete(List<Long> idList) {
        adminAccountRepository.adminAccountDelete(idList);
    }

    @Override
    public AdminAccountRes adminAccountVO(Long id) {
        return adminAccountRepository.adminAccountVO(id);
    }

    @Override
    public List<AdminAccountRes> adminAccountList(AdminAccountQuery query) {
        return adminAccountRepository.adminAccountList(query);
    }

    @Override
    public AdminAccountRes passwordVerify(String username, String rawPassword) {
        if (StrUtil.hasBlank(username, rawPassword)) {
            throw new ScmException(BaseErrorCode.PARAM, "用户/密码不能为空");
        }
        AdminAccountRes account = adminAccountRepository.adminAccountByUsername(username);
        if (account == null) {
            throw new ScmException(BaseErrorCode.USER_NOT_FOUND, username);
        }
        if (!SecurityUtils.matchesPassword(rawPassword, account.getPassword())) {
            throw new ScmException(BaseErrorCode.PASSWORD_ERROR, username);
        }
        return account;
    }

    @Override
    public void passwordUpdate(PasswordUpdateReq req) {
        AdminAccountRes account = adminAccountRepository.adminAccountVO(req.getAccountId());
        if (account == null) {
            throw new ScmException(BaseErrorCode.USER_NOT_FOUND, String.valueOf(req.getAccountId()));
        }
        if (!SecurityUtils.matchesPassword(req.getPassword(), account.getPassword())) {
            throw new ScmException(BaseErrorCode.PASSWORD_ERROR, account.getUsername());
        }
        AdminAccountReq update = new AdminAccountReq();
        update.setId(account.getId());
        update.setPassword(new BCryptPasswordEncoder().encode(req.getNewPassword()));
        adminAccountRepository.adminAccountSave(update);
    }

    /**
     * 明文密码存在则 BCrypt 加密 (置回请求对象)。
     *
     * @param req 账号请求
     */
    private void encodePasswordIfPresent(AdminAccountReq req) {
        if (StrUtil.isNotBlank(req.getPassword())) {
            req.setPassword(new BCryptPasswordEncoder().encode(req.getPassword()));
        }
    }
}
