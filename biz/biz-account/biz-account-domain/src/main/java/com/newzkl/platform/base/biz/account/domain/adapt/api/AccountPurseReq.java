package com.newzkl.platform.base.biz.account.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 钱包查询入参
 *
 * <p>迁移: 跨域 finance 结构 {@code com.zkl.scm.finance.rpc.model.purse.req.AccountPurseReq}
 * 降级为 account 本地端口 DTO。</p>
 *
 * @author KC
 */
@Data
public class AccountPurseReq implements Serializable {

    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 账户ID集合
     */
    private List<Long> accountIdList;

    /**
     * 账户类型
     */
    private Integer accountType;

    /**
     * 钱包类型
     */
    private Integer purseType;
}
