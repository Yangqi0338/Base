package com.newzkl.platform.base.biz.activity.model.event.req;


import lombok.Data;

@Data
public class UpdateDividendReq {


    /**
     * 结算id
     */
    private String settlementId;


   /**
     * 实际分红,json字符串格式
     * {"actualAmount": null, "actualPercent": null}
     */
    private String actualDividend;


}
