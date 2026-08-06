package com.newzkl.platform.base.biz.order.model.vo;

import com.newzkl.platform.base.common.core.model.money.Money;

import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * SPU 订单分润消息载体
 *
 * <p>迁移: 原 new-scm scm-message-rpc message.rpc.model.sale.SpuOrderMessageVO,
 * 支付成功后组装分润消息用. adopt-chicken: orderState 用枚举, 与 SpuOrder getter 对齐
 *
 * @author muc_fang
 */
@Data
public class SpuOrderMessageVO implements Serializable {

    /**
     * SPU订单ID
     */
    private Long id;

    /**
     * 订单状态
     */
    private OrderEnum.State orderState;

    /**
     * 二级市场ID
     */
    private Long twoMarketId;

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
