package com.newzkl.platform.base.common.ddd.facade;

import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

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
    private PurseEnum.FinanceUser financeUser;

    /**
     * 需初始化的子钱包类型
     */
    private List<PurseEnum.PurseType> subPurseType;

    /**
     * 上级账户ID
     */
    private Long parentId;

    /**
     * 杠杆倍率
     */
}
