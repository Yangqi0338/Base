package com.newzkl.platform.base.biz.order.facade.model.api.order;


import lombok.Data;

import java.time.LocalDateTime;


@Data
public class OrderAmountReq implements java.io.Serializable{

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}
