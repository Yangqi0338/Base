package com.newzkl.platform.base.biz.socialbang.model.event.vo;


import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

/**
 * 详情统计数据
 */
@Data
public class SettleSumDataVO {


    /**
     * 订单信息
     */
    private String orderInfo;


    public JSONObject getOrderInfoObject() {
        if (orderInfo == null) {
            return null;
        }
        return JSONObject.parseObject(orderInfo);
    }

    private String state;


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

    public JSONObject getActualDividendObject() {
        if (actualDividend == null) {
            return null;
        }
        return JSONObject.parseObject(actualDividend);
    }

    /**
     * 奖金池参与人数
     */
    private String poolParticipant;

    public JSONObject getPoolParticipant() {
        if (poolParticipant == null) {
            return null;
        }
        return JSONObject.parseObject(poolParticipant);
    }


}
