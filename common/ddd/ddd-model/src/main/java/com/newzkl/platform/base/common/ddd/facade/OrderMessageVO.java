package com.newzkl.platform.base.common.ddd.facade;

import com.newzkl.platform.base.common.core.model.money.Money;

import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 交易单分润消息载体(替 SpuOrderMessageVO, SpuOrder 层折叠后, 语义 order 级)
 *
 * <p>支付成功后组装分润消息用. orderState 用枚举
 *
 * @author muc_fang
 */
@Data
public class OrderMessageVO implements Serializable {

    /**
     * 交易单ID
     */
    private Long id;

    /**
     * 订单状态
     */
    private OrderEnum.State orderState;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 交易师ID
     */
    private Long dealerId;

    /**
     * 渠道商ID
     */
    private Long channelId;

    /**
     * 运营商ID
     */
    private Long operatorId;

    /**
     * SPU_ID
     */
    private Long spuId;

    /**
     * 货款金额
     */
    private Money supplierAmount;

    /**
     * 选品金额
     */
    private Money goodsAmount;

    /**
     * 铺货金额
     */
    private Money storeAmount;

    /**
     * 运费金额
     */
    private Money freightAmount;

    /**
     * 优惠金额
     */
    private Money discountAmount;

    /**
     * SKU总数
     */
    private Integer skuCount;
}
