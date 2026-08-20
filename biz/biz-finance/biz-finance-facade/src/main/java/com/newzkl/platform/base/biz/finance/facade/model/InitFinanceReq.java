package com.newzkl.platform.base.biz.finance.facade.model;

import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 初始化钱包入参
 *
 * <p>迁移: 跨域 finance 结构 {@code com.zkl.scm.finance.rpc.model.purse.req.InitFinanceReq}
 * 降级为 account 本地端口 DTO。</p>
 *
 * @author KC
 */
@Data
public class InitFinanceReq implements Serializable {

    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 账户名称
     */
    private String accountName;

    /**
     * 资金账户类型
     */
    private PurseEnum.User purseUser;
}
