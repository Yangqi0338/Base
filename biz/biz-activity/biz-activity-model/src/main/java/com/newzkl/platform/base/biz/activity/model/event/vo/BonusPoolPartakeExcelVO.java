package com.newzkl.platform.base.biz.activity.model.event.vo;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class BonusPoolPartakeExcelVO {

    /**
     * 流水号
     */
    private String serialId;


    /**
     * 昵称
     */
    private String nickName;


    /**
     * 角色名称
     */
    private String roleName;



    /**
     * 手机号
     */
    private String phone;


    /**
     * 角色等级
     */
    private BigDecimal personPercent;


    /**
     * 分红奖金
     */
    private BigDecimal dividendAmount;


    /**
     * 结算周期
     */
    private String dividendCycle;
}
