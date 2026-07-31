package com.newzkl.platform.base.biz.order.model.vo;


import com.newzkl.platform.base.biz.order.model.support.api.order.OrderSkuVO;
import lombok.Data;

import java.util.List;

/**
 * @author muc_fang
 * @Description: 订单其他快照记录
 * @date 2024/1/2615:02
 */
@Data
public class OrderSnapVO {
    private Long orderId;
    /**
     * 平台服务费
     */
    private Integer platformServiceChange;
    /**
     * 运营商服务费
     */
    private Integer operatorServiceChange;

    /**
     * 本地商品
     */
    private List<OrderSkuVO> localGoods;

    /**
     * 外部商品
     */
    private List<OrderSkuVO> outGoods;
}
