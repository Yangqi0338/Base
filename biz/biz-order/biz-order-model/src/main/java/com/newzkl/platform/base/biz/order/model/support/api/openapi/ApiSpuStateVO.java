package com.newzkl.platform.base.biz.order.model.support.api.openapi;

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
public class ApiSpuStateVO implements Serializable {
    /**
     * spuId
     */
    @NotNull
    private Long spuId;
    /**
     * 售卖状态 0 下架 1 上架
     */
    @NotNull
    private Integer saleState;
}
