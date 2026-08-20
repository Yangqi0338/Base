package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * 账号关键号查询
 *
 * @author ruoyi
 */
@Data
public class AccountKeyQuery implements Serializable {

    /**
     * 归属端
     */
    private AccountEnum.Client client;

    /**
     * 账号id
     */
    private Long accountId;

    /**
     * 基于当前登录态构建查询条件
     *
     * @return 填充了归属端与账号id的查询对象
     */
    public static AccountKeyQuery build() {
        AccountKeyQuery accountKeyQuery = new AccountKeyQuery();
        accountKeyQuery.setClient(SecurityUtils.getClient());
        accountKeyQuery.setAccountId(SecurityUtils.getAccountId());
        return accountKeyQuery;
    }

}
