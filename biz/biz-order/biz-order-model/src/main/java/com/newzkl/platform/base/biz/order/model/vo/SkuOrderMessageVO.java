package com.newzkl.platform.base.biz.order.model.vo;

import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * SKU 订单分润消息载体
 *
 * <p>迁移: 原 new-scm scm-message-rpc message.rpc.model.sale.SkuOrderMessageVO,
 * 支付成功后组装分润消息用. adopt-chicken: orderState 用枚举, 与 SkuOrder getter 对齐
 *
 * @author muc_fang
 */
@Data
public class SkuOrderMessageVO implements Serializable {

    /**
     * SKU订单ID
     */
    private Long id;

    /**
     * 渠道类型 0 供货商品 1 自营商品 2 外部商品
     */
    private Integer spuChannelType;

    /**
     * 商品金额
     */
    private Integer goodsAmount;

    /**
     * 铺货价格
     */
    private Integer storeAmount;

    /**
     * 货款金额
     */
    private Integer supplierAmount;

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
     * 运营商ID
     */
    private Long operatorId;

    /**
     * SPU_ID
     */
    private Long spuId;

    /**
     * skuID
     */
    private Long skuId;

    /**
     * 购买数量
     */
    private Integer count;

    /**
     * spu名称
     */
    private String spuName;

    /**
     * sku图片
     */
    private String skuImg;

    /**
     * sku销售属性
     */
    private String skuSaleAttribute;

    /**
     * sku名称
     */
    private String skuName;

    /**
     * 渠道商ID
     */
    private Long channelId;

    /**
     * 总服务费
     */
    private Integer totalServiceChange;

    /**
     * 运营商服务费
     */
    private Integer operatorServiceChange;

    /**
     * 运营商实际服务比例
     */
    private Double operatorRealRatio;
}
