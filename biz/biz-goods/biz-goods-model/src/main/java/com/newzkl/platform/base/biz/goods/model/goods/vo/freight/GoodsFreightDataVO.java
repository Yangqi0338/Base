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

    private Integer num;

    private BigDecimal weight;

    private BigDecimal volume;

    private String outSpuId;

    private String outSkuId;

    private String channelType;

    private String outItemCode;

    /**
     * 供应商ID
     */
    private Long supplierId;
}
