package com.newzkl.platform.base.biz.order.model.req.query;

import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
* 售后单
* @author fang
*/
@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefundQuery extends BizPageQuery {
    /**
     * SPU订单号
     */
    private Long spuOrderId;
    /**
     * 渠道商ID
     */
    private Long channelId;
    /**
     * 门店ID
     */
    private Long storeId;
    /**
     * 供应商ID
     */
    private Long supplierId;
    /**
     * (0,"待渠道商审核"),(2,"待供应商审核"),(4,"待提交物流"),(6,"待确认收货"),(7,"待平台介入"),(8,"平台介入中"),(9,"退款中"),(10,"已完成"),(-2,"已拒绝"),(-4,"已关闭"),
     */
    private List<RefundEnum.State> refundStateList;
    public void setRefundState(RefundEnum.State refundState){
        this.refundStateList = doWrapperList(refundStateList, refundState);
    }
    /**
     * 售后类型 (0 仅退款 1 退货退款)
     */
     private RefundEnum.RefundType refundType;
    /**
     * 不可见来源订单状态
     */
    private List<OrderEnum.State> fromOrderStateNot;
    /**
     * 订单类型 (0:渠道商订单 1:c端订单) 查询
     */
    private OrderEnum.OrderType orderType;
    /**
     * C端ID
     */
    private Long memberId;
    /**
     * 状态变化时间小于
     */
    private LocalDateTime stateTimeLess;
    /**
     * 用户名
     */
    private String username;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 商品名称
     */
    private String spuName;

}
