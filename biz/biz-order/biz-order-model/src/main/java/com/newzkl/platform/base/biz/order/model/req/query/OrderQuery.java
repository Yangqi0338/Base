package com.newzkl.platform.base.biz.order.model.req.query;

import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;

import java.util.List;

/**
* 订单
* @author fang
*/
@Data
public class OrderQuery extends BizPageQuery {
    /**
    * 渠道类型 (0:API) 查询
    */
    private Integer channelType;
    /**
    * 订单类型 (0:渠道商订单 1:c端订单) 查询
    */
    private OrderEnum.OrderType orderType;
    /**
    * 商品类型 (0:实物 1:课程 2:服务) 查询
    */
    private Integer goodsType;
    /**
     * 运营商ID
     */
    private Long operatorId;
    /**
    * 渠道商ID 查询
    */
    private Long channelId;
    /**
     * 商户ID
     */
    private Long merchantId;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 昵称
     */
    private Long nickname;
    /**
     * 外部订单号集合
     */
    private List<String> outOrderNoList;

    public void setOutOrderNo(String outOrderNo) {
        this.outOrderNoList = doWrapperList(this.outOrderNoList, outOrderNo);
    }

    /**
     * 订单状态集合
     */
    private List<OrderEnum.State> orderStateList;

    public void setOrderState(OrderEnum.State orderState) {
        this.orderStateList = doWrapperList(this.orderStateList, orderState);;
    }
}
