package com.newzkl.platform.base.biz.finance.model.event;

import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

/**
 * @author muc_fang
 * @Description: sku订单
 * @date 2024/1/2517:18
 */
@Data
public class SkuOrderEarningsEvent {
    /**
     * SKU订单ID
     */
    private Long id;
    /**
     * 商品金额
     */
    private Money goodsAmount;
    /**
     * 铺货价格
     */
    private Money storeAmount;
    /**
     * 货款金额
     */
    private Money supplierAmount;
    /**
     * 渠道商支付金额
     */
    private Money totalAmount;
    /**
     * 服务费
     */
    private Money serviceAmount;
    /**
     * 订单状态 (0, "新订单"),(2,"待付款"),(4, "派发中"),(6,"待发货"),(8,"待收货"),(10,"已收货"),(12,"已完成"),(99,"已关闭")
     */
    private Integer orderState;
    /**
     * 供应商ID
     */
    private Long supplierId;
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
    private Money totalServiceChange;
    /**
     * 运营商服务费
     */
    private Money operatorServiceChange;
    /**
     * 运营商实际服务比例
     */
    private Double operatorRealRatio;


}
