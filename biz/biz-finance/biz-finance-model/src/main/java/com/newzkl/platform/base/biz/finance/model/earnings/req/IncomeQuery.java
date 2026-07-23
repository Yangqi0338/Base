package com.newzkl.platform.base.biz.finance.model.earnings.req;

import com.newzkl.platform.base.common.ddd.model.query.QuerySupport;
import com.newzkl.platform.base.biz.finance.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.biz.finance.model.enums.user.identity.RoleEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class IncomeQuery implements Serializable {
    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 非状态
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
