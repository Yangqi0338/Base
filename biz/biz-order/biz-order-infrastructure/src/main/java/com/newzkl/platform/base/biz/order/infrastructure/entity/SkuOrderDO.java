package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.money.Money;

import com.newzkl.platform.base.biz.order.model.vo.OrderSkuInfo;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.core.mybatis.handler.RawJsonStringTypeHandler;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
import org.dromara.autotable.annotation.OldColumnName;
import org.dromara.mpe.autofill.annotation.JsonSerializable;

import java.time.LocalDateTime;

/**
 * @author muc_fang
 * @Description: 订单
 * @date 2023/11/1014:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class SkuOrderDO extends BaseDO {

    /**
     * 交易单号
     */
    @Index
    @OldColumnName("order_id")
    private String orderNo;
    /**
     * SKU订单号
     */
    @Index
    private String skuOrderNo;
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
     * 总金额
     */
    private Money totalAmount;
    /**
     * 货款金额
     */
    private Money supplierAmount;
    /**
     * 订单状态
     */
    @Index
    private OrderEnum.State orderState;
    /**
     * 供应商ID
     */
    @Index
    private Long supplierId;
    /**
     * spuID
     */
    @Index
    private Long spuId;
    /**
     * skuID
     */
    @Index
    private Long skuId;
    /**
     * 外部SkuId
     */
    private String outSkuId;
    /**
     * 外部SpuId
     */
    private String outSpuId;
    /**
     * SPU渠道类型
     */
    @Index
    private SpuEnum.ChannelType spuChannelType;
    /**
     * 内嵌SPU/SKU信息(下沉自SpuOrder)
     */
    @JsonSerializable
    private OrderSkuInfo orderSkuInfo;
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
    @JsonSerializable(typeHandler = RawJsonStringTypeHandler.class)
    private String skuSaleAttribute;
    /**
     * sku供货价
     */
    @OldColumnName("sku_supplier_price")
    private Money supplierPrice;
    /**
     * sku采购价
     */
    @OldColumnName("sku_sale_price")
    private Money salePrice;
    /**
     * sku铺货价
     */
    @OldColumnName("sku_store_price")
    private Money storePrice;
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
     * 发货完成时间
     */
    private LocalDateTime deliveredTime;
    /**
     * 确认收货时间
     */
    private LocalDateTime receiveTime;
    /**
     * 订单状态流转日志
     * @ext 逗号隔开
     */
    private String orderStateLog;
    /**
     * 结算节点
     */
    private EarningsEnum.SettleType settleOrderType;
    /**
     * 结算发送状态
     */
    private CommonEnum.YesOrNo settleSendState;
    /**
     * 总服务费
     */
    private Money totalServiceChange;
}
