package com.newzkl.platform.base.biz.order.facade.model.order;

import lombok.Data;

/**
 * @author muc_fang
 * @Description: sku订单
 * @date 2024/1/2517:18
 */
@Data
public class SkuOrderRpcVO {
    /**
     * SKU订单ID
     */
    private Long id;
    /**
     * 商品金额
     */
    private Integer goodsAmount;
    /**
     * 货款金额
     */
    private Integer supplierAmount;
    /**
     * 订单状态 (0, "新订单"),(2,"待付款"),(4, "派发中"),(6,"待发货"),(8,"待收货"),(10,"已收货"),(12,"已完成"),(99,"已关闭")
     */
    private Integer orderState;
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
}
