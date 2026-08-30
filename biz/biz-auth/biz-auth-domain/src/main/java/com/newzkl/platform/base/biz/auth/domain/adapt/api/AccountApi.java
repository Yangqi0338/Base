package com.newzkl.platform.base.biz.auth.domain.adapt.api;

import com.newzkl.platform.base.common.ddd.facade.IdentityRegisterRpcReq;
import com.newzkl.platform.base.common.ddd.facade.AccountRpcVO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public interface AccountApi {
    AccountRpcVO account(AccountEnum.Client client, Long accountId);
    AccountRpcVO account(AccountEnum.Client client, String username);
    AccountRpcVO account(String yqm);

    /**
     * 按凭证查全部端账号
     *
     * <p>client 为空时不限端, 同一 username 可能存在多端账号, 返回全部供登录侧按端优先级择主</p>
     *
     * @param client   端, 可空
     * @param username 登录凭证 (username | phone)
     * @return 账号列表
     */
    List<AccountRpcVO> accountList(AccountEnum.Client client, String username);
    List<AccountRpcVO> loginAccountList(AccountEnum.Client client, String username);

    AccountRpcVO register(List<IdentityRegisterRpcReq> registerRpcReq);

    boolean updateLoginTime(AccountEnum.Client client, Long id);

    boolean exists(String newUsername, AccountEnum.Client client);

    boolean accountEdit(AccountRpcVO accountUpdate);
}
