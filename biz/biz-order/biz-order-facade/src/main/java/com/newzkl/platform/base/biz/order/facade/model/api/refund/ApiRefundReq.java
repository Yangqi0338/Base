package com.newzkl.platform.base.biz.order.facade.model.api.refund;

import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
* 售后单查询参数
* @author fang
*/
@Data
public class ApiRefundReq implements Serializable {
    /**
     * 售后单号
     */
    private Long id;
    /**
     * 售后单号集合
     */
    private List<Long> idList;
    /**
     * 售后状态
     */
    private RefundEnum.State refundState;
    /**
     * 售后类型
     */
     private RefundEnum.RefundType refundType;
    /**
     * 创建开始时间
     */
    private Long createBeginTime;
    /**
     * 创建结束时间
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
