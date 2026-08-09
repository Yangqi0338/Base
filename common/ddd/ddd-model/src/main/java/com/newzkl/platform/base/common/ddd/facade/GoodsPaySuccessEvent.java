package com.newzkl.platform.base.common.ddd.facade;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/12/1919:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsPaySuccessEvent implements Serializable {
    /**
     * 订单ID
     */
    private Long orderId;
    /**
     * SKU订单分润信息
     */
    private List<SkuOrderMessageVO> skuOrderList;
    /**
     * SPU订单支付成功信息
     */
    private List<SpuOrderMessageVO> spuOrderList;
}
