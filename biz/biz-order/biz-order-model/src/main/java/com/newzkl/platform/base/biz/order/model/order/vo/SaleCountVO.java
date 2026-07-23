package com.newzkl.platform.base.biz.order.model.order.vo;

import lombok.Data;
import java.io.Serializable;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/4/2417:06
 */
@Data
public class SaleCountVO implements Serializable {
    /**
     * 待付款
     */
    private Integer waitPayNumber;
    /**
     * 待发货
     */
    private Integer waitDeliveryNumber;
    /**
     * 售后单
     */
    private Integer refundPage;
}
