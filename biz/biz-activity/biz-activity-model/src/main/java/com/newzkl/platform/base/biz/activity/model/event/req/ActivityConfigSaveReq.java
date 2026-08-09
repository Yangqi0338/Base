package com.newzkl.platform.base.biz.activity.model.event.req;

import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.common.ddd.model.enums.activity.ActivityEnum;
import com.newzkl.platform.base.biz.activity.model.validator.ActivityIdValid;
import com.newzkl.platform.base.common.ddd.model.check.AddCommand;
import com.newzkl.platform.base.common.ddd.model.check.UpdateCommand;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @Description: 活动配置保存请求对象
 * @Author: niu
 * @Date: 2024/1/9 16:20
 */
@Data
public class ActivityConfigSaveReq {

    /**
     * id
     */
    @NotNull(groups = UpdateCommand.class)
    private Long id;

    /**
     * 活动id
     * @ext 自定义 FHC+创建日期+6位自编码000001, 如 FHC 2025 08 31 10 30 30 000001
     */
    @ActivityIdValid(groups = UpdateCommand.class)
    private String  activityId;

    /**
     * 策略id
     */
    private Long strategyId;

    /**
     * 策略描述
     */
    private String strategyDesc;

    /**
     * 策略方式
     * @ext 候选枚举 {@link ActivityEnum.StrategyMode}: 0-贡献值分配 1-甄选师等级分配
     */
    private Integer strategyMode;



    /**
     * 状态
     * @ext 候选枚举 {@link ActivityEnum.ExecuteState}: PENDING-未生效 ACTIVE-生效中 CANCELLED-已作废
     */
    private String state;

    /**
     * 活动名称
     */
    @NotBlank(groups = {AddCommand.class, UpdateCommand.class})
    private String activityName;


    /**
     * 活动描述
     */
    private String activityDesc;


    /**
     * 订单金额比例
     */
    @NotNull(groups = {AddCommand.class, UpdateCommand.class})
    private Integer orderAmountsRate;

    /**
     * 分红周期
     * @ext 候选枚举 {@link ActivityEnum.DividendCycle}: WEEKLY-周结算 MONTHLY-月结算
     */
    @NotBlank(groups = {AddCommand.class, UpdateCommand.class})
    private String dividendCycle;


    /**
     * 分红策略
     * @ext 候选枚举 {@link ActivityEnum.SettlementStrategy}: CYCLE-周期循环 ONCE-单次结算后关闭
     */
    @NotBlank(groups = {AddCommand.class, UpdateCommand.class})
    private String settlementStrategy;



    /**
     * 分红方式
     * @ext 候选枚举 {@link ActivityEnum.DividendMethod}: AVERAGE-平均分红 WEIGHT-加权分红
     */
    @NotBlank(groups = {AddCommand.class, UpdateCommand.class})
    private String dividendMethod;


    /**
     * 分红角色
     * @ext 候选枚举 {@link com.zkl.scm.model.constants.user.RoleEnum}: 1004-运营商 1005-交易师 1006-甄选师; 传参格式 {"roles": [1004, 1005]}
     */
    @NotBlank(groups = {AddCommand.class, UpdateCommand.class})
    private String dividendRole;

    public JSONObject getDividendRoleArray() {
        if (this.dividendRole == null) {
            return null;
        }
        return JSONObject.parseObject(this.dividendRole);
    }

    /**
     * 分红用户
     * @ext 传参格式 [{"userId":1, "roleName": "运营商", "username": "10010011004", "time":"2025-10-21 20:00:00"}]
     */
    private String dividendUser;



    public JSONObject getDividendUserArray() {
        if (this.dividendUser == null) {
            return null;
        }
        return JSONObject.parseObject(this.dividendUser);
    }







}
