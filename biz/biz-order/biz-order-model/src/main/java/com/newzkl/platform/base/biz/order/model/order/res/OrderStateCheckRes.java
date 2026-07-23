package com.newzkl.platform.base.biz.order.model.order.res;

import lombok.Data;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/1/2019:39
 */
@Data
public class OrderStateCheckRes {
    private String orderNo;
    private String spuOrderNo;
    private Integer currentState;
    private Integer toState;
    private String outOrderNo;
    private Long channelId;
    private Integer orderType;
}
