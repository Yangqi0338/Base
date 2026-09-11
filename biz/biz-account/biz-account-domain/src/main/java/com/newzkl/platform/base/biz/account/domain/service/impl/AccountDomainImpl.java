package com.newzkl.platform.base.biz.account.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.adapt.api.PurseApi;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicySupport;
import com.newzkl.platform.base.biz.account.domain.repository.AccountRepository;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.model.assembler.AccountAssembler;
import com.newzkl.platform.base.biz.account.model.auth.req.AccountSaveReq;
import com.newzkl.platform.base.biz.account.model.req.*;
import com.newzkl.platform.base.biz.account.model.res.AccountInfo;
import com.newzkl.platform.base.biz.account.model.res.SimpleAccountRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountStructureTreeVO;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.common.core.model.enums.SmsEnum;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.redis.model.req.VerificationCodeReq;
import com.newzkl.platform.base.common.ddd.facade.IdentityRegisterRpcReq;
import com.newzkl.platform.base.common.ddd.model.constant.AccountErrorCode;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/2210:47
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountDomainImpl implements AccountDomain {

    private final AccountRepository accountRepository;
    private final AccountAssembler accountAssembler;
    private final PurseApi purseApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean accountEdit(AccountReq req) {
        AccountVO account = accountAssembler.req2VO(req);

        // 修改account基础信息
        boolean updated = accountRepository.accountEdit(account, null);

        // 角色特定修改
//        AbsIdentityPolicySupport.getPolicy(req.getIdentity()).saveByAccount(req);
        return updated;
    }

    @Override
    public void accountDelete(List<Long> accountIdList) {
        accountRepository.accountDelete(accountIdList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void destroy(Long accountId, DestroyRoleReq destroyRoleReq) {
        AccountEnum.Identity identity = destroyRoleReq.getIdentity();
        AccountEnum.Client client = identity.getClient();
        AccountVO accountVO = account(client, accountId);
        if (AccountEnum.State.DESTROY == accountVO.getState()) {
            throw new PlatformException(AccountErrorCode.IS_LOCK);
        }

        String username = accountVO.getUsername();
        // 验证验证码

        VerificationCodeReq verificationCodeReq = new VerificationCodeReq();
        verificationCodeReq.setCode(destroyRoleReq.getCode());
        verificationCodeReq.setPhone(username);
        verificationCodeReq.setType(SmsEnum.Type.DESTROY_USER);
        accountRepository.verificationCode(verificationCodeReq);

        // 执行账号注销: 仅改状态/摘角色位, 身份实体与角色绑定清理延后到定时回收
        // 宽限期内 account 仍 DESTROY 保留, 登录可激活恢复; 故此处不删身份实体
        accountRepository.destroy(accountVO, destroyRoleReq.getIdentity());
        //注销成功通知
//            CodeReq codeReq = new CodeReq();
//            codeReq.setPhone(username);
//            codeReq.setType(SmsEnum.Type.DESTROY_USER_EVENT);
        // TODO[infra-port sms]: SmsMethod.sendCode(codeReq);
//            SmsMethod.send()
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void destroyAccount(Long accountId) {
        AccountVO account = account(null, accountId);
        if (AccountEnum.State.DESTROY == account.getState()) {
            throw new PlatformException(AccountErrorCode.IS_LOCK);
        }
        // 整个注销账号: 仅改状态, 身份表数据保留 (与 cancelAccount 同构, 区别在非 C 端 + 通行码免短信)
        account.setState(AccountEnum.State.DESTROY);
        account.setCancelTime(LocalDateTime.now());
        accountRepository.accountEdit(account, null);
    }

    @Override
    public List<AccountStructureTreeVO> findScopeSubAccountStructure(AccountEnum.Client client, Long accountId) {
        AccountParentQuery accountParentQuery = new AccountParentQuery();
        accountParentQuery.setId(accountId);
        accountParentQuery.setClient(client);
//        return subAccountList(accountParentQuery);
        return null;
    }

    @Override
    public @NotNull AccountVO account(AccountEnum.Client client, Long accountId) {
        AccountVO accountVO = accountRepository.account(client, accountId);
        if (accountVO == null) {
            throw new PlatformException(AccountErrorCode.NO_EXIST);
        }
        return accountVO;
    }

    @Override
    public AccountInfo accountInfo(AccountQuery query) {
        return accountRepository.getOne(query, AccountInfo.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AccountVO customSave(AccountSaveReq customSaveReq) {
        AccountVO account = new AccountVO();
        account.init(Collections.singletonList(customSaveReq.getIdentity()), customSaveReq.getUsername(), customSaveReq.getPassword(),
                customSaveReq.getState());

        // 同步pid和inviteId的主次数据关系
        account.setPhone(customSaveReq.getPhone());
        account.setNickname(customSaveReq.getNickname());
        account.setHead(customSaveReq.getHeadImg());
        accountRepository.accountSave(account);
        return account;
    }

    @Override
    public List<AccountVO> accountList(AccountQuery accountQuery) {
        return accountRepository.accountList(accountQuery);
    }

    @Override
    public Page<SimpleAccountRes> simpleAccountPage(SimpleAccountQuery accountQuery) {
        AccountQuery query = accountAssembler.simpleQuery2Query(accountQuery);
        return accountRepository.pageObj(query, SimpleAccountRes.class);
    }

    @Override
    public Page<AccountVO> accountPage(AccountQuery accountQuery) {
        return accountRepository.accountPage(accountQuery);
    }

    @Override
    public AccountVO register(IdentityRegisterRpcReq req) {
        // 保存account
        AccountVO accountVO = new AccountVO();
        accountVO.init(CollUtil.newArrayList(req.getIdentity()), req.getUsername(),req.getPassword(), AccountEnum.State.ENABLE);
        accountVO.setPid(req.getPid());
        // 层级链: 上游按父账号已拼接好(末尾含分隔符), 无父账号时由 accountRepository.accountSave 兜底构建
        accountVO.setPidList(req.getPidList());
        accountVO.setMainAccountId(req.getMainAccountId());
        accountVO.setOrigin(req.getOrigin());
        accountVO.setNickname(req.getNickname());
        accountVO.setInviteAccountId(req.getInviteAccountId());
        accountVO.setHead(req.getHead());
        accountVO.setPhone(req.getPhone());
        accountVO.setId(req.getId());
        // 小程序注册携带 openId 时标记已绑定微信, 供后续快速判断跳过 member/channel 查询
        if (StrUtil.isNotBlank(req.getOpenId())) {
            accountVO.setWxPermission(CommonEnum.YesOrNo.YES);
        }
        Long accountId = accountRepository.accountSave(accountVO);
        accountVO.setId(accountId);

        //初始化财务
        PurseEnum.User user = PurseEnum.User.getByRole(req.getIdentity());
        if (user != null) {
            purseApi.initFinance(accountId, req.getUsername(), user);
        }
        return accountVO;
    }

}
