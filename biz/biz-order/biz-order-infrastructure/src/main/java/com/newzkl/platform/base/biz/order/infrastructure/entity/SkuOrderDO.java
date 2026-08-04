package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.newzkl.platform.base.common.core.model.dto.Money;

import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author muc_fang
 * @Description: 订单
 * @date 2023/11/1014:50
 */
@Data
public class SkuOrderDO extends BaseDO {

    private Long orderId;
    private Long spuOrderId;
    private Money goodsAmount;
    /**
     * 铺货金额
     */
    private Money storeAmount;
    private Money freightAmount;
    private Money discountAmount;
    private Money totalAmount;
    /**
     * 货款金额 supplier_amount
     */
    private Money supplierAmount;
    private Integer orderState;
    /**
     * 交易师ID dealer_id
     */
    private Long dealerId;
    /**
     * 运营商ID operator_id
     */
    private Long operatorId;
    /**
     * 供应商ID
     */
    private Long supplierId;
    /**
     * 结算配置 settlement_config_v_o
     */
    private String settlementConfigVO;
    /**
     * skuID
     */
    private Long spuId;
    /**
     * skuID
     */
    private Long skuId;
    /**
     * 外部SkuId
     */
    private String outSkuId;
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
     * sku重量(千克)
     */
    private Double skuWeight;
    /**
     * sku体积(m3)
     */
    private Double skuVolume;
    /**
     * sku供货价 sku_supplier_price
     */
    private Money skuSupplierPrice;
    /**
     * sku采购价
     */
    private Money skuSalePrice;
    /**
     * sku铺货价
     */
    private Money skuStorePrice;
    /**
     * 发货数量
     */
    private Integer deliverCount;
    /**
     * 售后中数量
     */
    private Integer refundingCount;
    /**
     * 已售后数量
     */
    private Integer refundedCount;
    /**
     * 订单状态流转日志,逗号隔开
     */
    private String orderStateLog;
    /**
     * 发货完成时间
     */
    private LocalDateTime deliveredTime;
    /**
     * 确认收货时间
     */
    private LocalDateTime receiveTime;
    /**
     * 结算节点
     */
    private Integer settleOrderType;
    /**
     * 结算发送状态
     */
    private Integer settleSendState;
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
