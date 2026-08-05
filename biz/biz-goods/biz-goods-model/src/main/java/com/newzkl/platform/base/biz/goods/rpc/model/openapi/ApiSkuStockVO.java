package com.newzkl.platform.base.biz.goods.rpc.model.openapi;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * SKU 库存信息
 *
 * @author KC
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
