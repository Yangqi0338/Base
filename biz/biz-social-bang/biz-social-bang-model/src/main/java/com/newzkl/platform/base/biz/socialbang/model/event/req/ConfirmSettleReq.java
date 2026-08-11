package com.newzkl.platform.base.biz.socialbang.model.event.req;


import lombok.Data;

/**
 * 确认结算请求对象
 */
@Data
public class ConfirmSettleReq {

    /**
     * 结算id
     */
    private String settlementId;
}
