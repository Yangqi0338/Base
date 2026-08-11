package com.newzkl.platform.base.biz.socialbang.model.event.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.common.ddd.model.enums.activity.ActivityEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description: 历史奖金池数据
 * @Author: niu
 * @Date: 2024/1/16 16:01
 */
@Data
public class SettleHistoryBonusPoolDataListVO {

    /**
     * 奖金池id
     */
    private Long bonusPoolId;

    /**
     * 活动id
     */
    private String activityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 结算id
     */
    private String settlementId;

    /**
     * 结算名称
     */
    private String settlementName;

    /**
     * 订单信息 "orderCount": 订单统计, "totalOrderAmount": 订单金额
     */
    private String orderInfo;

    /**
     * 获取订单信息
     * @return
     */
    public JSONObject getOrderInfoObject() {
        if (orderInfo == null) {
            return null; //
        }
        return JSONObject.parseObject(orderInfo);
    }

    /**
     * 预估分红
     */
    private String estimatedDividend;

    public JSONObject getEstimatedDividendObject() {
        if (estimatedDividend == null) {
            return null;
        }
        return JSONObject.parseObject(estimatedDividend);
    }

    /**
     * 实际分红
     */
    private String actualDividend;


    /**
     * 获取实际分红 actualMount :实际分红 ；actualPercent：实际分红比例
     * @return
     */
    public JSONObject getActualDividendObject() {
        if (actualDividend == null) {
            return null;
        }
        return JSONObject.parseObject(actualDividend);
    }

    /**
     * 奖金池参与人数
     * "dealer": 交易师, "operator": 运营商, "selector": 交易师
     * @return
     */
    private String poolParticipant;

    public JSONObject getPoolParticipant() {
        if (poolParticipant == null) {
            return null;
        }
        return JSONObject.parseObject(poolParticipant);
    }


    /**
     * 分红方式
     */
    private String dividendMethod;

    public String getDividendMethodDesc() {
        return ActivityEnum.DividendMethod.getDescByCode(this.dividendMethod);
    }

    /**
     * 分红方式
     */
    private String dividendCycle;

    public String getDividendCycleDesc() {
        return ActivityEnum.DividendCycle.getDescByCode(this.dividendCycle);
    }

    /**
     * 确认时间
     */
    @ExcelProperty("确认时间")
    @ColumnWidth(value = 20)
    private LocalDateTime confirmTime;

    private String state;

    public String getStateDesc() {
        return ActivityEnum.SettleState.getDescByCode(this.state);
    }


}
