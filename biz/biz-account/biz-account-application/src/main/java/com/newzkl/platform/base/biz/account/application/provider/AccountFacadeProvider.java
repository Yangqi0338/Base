package com.newzkl.platform.base.biz.account.application.provider;

import cn.hutool.core.collection.CollectionUtil;
import com.newzkl.platform.base.biz.account.domain.repository.AccountRepository;
import com.newzkl.platform.base.biz.account.domain.repository.MemberRepository;
import com.newzkl.platform.base.biz.account.facade.AccountFacade;
import com.newzkl.platform.base.biz.account.model.req.AccountQuery;
import com.newzkl.platform.base.biz.account.model.res.AccountInfo;
import com.newzkl.platform.base.biz.account.model.vo.MemberVO;
import com.newzkl.platform.base.common.ddd.facade.AccountGroupVO;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
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
        return new AccountGroupVO().setUserAccount(account.getUserAccount()).setNickname(account.getNickname()).setPhone(account.getPhone()).setHead(account.getHead()).setId(account.getId());
    }

    @Override
    public AccountGroupVO accountInfo(CommonEnum.Client client, Long id) {
        AccountVO account = accountRepository.account(client,id);
        if (account == null) {
            return null;
        }
        return new AccountGroupVO().setUserAccount(account.getUserAccount()).setNickname(account.getNickname()).setPhone(account.getPhone()).setHead(account.getHead()).setId(account.getId());

    }
}
