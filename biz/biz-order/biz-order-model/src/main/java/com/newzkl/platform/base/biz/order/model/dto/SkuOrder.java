package com.newzkl.platform.base.biz.order.model.dto;

import cn.hutool.core.util.NumberUtil;
import com.newzkl.platform.base.biz.order.model.req.OrderItemCommand;
import com.newzkl.platform.base.biz.order.model.support.api.ChannelNowServiceFeeRes;
import com.newzkl.platform.base.biz.order.model.support.api.EarningsConfigRpcVO;
import com.newzkl.platform.base.biz.order.model.support.api.SkuSaleInfo;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;

import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author muc_fang
 * @Description: sku订单
 * @date 2023/11/289:38
 */
@Data
public class SkuOrder implements Serializable {

    /**
     * 订单ID
     */
    private Long id;
    /**
     * 订单ID
     */
    private Long orderId;
    /**
     * SPU订单ID
     */
    private Long spuOrderId;

    /**
     * 门店id 传参用
     */
    private Long storeId;
    /**
     * 货款金额
     */
    private Integer supplierAmount;
    /**
     * 商品金额
     */
    private Integer goodsAmount;
    /**
     * 铺货金额
     */
    private Integer storeAmount;
    /**
     * 运费金额
     */
    private Integer freightAmount;
    /**
     * 优惠金额
     */
    private Integer discountAmount;
    /**
     * 订单状态 (0, "新订单"),(1,"C端待付款"),(2,"渠道商待付款"),(3,"运营商待付款"),(4, "派发中"),(6,"待发货"),(8,"待收货"),(10,"已收货"),(12,"已完成"),(99,"已关闭"),
     */
    private OrderEnum.State orderState;
    /**
     * 发货时间
     */
    private LocalDateTime deliverTime;
    /**
     * 二级市场ID
     */
    private Long twoMarketId;
    /**
     * 供应商ID
     */
    private Long supplierId;
    /**
     * 结算配置
     */
    private String settlementConfigVO;
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
     * sku供货价
     */
    private Integer skuSupplierPrice;
    /**
     * sku采购价
     */
    private Integer skuSalePrice;
    /**
     * sku铺货价
     */
    private Integer skuStorePrice;
    /**
     * sku重量(千克)
     */
    private Double skuWeight;
    /**
     * sku体积(m3)
     */
    private Double skuVolume;
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
     * 总服务费 : 渠道商应付
     */
    private Integer totalServiceChange;
    /**
     * 运营商服务费: 渠道商应付的部分
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
     * 渠道类型 0 供货商品 1 自营商品
     */
    private SpuEnum.ChannelType spuChannelType;

    /**
     * spu销售类型 0 实物
     */
    private Integer spuSaleType;

    /**
     * spu图片
     */
    private String spuImg;

    private LocalDateTime createTime;

    public static Integer percent(Integer value, Double ratio){
        return NumberUtil.div(NumberUtil.mul(ratio, value), BIG_100).intValue();
    }

    /**
     * 初始化
     * @param orderItemCommand
     * @param orderId
     * @param spuOrderId Spu订单ID
     * @param freightAmount 运费金额
     * @param channelVO
     */
    public void init(OrderItemCommand orderItemCommand, Long orderId, Long spuOrderId, SkuSaleInfo skuVO,
                     Integer freightAmount, EarningsConfigRpcVO channelVO, ChannelNowServiceFeeRes channelNowServiceFee) {
        this.id = SnowflakeIdAble.getSnowflakeId();
        this.orderId = orderId;
        this.spuId = skuVO.getSpuId();
        this.skuImg = skuVO.getImg();
        this.spuOrderId = spuOrderId;
        //计算订单金额
        this.supplierAmount = 0;
        this.goodsAmount = 0;
        this.storeAmount = 0;
        if(SpuEnum.ChannelType.CUSTOM == skuVO.getSpuChannelType()){
            this.storeAmount = skuVO.getStorePrice() * orderItemCommand.getCount();
        }else if(SpuEnum.ChannelType.SELECTION == skuVO.getSpuChannelType()){
            this.supplierAmount = skuVO.getSupplyPrice() * orderItemCommand.getCount();
            this.goodsAmount = skuVO.getSalePrice() * orderItemCommand.getCount();
            Integer storePrice = skuVO.getStorePrice() == null ? 0 : skuVO.getStorePrice();
            this.storeAmount = storePrice * orderItemCommand.getCount();
        }else {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        this.freightAmount = 0;
        this.discountAmount = 0;

        buildServiceChange(channelNowServiceFee);

        this.orderState = OrderEnum.State.NEW;
        this.orderStateLog = OrderEnum.State.NEW.getCode().toString();
        this.supplierId = skuVO.getSupplierId();
        if(channelVO.getUpDealerId() != null){
            this.dealerId = channelVO.getUpDealerId();
        }
        if(channelVO.getUpOperatorId() != null){
            this.operatorId = channelVO.getUpOperatorId();
        }
        this.twoMarketId = skuVO.getTwoMarketId();
        this.skuId = orderItemCommand.getSkuId();
        this.count = orderItemCommand.getCount();
        this.spuName = skuVO.getSpuName();
        this.skuImg = skuVO.getImg();
        this.skuSaleAttribute = skuVO.getSaleAttributeJson();
        this.spuName = skuVO.getSpuName();
        this.skuWeight = skuVO.getWeight();
        this.skuVolume = skuVO.getVolume();
        this.skuSalePrice = skuVO.getSalePrice();
        this.skuSupplierPrice = skuVO.getSupplyPrice();
        this.skuStorePrice = skuVO.getStorePrice() == null ? 0 : skuVO.getStorePrice();
        this.deliverCount = 0;
        this.refundingCount = 0;
        this.refundedCount = 0;
    }

    private static final BigDecimal BIG_100 = new BigDecimal(100);

    /**
     * 根据服务费配置构建订单服务费
     */
    public void buildServiceChange(ChannelNowServiceFeeRes channelNowServiceFee) {
        Double operatorNowValue = channelNowServiceFee.getOperatorNowValue();
        Double platformNowValue = channelNowServiceFee.getPlatformNowValue();

        // 运营商服务费
        int operatorServiceChange = percent(this.goodsAmount, channelNowServiceFee.getOperatorNowValue());
        // 平台服务费
        int platformServiceChange = percent(this.goodsAmount, channelNowServiceFee.getPlatformNowValue());

        // 总服务费 (若有运营商服务费,则等于运营商服务费)
        this.totalServiceChange = operatorServiceChange > 0 ? operatorServiceChange : platformServiceChange;

        // 运营商服务费(总服务费-平台服务费)
        this.operatorServiceChange = Math.max(this.totalServiceChange - platformServiceChange, 0);

        // 运营商真实服务费比例
        this.operatorRealRatio = NumberUtil.sub(operatorNowValue, platformNowValue);
    }
}
