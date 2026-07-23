package com.newzkl.platform.base.biz.order.model.order.req;

import com.newzkl.platform.base.biz.order.model.support.api.DistributionDetailVO;
import com.newzkl.platform.base.biz.order.model.enums.goods.SpuEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 运费计算请求VO
 * @author sijiwang
 */
@Data
public class FreightCalculateReq {
    /** 商品ID（SPU） */
    private Long goodsId;
    /** 购买数量 */
    private Integer num;
    /** 总重量 */
    private BigDecimal totalWeight;
    /** 总体积 */
    private BigDecimal totalVolume;
    /** 外部SPU ID */
    private String outSpuId;
    /** 外部SKU ID */
    private String outSkuId;
    /** 渠道类型 */
    private SpuEnum.ChannelType channelType;
    /** 商品编码 */
    private String itemCode;
    /** 供应商ID */
    private Long supplierId;
    /** 运费模板ID */
    private Long freightTemplateId;
    /** 收货地址编码（省/市/区） */
    private String shipProvinceCode;
    private String shipCityCode;
    private String shipAreaCode;

    /**
     * 收货省份名称
     */
    private String shipProvinceName;

    /**
     * 收货城市名称
     */
    private String shipCityName;

    /**
     * 收货区县名称
     */
    private String shipAreaName;

    /**
     * 详细收货地址（如：创业路东XX小区XX号楼）
     */
    private String shipDetailAddress;

    /** 铺货详情 */
    private DistributionDetailVO distributionDetail;
}