package com.newzkl.platform.base.biz.account.application.provider;

import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicy;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicySupport;
import com.newzkl.platform.base.biz.account.domain.repository.AccountRepository;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.domain.service.ChannelClientDomain;
import com.newzkl.platform.base.biz.account.facade.AccountFacade;
import com.newzkl.platform.base.biz.account.facade.model.AccountRpcQuery;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.AccountQuery;
import com.newzkl.platform.base.biz.account.model.req.AccountReq;
import com.newzkl.platform.base.biz.account.model.req.ChannelReq;
import com.newzkl.platform.base.biz.account.model.req.IdentityRegisterRes;
import com.newzkl.platform.base.common.ddd.facade.AccountGroupVO;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.facade.AccountRpcVO;
import com.newzkl.platform.base.common.ddd.facade.ChannelRegisterReq;
import com.newzkl.platform.base.common.ddd.facade.IdentityRegisterRpcReq;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.ChannelEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountFacadeProvider implements AccountFacade {

    private final AccountRepository accountRepository;
    private final AccountDomain accountDomain;
    private final ChannelClientDomain channelDomain;

    @Override
    public List<AccountGroupVO> listAccountByIds(List<Long> accountIdList) {
        List<AccountVO> accountVOList = accountRepository.listAccountByIds(accountIdList);
        return TransferUtils.transfers(accountVOList, AccountGroupVO::new);
    }

    @Override
    public List<Long> queryMember(String nickname) {
        AccountQuery query = new AccountQuery();
        query.setNickname(nickname);
        query.setClient(AccountEnum.Client.USER);
        return accountRepository.findIdList(query);
    }

    @Override
    public AccountGroupVO selectByUserAccount(String userAccount) {
        AccountVO account = accountRepository.selectByUserAccount(userAccount);
        if (account == null) {
            return null;
        }
        AccountGroupVO accountGroupVO = new AccountGroupVO();
        accountGroupVO.setId(account.getId());
        return accountGroupVO.setNickname(account.getNickname()).setPhone(account.getPhone()).setHead(account.getHead());
    }

    @Override
    public AccountGroupVO accountInfo(AccountEnum.Client client, Long id) {
        AccountVO account = accountRepository.account(client,id);
        if (account == null) {
            return null;
        }
        AccountGroupVO accountGroupVO = new AccountGroupVO();
        accountGroupVO.setId(account.getId());
        return new AccountGroupVO().setNickname(account.getNickname()).setPhone(account.getPhone()).setHead(account.getHead());

    }

    @Override
    public boolean registerChannel(ChannelRegisterReq req) {
        Long accountId = req.getAccountId();
        AccountEnum.Identity identity = req.getIdentity();
        CommonEnum.YesOrNo storePermission = req.getStorePermission();
        String phone = req.getContactPhone();
        String contactName = req.getContactName();
        String storeName = req.getStoreName();

        if (AccountEnum.Identity.CHANNEL == identity) {
            ChannelReq channelReq = new ChannelReq();
            channelReq.setState(ChannelEnum.State.OPEN);
            channelReq.setStorePermission(storePermission);
            channelReq.setContactsName(contactName);
            channelReq.setStoreName(storeName);
            channelReq.setContactsWay(phone);

            return channelDomain.channelEdit(channelReq);
        } else {
            AccountVO accountVO = accountRepository.account(identity.getClient(), accountId);
            IdentityCustomSaveReq saveReq = new IdentityCustomSaveReq();
            saveReq.setHead(accountVO.getHead());
            saveReq.setContactsWay(phone);
            saveReq.setName(accountVO.getRealName());
            saveReq.setStoreName(storeName);
            saveReq.setContactsName(contactName);
//        saveReq.setChannelType();
            saveReq.setStorePermission(storePermission);
            IdentityRegisterRes registerRes = AbsIdentityPolicySupport.getPolicy(identity)
                    .customRegister(saveReq);
            return registerRes.isSuccess();
        }
    }

    @Override
    public AccountRpcVO accountInfo(AccountRpcQuery query) {
        AccountVO account = accountRepository.account(TransferUtils.transfer(query, AccountQuery::new));
        return TransferUtils.transfer(account, AccountRpcVO::new);
    }

    @Override
    public List<AccountRpcVO> accountInfoList(AccountRpcQuery query) {
        List<AccountVO> accountList = accountRepository.accountList(TransferUtils.transfer(query, AccountQuery::new));
        return TransferUtils.transfers(accountList, AccountRpcVO::new);
    }

    @Override
    public AccountRpcVO register(List<IdentityRegisterRpcReq> registerRpcReq) {
        AccountRpcVO mainAccount = null;
        for (int i = 0; i < registerRpcReq.size(); i++) {
            IdentityRegisterRpcReq req = registerRpcReq.get(i);
            // 保存 account
            AccountVO account = accountDomain.register(req);

            // 角色初始化: 组装身份初始化参数并分发到对应角色策略
            IdentityCustomSaveReq saveReq = new IdentityCustomSaveReq();
            saveReq.setId(account.getId());
            saveReq.setNickname(req.getNickname());
            saveReq.setHead(req.getHead());
            saveReq.setRegisterOnce(req.isRegisterOnce());
            AbsIdentityPolicy policy = AbsIdentityPolicySupport.getPolicy(req.getIdentity());
            policy.customRegister(saveReq);
            // 端默认身份(自助注册主账号)自动授本端超管; 非默认身份(如员工)由管理员分配, 不触发
            policy.autoBindDefaultSuperAdmin(account.getId());

            // 首个为主角色账号, 回传供上游注册后登录
            if (i == 0) {
                mainAccount = TransferUtils.transfer(account, AccountRpcVO::new);
            }
        }
        return mainAccount;
    }

    @Override
    public boolean accountEdit(AccountRpcVO rpcVO) {
        AccountReq req = TransferUtils.transfer(rpcVO, AccountReq.class);
        return accountDomain.accountEdit(req);
    }
}
