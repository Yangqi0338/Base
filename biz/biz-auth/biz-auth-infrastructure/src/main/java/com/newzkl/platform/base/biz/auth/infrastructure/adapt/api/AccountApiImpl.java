package com.newzkl.platform.base.biz.auth.infrastructure.adapt.api;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.account.facade.AccountFacade;
import com.newzkl.platform.base.biz.account.facade.model.AccountRpcQuery;
import com.newzkl.platform.base.biz.auth.domain.adapt.api.AccountApi;
import com.newzkl.platform.base.common.ddd.facade.IdentityRegisterRpcReq;
import com.newzkl.platform.base.common.ddd.facade.AccountRpcVO;
import com.newzkl.platform.base.common.ddd.infrastructure.rpc.RpcReference;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class AccountApiImpl implements AccountApi {

    @RpcReference
    private AccountFacade accountFacade;

    @Override
    public AccountRpcVO account(AccountEnum.Client client, Long accountId) {
        AccountRpcQuery query = new AccountRpcQuery();
        query.setId(accountId);
        query.setClient(client);
        return accountFacade.accountInfo(query);
    }

    @Override
    public AccountRpcVO account(AccountEnum.Client client, String username) {
        AccountRpcQuery query = new AccountRpcQuery();
        query.setUsername(username);
        query.setClient(client);
        return accountFacade.accountInfo(query);
    }

    @Override
    public AccountRpcVO account(String yqm) {
        AccountRpcQuery query = new AccountRpcQuery();
        query.setYqm(yqm);
        return accountFacade.accountInfo(query);
    }

    @Override
    public List<AccountRpcVO> accountList(AccountEnum.Client client, String username) {
        AccountRpcQuery query = new AccountRpcQuery();
        query.setUsername(username);
        query.setClient(client);
        return accountFacade.accountInfoList(query);
    }

    @Override
    public List<AccountRpcVO> loginAccountList(AccountEnum.Client client, String username) {
        AccountRpcQuery query = new AccountRpcQuery();
        query.setUsername(username);
        query.setClient(client);
        query.setState(AccountEnum.State.ENABLE);
        return accountFacade.accountInfoList(query);
    }

    @Override
    public AccountRpcVO register(List<IdentityRegisterRpcReq> registerRpcReq) {
        return accountFacade.register(registerRpcReq);
    }

    @Override
    public boolean updateLoginTime(AccountEnum.Client client, Long id) {
        log.info("【登录时间更新】开始更新账号{}的登录时间", id);
        AccountRpcVO rpcVO = new AccountRpcVO();
        rpcVO.setId(id);
        rpcVO.setLastLoginTime(LocalDateTime.now());
        rpcVO.setClient(client);
        return accountFacade.accountEdit(rpcVO);
    }

    @Override
    public boolean exists(String newUsername, AccountEnum.Client client) {
        AccountRpcQuery query = new AccountRpcQuery();
        query.setUsername(newUsername);
        query.setClient(client);
        return accountFacade.accountInfo(query) != null;
    }

    @Override
    public boolean accountEdit(AccountRpcVO accountUpdate) {
        return accountFacade.accountEdit(accountUpdate);
    }

    @Override
    public boolean bindWx(AccountEnum.Identity identity, Long accountId, String openId, String unionId) {
        return accountFacade.bindWx(identity, accountId, openId, unionId);
    }
}
