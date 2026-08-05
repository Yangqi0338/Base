package com.newzkl.platform.base.biz.order.domain.adapt.api;



import com.newzkl.platform.base.common.ddd.facade.AccountGroupVO;

import java.util.List;

/**
 * 账户出站端口
 *
 * @author KC
 */
public interface AccountApi {

    /**
     * 按账户ID批量查询账户信息
     *
     * @param accountIdList 账户ID列表
     * @return 账户信息列表, 恒非 null
     */
    List<AccountGroupVO> listAccountByIds(List<Long> accountIdList);

    /**
     * 按昵称查询会员账户ID
     *
     * @param nickname 会员昵称
     * @return 会员账户ID列表, 恒非 null
     */
    List<Long> queryMember(String nickname);

    /**
     * 按登录账号查询账户信息
     *
     * @param userAccount 登录账号
     * @return 账户信息, 无则 null
     */
    AccountGroupVO selectByUserAccount(String userAccount);

    /**
     * 获取账号信息
     *
     * @param id
     * @return
     */
    AccountGroupVO accountInfo(Long id);
    AccountGroupVO channelInfo(Long id);
}
