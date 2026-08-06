package com.newzkl.platform.base.biz.order.model.vo;

import com.newzkl.platform.base.common.core.model.money.Money;


import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.SettleType;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.vo.BaseVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * SKU订单
 * @author fang
 */
@Data
public class SkuOrderVO extends BaseVO {
     /**
     * 订单ID
     */
     private Long id;
     /**
      * SPU订单ID
      */
     private Long spuOrderId;
     /**
      * 订单ID
      */
     private Long orderId;
     /**
     * 商品金额
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
      * 货款金额
      */
     private Money supplierAmount;
     /**
     * 订单状态 (0, "新订单"),(1,"C端待付款"),(2,"渠道商待付款"),(3,"运营商待付款"),(4, "派发中"),(6,"待发货"),(8,"待收货"),(10,"已收货"),(12,"已完成"),(99,"已关闭"),
     */
     private OrderEnum.State orderState;
     /**
      * spuId
      */
     private Long spuId;
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
      * 结算配置
      */
     private String settlementConfigVO;
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
      * sku供货价
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
     private SettleType settleOrderType;
     /**
      * 总服务费
      */
     private Money totalServiceChange;
     /**
      * 运营商服务费
      */
     private Integer operatorServiceChange;
     /**
      * 运营商实际服务比例
      */
     private Double operatorRealRatio;
     /**
      * 结算发送状态
      */
     private CommonEnum.YesOrNo settleSendState;
    /**
     * 物流信息
     */
     private List<DeliverVO> deliverVOList;
}