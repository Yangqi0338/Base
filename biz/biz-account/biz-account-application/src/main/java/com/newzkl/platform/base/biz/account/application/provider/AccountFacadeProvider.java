package com.newzkl.platform.base.biz.account.application.provider;

import cn.hutool.core.collection.CollectionUtil;
import com.newzkl.platform.base.biz.account.domain.policy.AbsAccountPolicySupport;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicySupport;
import com.newzkl.platform.base.biz.account.domain.repository.AccountRepository;
import com.newzkl.platform.base.biz.account.domain.repository.MemberRepository;
import com.newzkl.platform.base.biz.account.domain.service.ChannelClientDomain;
import com.newzkl.platform.base.biz.account.facade.AccountFacade;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.AccountRegisterRes;
import com.newzkl.platform.base.biz.account.model.req.ChannelCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.ChannelReq;
import com.newzkl.platform.base.biz.account.model.req.IdentityRegisterRes;
import com.newzkl.platform.base.biz.account.model.vo.MemberVO;
import com.newzkl.platform.base.common.ddd.facade.AccountGroupVO;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.facade.ChannelRegisterReq;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.ChannelEnum;
import com.newzkl.platform.base.common.ddd.model.vo.AccountInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountFacadeProvider implements AccountFacade {

    private final AccountRepository accountRepository;
    private final ChannelClientDomain channelDomain;
    private final MemberRepository memberRepository;

    @Override
    public List<AccountGroupVO> listAccountByIds(List<Long> accountIdList) {
        List<AccountVO> accountVOList = accountRepository.listAccountByIds(accountIdList);
        return TransferUtils.transfers(accountVOList, AccountGroupVO::new);
    }

    @Override
    public List<Long> queryMember(String nickname) {
        List<MemberVO> memberVOS = memberRepository.queryMember(nickname);
        if (CollectionUtil.isNotEmpty(memberVOS)){
            return memberVOS.stream().map(MemberVO::getId).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public AccountGroupVO selectByUserAccount(String userAccount) {
        AccountVO account = accountRepository.selectByUserAccount(userAccount);
        if (account == null) {
            return null;
        }
        AccountGroupVO accountGroupVO = new AccountGroupVO();
        accountGroupVO.setId(account.getId());
        return accountGroupVO.setUserAccount(account.getUserAccount()).setNickname(account.getNickname()).setPhone(account.getPhone()).setHead(account.getHead());
    }

    @Override
    public AccountGroupVO accountInfo(CommonEnum.Client client, Long id) {
        AccountVO account = accountRepository.account(client,id);
        if (account == null) {
            return null;
        }
        AccountGroupVO accountGroupVO = new AccountGroupVO();
        accountGroupVO.setId(account.getId());
        return new AccountGroupVO().setUserAccount(account.getUserAccount()).setNickname(account.getNickname()).setPhone(account.getPhone()).setHead(account.getHead());

    }

    @Override
    public boolean registerChannel(ChannelRegisterReq req) {
        Long accountId = req.getAccountId();
        RoleEnum.CompanyRole role = req.getRole();
        CommonEnum.YesOrNo storePermission = req.getStorePermission();
        String phone = req.getContactPhone();
        String contactName = req.getContactName();
        String storeName = req.getStoreName();

        if (RoleEnum.CompanyRole.CHANNEL == role) {
            ChannelReq channelReq = new ChannelReq();
            channelReq.setState(ChannelEnum.State.OPEN);
            channelReq.setStorePermission(storePermission);
            channelReq.setContactsName(contactName);
            channelReq.setStoreName(storeName);
            channelReq.setContactsWay(phone);

            return channelDomain.channelEdit(channelReq) > 0;
        } else {
            AccountVO accountVO = accountRepository.account(role.getClient(), accountId);
            IdentityCustomSaveReq saveReq = new IdentityCustomSaveReq();
            saveReq.setUsername(accountVO.getUsername());
            saveReq.setHeadImg(accountVO.getHead());
            saveReq.setContactsWay(phone);
            saveReq.setName(accountVO.getRealName());
            saveReq.setStoreName(storeName);
            saveReq.setContactsName(contactName);
//        saveReq.setChannelType();
            saveReq.setStorePermission(storePermission);
            IdentityRegisterRes registerRes = AbsIdentityPolicySupport.getPolicy(role)
                    .customRegister(saveReq);
            return registerRes.isSuccess();
        }
    }
}
