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
     * 交易单号
     */
    private String orderNo;
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
     * 售后状态集合
     */
    private List<RefundEnum.State> refundStateList;
    public void setRefundState(RefundEnum.State refundState){
        this.refundStateList = doWrapperList(refundStateList, refundState);
    }
    /**
     * 售后类型
     */
     private RefundEnum.RefundType refundType;
    /**
     * 不可见来源订单状态
     */
    private List<OrderEnum.State> fromOrderStateNot;
    /**
     * 订单类型查询
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
    /**
     * 商品id
     */
    private Long spuId;

}
