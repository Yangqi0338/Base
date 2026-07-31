package com.newzkl.platform.base.biz.activity.model.event.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Description: 历史奖金池数据
 * @Author: niu
 * @Date: 2024/1/16 16:01
 */
@Data
public class SettleHistoryBonusPoolDataListExcelVO {

    /**
     * 奖金池id
     */
    @ExcelProperty("奖金池id")
    @ColumnWidth(value = 20)
    private Long bonusPoolId;

    /**
     * 活动id
     */
    @ExcelProperty("活动id")
    @ColumnWidth(value = 20)
    private String activityId;

    /**
     * 活动名称
     */
    @ExcelProperty("活动名称")
    @ColumnWidth(value = 20)
    private String activityName;

    /**
     * 结算id
     */
    @ExcelProperty("结算id")
    @ColumnWidth(value = 20)
    private String settlementId;

    /**
     * 结算名称
     */
    @ExcelProperty("结算名称")
    @ColumnWidth(value = 20)
    private String settlementName;

    /**
     * 订单信息 "orderCount": 订单统计, "totalOrderAmount": 订单金额
     */
    //private String orderInfo;


    /**
     * 订单统计 "orderCount"
     */
    @ExcelProperty("订单统计")
    @ColumnWidth(value = 20)
    private Integer orderCount;

    /**
     * 订单信息订单金额
     */
    @ExcelProperty("订单金额")
    @ColumnWidth(value = 20)
    private BigDecimal totalOrderAmount;





    /*@ExcelProperty("预估分红")
    @ColumnWidth(value = 20)
    private String estimatedDividend;*/
    /**
     * 预估分红金额 {"estimatedAmount": 30, "estimatedPercent": "30%"}
     */
    @ExcelProperty("预估分红金额")
    @ColumnWidth(value = 20)
    private BigDecimal estimatedAmount;

    /**
     * 预估分红比例
     */
    @ExcelProperty("预估分红比例")
    @ColumnWidth(value = 20)
    private String estimatedPercent;



    /*public JSONObject getEstimatedDividendObject() {
        if (estimatedDividend == null) {
            return null;
        }
        return JSONObject.parseObject(estimatedDividend);
    }*/

   /* *//**
     * 实际分红 {"actualMount": 30, "actualPercent": "30%"}
     *//*
    @ExcelProperty("实际分红")
    @ColumnWidth(value = 20)
    private String actualDividend;*/

    /**
     * 实际分红金额
     */
    @ExcelProperty("实际分红金额")
    @ColumnWidth(value = 20)
    private BigDecimal actualAmount;

    /**
     * 奖金池实际分红比例
     */
    @ExcelProperty("奖金池实际分红比例")
    @ColumnWidth(value = 20)
    private String actualPercent;


    /**
     * 交易师
     */
    @ExcelProperty("交易师参与人数")
    @ColumnWidth(value = 20)
    private Integer dealer;

    /**
     * 运营商
     */
    @ExcelProperty("运营商参与人数")
    @ColumnWidth(value = 20)
    private Integer operator;

    /**
     * 甄选师
     */
    @ExcelProperty("甄选师参与人数")
    @ColumnWidth(value = 20)
    private Integer selector;

    /**
     * 奖金池参与人数
     * "dealer": 交易师, "operator": 运营商, "selector": 交易师
     * @return
     *//*
    @ExcelProperty("奖金池参与人数")
    @ColumnWidth(value = 20)
    private String poolParticipant;*/

    /*public JSONObject getPoolParticipant() {
        if (poolParticipant == null) {
            return null;
        }
        return JSONObject.parseObject(poolParticipant);
    }*/


    /**
     * 分红方式
     */
    @ExcelProperty("分红方式")
    @ColumnWidth(value = 20)
    private String dividendMethod;


    /**
     * 分红周期
     */
    @ExcelProperty("分红周期")
    @ColumnWidth(value = 20)
    private String dividendCycle;


    /**
     * 确认时间
     */
    @ExcelProperty("确认时间")
    @ColumnWidth(value = 20)
    private LocalDateTime confirmTime;


    /**
     * 状态
     */
    @ExcelProperty("状态")
    @ColumnWidth(value = 20)
    private String state;



}
