package com.newzkl.platform.base.biz.order.model.support.api.order;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: 订单商品信息
 * @Author: niu
 * @Date: 2022/4/22 17:06
 */

@Data
public class OrderGoodsInfoVO implements Serializable {
    /** 数量 */
    private Integer num;
    /**
     * 二级市场ID
     */
    private Long twoMarketId;
    /**
     * 供应商ID
     */
    private Long supplierId;
    /**
     * ID
     */
    private Long skuId;
    /**
     * 图片
     */
    private String img;
    /**
     * 补偿字段
     */
    private String saleAttributeJson;
    /**
     * 重量(千克)
     */
    private BigDecimal weight;
    /**
     * 体积(m3)
     */
    private BigDecimal volume;
    /**
     * spuId (查询)
     */
    private Long spuId;
    /**
     * 供货价
     */
    private Money supplyPrice;
    /**
     * 销售价
     */
    private Money salePrice;
    /**
     * 铺货价
     */
    private Money storePrice;
    /**
     * 运费模板id
     */
    private Long freightTemplateId;
    /**
     * 状态 0:仓库中 2:上架中 3:待上架 (查询)
     */
    private Integer spuState;
    /**
     * spu图片
     */
    private String spuImg;
    /**
     * spu名称
     */
    private String spuName;
    /**
     * spu销售类型 0 实物
     */
    private Integer spuSaleType;
    /**
     * 渠道类型 0 供货商品 1 自营商品 2:外部商品
     */
    private SpuEnum.ChannelType spuChannelType;

    /**
     * 运费
     */
    private Integer freight;

    /** 外部SPU ID */
    private String outSpuId;

    /** 外部SKU ID */
    private String outSkuId;
    /**
     * 起购数量
     */
    private Integer buyStartQty;

    /**
     * 扩展字段
     */
    private String expand;
}
