package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
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
    private CommonEnum.Client client;

    /**
     * 账号id
     */
    private Long accountId;

    public static AccountKeyQuery build() {
        AccountKeyQuery accountKeyQuery = new AccountKeyQuery();
        accountKeyQuery.setClient(SecurityUtils.getClient());
        accountKeyQuery.setAccountId(SecurityUtils.getAccountId());
        return accountKeyQuery;
    }

}
