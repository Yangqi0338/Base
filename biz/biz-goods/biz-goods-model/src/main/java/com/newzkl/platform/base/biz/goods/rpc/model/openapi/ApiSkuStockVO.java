package com.newzkl.platform.base.biz.goods.rpc.model.openapi;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品spu信息
 *
 * @author wqm
 * @since 2023年04月27日 14:58:00
 */
@Data
public class ApiSkuStockVO implements Serializable {
    /**
     * SKU_ID
     */
    @NotNull
    private Long id;
    /**
     * 库存
     */
    @NotNull
    private Integer inventory;
}
