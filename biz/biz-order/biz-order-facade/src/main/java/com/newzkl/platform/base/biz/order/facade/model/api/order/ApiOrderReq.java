package com.newzkl.platform.base.biz.order.facade.model.api.order;

import lombok.Data;

import java.io.Serializable;

/**
* 订单查询参数
* @author fang
*/
@Data
public class ApiOrderReq implements Serializable {
    /**
     * 外部订单号
     */
    private String outOrderNo;
    /**
     * 创建开始时间: 13位时间戳
     */
    private Long createBeginTime;
    /**
     * 创建结束时间: 13位时间戳
     */
    private Long createEndTime;
    /**
     * 当前页 默认 1
     */
    private Integer pageNo = 1;
    /**
     * 每页的数量 默认 10
     */
    private Integer pageSize = 10;
}
