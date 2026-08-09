package com.newzkl.platform.base.biz.finance.model.earnings.req;

import com.newzkl.platform.base.common.ddd.model.query.QuerySupport;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 收益查询
 */
@Data
public class IncomeQuery implements Serializable {
    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 结算状态
     * @ext 编码值, 候选枚举 EarningsEnum.State; 保留 Integer
     */
    private Integer state;

    /**
     * 非结算状态
     * @ext 编码值, 候选枚举 EarningsEnum.State; 保留 Integer
     */
    private Integer stateNot;

    /**
     * 消费类型列表
     */
    private List<EarningsEnum.ConsumeType> consumeTypeList;

    public void setConsumeType(EarningsEnum.ConsumeType consumeType) {
        this.consumeTypeList = QuerySupport.doWrapperList(this.consumeTypeList, consumeType);
    }

    /**
     * 角色
     */
    private List<RoleEnum.CompanyRole> roleList;
}
