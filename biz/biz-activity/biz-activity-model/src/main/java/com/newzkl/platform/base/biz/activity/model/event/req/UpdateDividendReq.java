package com.newzkl.platform.base.biz.activity.model.event.req;


import lombok.Data;

/**
 * 更新分红请求对象
 */
@Data
public class UpdateDividendReq {


    /**
     * 结算id
     */
    private String settlementId;


    /**
     * 实际分红
     * @ext json字符串格式 {"actualAmount": null, "actualPercent": null}
     */
    private String actualDividend;


}
