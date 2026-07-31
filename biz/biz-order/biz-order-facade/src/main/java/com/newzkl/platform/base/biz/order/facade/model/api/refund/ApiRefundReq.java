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
     * (0,"待渠道商审核"),(2,"待供应商审核"),(4,"待提交物流"),(6,"待确认收货"),(7,"待平台介入"),(8,"平台介入中"),(9,"退款中"),(10,"已完成"),(-2,"已拒绝"),(-4,"已关闭"),
     */
    private RefundEnum.State refundState;
    /**
     * 售后类型 (0 仅退款 1 退货退款)
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
