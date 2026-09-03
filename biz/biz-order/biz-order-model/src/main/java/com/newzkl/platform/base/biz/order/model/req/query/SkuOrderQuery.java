package com.newzkl.platform.base.biz.order.model.req.query;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
* SKU订单
* @author fang
*/
@Data
public class SkuOrderQuery extends BizPageQuery {

    /**
     * SKU_ID集合
     */
    private List<Long> skuIdList;
    public void setSkuId(Long skuId) {
        this.skuIdList = doWrapperList(skuIdList,skuId);
    }
    /**
     * 小于发货时间
     */
    private LocalDateTime lessDeliverTime;
    /**
     * 小于收货时间
     */
    private LocalDateTime lessReceiveTime;
    /**
     * 交易单号集合
     */
    private List<String> skuOrderNoList;

    public void setSkuOrderNo(String skuOrderNo) {
        this.skuOrderNoList = doWrapperList(skuOrderNoList, skuOrderNo);
    }
    /**
     * 交易单号集合
     */
    private List<String> orderNoList;

    public void setOrderNo(String orderNo) {
        this.orderNoList = doWrapperList(orderNoList, orderNo);
    }
    /**
     * SPU_ID集合
     */
    private List<Long> spuIdList;

    public void setSpuId(Long spuId) {
        this.spuIdList = doWrapperList(spuIdList, spuId);
    }

    /**
     * 售后中数量
     */
    private Integer refundingCount;
    /**
     * 结算发送状态
     */
    private CommonEnum.YesOrNo settleSendState;
    /**
     * 订单状态集合
     */
    private List<OrderEnum.State> orderStateList;

    public void setOrderState(OrderEnum.State orderState) {
        this.orderStateList = doWrapperList(orderStateList, orderState);
    }
}
