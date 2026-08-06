package com.newzkl.platform.base.biz.finance.model.earnings.req;


import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 奖励的分润
 *
 * @author niu
 * @description: 分润请求对象
 * @date 2023/12/18 16:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EarningsAwardExecReq extends EarningsExecReq {

    /**
     * 活动id
     */
    private String activityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 结算单号
     */
    private String settlementId;

    /**
     * 账户id
     */
    private Long accountId;


    /**
     * 账户名称
     */
    private String accountName;

    /**
     * 角色id
     */
    private RoleEnum.CompanyRole role;

    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 流水号
     */
    private String serialId;

    /**
     * {@code ActivityEnum.DividendMethod}
     * 分红方式
     * AVERAGE("AVERAGE", "平均分红"),
     * WEIGHT("WEIGHT","加权分红");
     */
    private String dividendMethod;

    /**
     * 分红周期 {@code ActivityEnum.DividendCycle}
     * 结算周期类型
     */
    private String settlementType;

    /**
     * 分红id
     */
    private Long dividendId;

    /**
     * 金额
     */
    private Money amount;

    /**
     * 分红比例
     */
    private Integer percent;

    @Override
    public Long getId() {
        return dividendId;
    }
}
