package com.newzkl.platform.base.biz.account.domain.adapt.api;

import com.newzkl.platform.base.biz.account.model.enums.finance.PurseEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 虚拟资产 (期权) 变更入参。
 *
 * <p>迁移: 原 {@code com.zkl.scm.finance.rpc.model.req.VirtualAssetsAlterReq};
 * 因位于 account 出站端口 {@link FinanceVirtualAssetsApi} 签名上, 在 account 域内保留一份中性 DTO。</p>
 *
 * @author KC
 */
@Data
public class VirtualAssetsAlterReq implements Serializable {

    /**
     * 账号 ID
     */
    private Long accountId;

    /**
     * 账号名称
     */
    private String accountName;

    /**
     * 账号类型 (资金侧角色)
     */
    private PurseEnum.FinanceUser accountType;

    /**
     * 资产类型: 1 期权
     */
    private Integer assetsType;

    /**
     * 变更类型: 1 增加
     */
    private Integer alterType;

    /**
     * 变更数值
     */
    private Integer alterValue;

    /**
     * 业务类型: 1 开通码分配
     */
    private Integer businessType;

    /**
     * 变更明细 (JSON)
     */
    private String alterInfo;
}
