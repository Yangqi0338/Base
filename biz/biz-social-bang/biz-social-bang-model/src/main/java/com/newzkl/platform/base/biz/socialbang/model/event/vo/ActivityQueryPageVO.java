package com.newzkl.platform.base.biz.socialbang.model.event.vo;


import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.newzkl.platform.base.common.ddd.model.enums.activity.ActivityEnum;
import lombok.Data;

import java.util.Date;

@Data
public class ActivityQueryPageVO {

    /**
     * id
     */
    private Long id;

    /**
     * 系统自己生成的活动id
     */
    private String activityId;


    /**
     * 活动名称
     */
    private String activityName;


    /**
     * 活动描述
     */
    private String activityDesc;

    /**
     * 状态 {@link ActivityEnum.ExecuteState}
     */
    private String state;


    public String getStateDesc() {
        return ActivityEnum.ExecuteState.getDescByCode(this.state);
    }


    /**
     * 订单金额比例
     */
    private Integer orderAmountsRate;

    /**
     * 分红周期 {@link ActivityEnum.DividendCycle}
     */
    private String dividendCycle;

    public String getDividendCycleDesc() {
        return ActivityEnum.DividendCycle.getDescByCode(this.dividendCycle);
    }

    /**
     * 分红策略 {@link ActivityEnum.SettlementStrategy}
     *
     */
    private String settlementStrategy;


    public String getSettlementStrategyDesc() {
        return ActivityEnum.SettlementStrategy.getDescByCode(this.settlementStrategy);
    }

    /**
     * 分红方式 {@link ActivityEnum.DividendMethod}
     */
    private String dividendMethod;

    public String getDividendMethodDesc() {
        return ActivityEnum.DividendMethod.getDescByCode(this.dividendMethod);
    }

    /**
     * 分红角色
     */
    private String dividendRole;


    public JSONObject getDividendRoleArray() {
        if (this.dividendRole == null) {
            return null;
        }
        return JSONObject.parseObject(this.dividendRole);
    }

    /****
     * 分红用户
     */
    private String dividendUser;

    /**
     * 分红用户 array
     * @return
     */
    public JSONObject getDividendUserArray() {
        if (this.dividendUser == null) {
            return null;
        }
        return JSONObject.parseObject(this.dividendUser);
    }

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;


}
