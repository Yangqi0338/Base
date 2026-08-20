package com.newzkl.platform.base.biz.account.application.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.application.service.AccountService;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicySupport;
import com.newzkl.platform.base.biz.account.domain.repository.AccountRepository;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.model.assembler.AccountAssembler;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityProxySaveReq;
import com.newzkl.platform.base.biz.account.model.req.*;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.vo.MemberAccountVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.model.constant.AccountErrorCode;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/2210:36
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountDomain accountDomain;
    private final AccountRepository accountRepository;

    private final AccountAssembler accountAssembler;


    @Override
    public Page<MemberAccountVO> pageAccount(AccountQuery query) {
        Page<AccountVO> accountPage = accountRepository.accountPage(query);
        if (accountPage == null || CollectionUtils.isEmpty(accountPage.getRecords())) {
            return new Page<>();
        }
        List<AccountVO> accountVOList = accountPage.getRecords();

        Map<String, Integer> groupCountMap = new HashMap<>();
//        if (CollectionUtils.isNotEmpty(userAccounts)) {
//            List<MemberGroupCountVO> memberGroupCountVOS = imUserRpcFacade.countGroupNum(userAccounts);
//            memberGroupCountVOS.forEach(item -> groupCountMap.put(item.getUserAccount(), item.getGroupCount()));
//        }

        Set<Long> pidSet = accountVOList.stream()
                .map(AccountVO::getPid)
                .filter(pid -> ObjectUtils.isNotEmpty(pid) && pid != 0)
                .collect(Collectors.toSet());

        Map<Long, AccountVO> parentAccountMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(pidSet)) {
            List<AccountVO> parentAccountList = accountRepository.accountList(null);
            parentAccountList.forEach(parentVO -> parentAccountMap.put(parentVO.getId(), parentVO));
        }

        List<MemberAccountVO> memberAccountVOList = accountVOList.stream().map(accountVO -> {
            MemberAccountVO memberVO = new MemberAccountVO();
            memberVO.setId(accountVO.getId());
            memberVO.setHead(accountVO.getHead());
            memberVO.setNickname(accountVO.getNickname());
            memberVO.setState(accountVO.getState());
            memberVO.setCreateTime(accountVO.getCreateTime());
            memberVO.setPhone(accountVO.getPhone());

            Long pid = accountVO.getPid();
            if (ObjectUtils.isNotEmpty(pid) && pid != 0) {
                AccountVO parentAccount = parentAccountMap.get(pid);
                if (ObjectUtils.isNotEmpty(parentAccount)) {
                    memberVO.setPid(parentAccount.getId());
                    memberVO.setPUsername(parentAccount.getUsername());
                    memberVO.setPNickname(parentAccount.getNickname());
                }
            }
            return memberVO;
        }).collect(Collectors.toList());

        Page<MemberAccountVO> resultPage = new Page<>();
        resultPage.setRecords(memberAccountVOList);
        resultPage.setTotal(accountPage.getTotal());
        resultPage.setPages(accountPage.getPages());
        resultPage.setSize(accountPage.getSize());
        resultPage.setCurrent(accountPage.getCurrent());

        return resultPage;
    }

    @Override
    public void disableAccount(AdminDisableAccountReq req) {
        AccountVO account = accountDomain.account(req.getClient(), req.getId());

        if (account.getState() != AccountEnum.State.ENABLE) {
            throw new PlatformException(AccountErrorCode.PARAM_ERROR, "用户已注销或被平台禁用，不可操作");
        }
        if (req.getState() != AccountEnum.State.DESTROY && req.getState() != AccountEnum.State.ENABLE) {
            throw new PlatformException(AccountErrorCode.PARAM_ERROR, "只能进行启用或者禁用");
        }
        account.setState(req.getState());
//        accountDomain.accountEdit(account);
        MemberReq command = new MemberReq();
    }

    @Override
    public Long identityCreate(AdminRegisterIdentityReq req) {
        //  构建注册参数并执行会员注册
        IdentityProxySaveReq memberRegisterReq = accountAssembler.adminRegisterReq2ProxyRegisterReq(req);
        AccountEnum.Identity identity = req.getIdentity();

        // 用上级账号查询id（非邀请人）
        if (StrUtil.isNotBlank(req.getSuperiorAccount())) {
            AccountQuery accountQuery = new AccountQuery()
                    .setUserAccount(req.getSuperiorAccount());
            AccountVO account = accountRepository.account(accountQuery);
            if (Objects.isNull(account)) {
                throw new PlatformException(BaseErrorCode.NODATA, "上级账号");
            }
            // 若上级和当前不是同客户端，则视为邀请人
            if (account.getClient() != identity.getClient()) {
                memberRegisterReq.setInviteId(account.getId());
            } else {
                memberRegisterReq.setPid(account.getId());
            }
        }

        // 代理注册
        IdentityRegisterRes registerRes = AbsIdentityPolicySupport.getPolicy(memberRegisterReq.getIdentity())
                .customRegister(new IdentityCustomSaveReq());

        // 注册失败则抛出异常，成功则重新执行登录
        if (registerRes.getErrorCode() != null) {
            throw new PlatformException(registerRes.getErrorCode());
        }

        return registerRes.getId();

    }

}
