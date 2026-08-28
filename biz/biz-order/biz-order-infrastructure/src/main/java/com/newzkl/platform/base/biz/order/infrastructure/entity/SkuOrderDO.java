package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.money.Money;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.core.mybatis.handler.RawJsonStringTypeHandler;
import com.newzkl.platform.base.common.ddd.facade.SettlementConfigVO;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
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
     * 交易单ID
     */
    @Index
    private Long orderId;
    /**
     * SPU订单ID
     */
    @Index
    private Long spuOrderId;
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
     * 交易师ID
     */
    @Index
    private Long dealerId;
    /**
     * 运营商ID
     */
    @Index
    private Long operatorId;
    /**
     * 供应商ID
     */
    @Index
    private Long supplierId;
    /**
     * 结算配置
     * @ext 源列 settlement_config_v_o, 多来源 JSON 故用 String
     */
    @JsonSerializable
    private SettlementConfigVO settlementConfigVO;
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
    @JsonSerializable(typeHandler = RawJsonStringTypeHandler.class)
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
     * 订单状态流转日志
     * @ext 逗号隔开
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
    private EarningsEnum.SettleType settleOrderType;
    /**
     * 结算发送状态
     */
    private CommonEnum.YesOrNo settleSendState;
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
