package com.newzkl.platform.base.biz.finance.model.earnings.req;


import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import com.newzkl.platform.base.common.ddd.model.query.QuerySupport;
import com.newzkl.platform.base.biz.finance.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author niu
 * @description: 分润记录查询
 * @date 2023/12/23 11:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EarningRecordQuery extends BizPageQuery {

    /**
     * 消费类型
     */
    private List<EarningsEnum.EarningType> earningTypeList;

    /**
     * 消费类型
     */
    private List<EarningsEnum.ConsumeType> consumeTypeList;

    /**
     * 角色
     */
    private List<RoleEnum.CompanyRole> roleList;

    /**
     * 关联订单号
     */
    private Long orderNo;

    /**
     * 结算状态  0：待结算  1：已结算  2:已售后
     */
    private EarningsEnum.State state;

    /**
     * 非结算状态  0：待结算  1：已结算  2:已售后
     */
    private EarningsEnum.State stateNot;

    /**
     * 分红方式
     */
    private String dividendMethod;

    /**
     * 结算开始时间
     */
    private Long earningStartTime;

    /**
     * 结算结算时间
     */
    private Long earningEndTime;

    public void setRoleId(RoleEnum.CompanyRole roleId) {
        this.roleList = this.wrapList(this.roleList, roleId);
    }

    public void setConsumeType(EarningsEnum.ConsumeType consumeType) {
        this.consumeTypeList = QuerySupport.doWrapperList(this.consumeTypeList, consumeType);
    }

    public void setEarningType(EarningsEnum.EarningType earningType) {
        this.earningTypeList = QuerySupport.doWrapperList(this.earningTypeList, earningType);
    }

}
