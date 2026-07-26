package com.newzkl.platform.base.biz.account.domain.adapt.api;

import com.newzkl.platform.base.biz.account.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 收益查询入参。
 *
 * <p>迁移: 跨域 finance 结构 {@code com.zkl.scm.finance.rpc.model.earnings.req.IncomeQuery}
 * 降级为 account 本地端口 DTO。</p>
 *
 * @author KC
 */
@Data
public class IncomeQuery implements Serializable {

    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 状态 (等于)
     */
    private Integer state;

    /**
     * 状态 (不等于)
     */
    private Integer stateNot;

    /**
     * 消费类型集合
     */
    private List<EarningsEnum.ConsumeType> consumeTypeList;

    /**
     * 角色集合
     */
    private List<RoleEnum.CompanyRole> roleList;

    /**
     * 设置单个消费类型 (内部转为集合)。
     *
     * @param consumeType 消费类型
     */
    public void setConsumeType(EarningsEnum.ConsumeType consumeType) {
        this.consumeTypeList = consumeType == null ? List.of() : List.of(consumeType);
    }
}
