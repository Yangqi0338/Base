package com.newzkl.platform.base.biz.account.domain.adapt.api;

import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 收益贡献查询入参
 *
 * <p>迁移: 跨域 finance 结构
 * {@code com.zkl.scm.finance.rpc.model.earnings.req.AccountContributeRpcQuery}
 * 降级为 account 本地端口 DTO。</p>
 *
 * @author KC
 */
@Data
public class AccountContributeRpcQuery implements Serializable {

    /**
     * 账户ID集合
     */
    private List<Long> accountIds;

    /**
     * 资金账户类型
     */
    private PurseEnum.FinanceUser accountType;
}
