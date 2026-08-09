package com.newzkl.platform.base.biz.finance.model.earnings.req;


import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 奖励分润执行请求
 *
 * @author niu
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
     * 分红方式
     * @ext AVERAGE 平均分红 WEIGHT 加权分红; 候选枚举 ActivityEnum.DividendMethod; 保留 String
     */
    private String dividendMethod;

    /**
     * 结算周期类型
     * @ext 候选枚举 ActivityEnum.DividendCycle; 保留 String
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
