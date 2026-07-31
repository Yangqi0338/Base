package com.newzkl.platform.base.biz.goods.model.goods.vo.freight;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author niu
 * @description:
 * @date 2024/5/7 16:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsFreightDataVO {

    /** 数量 */
    private Integer num;

    /** 重量 */
    private BigDecimal weight;

    /** 体积 */
    private BigDecimal volume;

    /** 外部SPU ID */
    private String outSpuId;

    /** 外部SKU ID */
    private String outSkuId;

    /** 渠道类型 */
    private String channelType;

    /** 外部商品编码 */
    private String outItemCode;

    /**
     * 供应商ID
     */
    private Long supplierId;
}
