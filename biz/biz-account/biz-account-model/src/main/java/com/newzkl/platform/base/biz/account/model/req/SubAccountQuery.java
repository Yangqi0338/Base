package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 子账号分页查询
 *
 * <p>主账号视角查自己名下子账号, mainAccountId 过滤由 service 从登录态注入, 不入参防越权</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SubAccountQuery extends BizPageQuery {
    /**
     * 登录账号(模糊)
     */
    private String username;
    /**
     * 账号状态
     */
    private AccountEnum.State state;
}
