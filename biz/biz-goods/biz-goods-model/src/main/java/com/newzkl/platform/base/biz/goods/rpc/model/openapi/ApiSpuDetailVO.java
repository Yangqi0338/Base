package com.newzkl.platform.base.biz.goods.rpc.model.openapi;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * SPU详情
 *
 * @author wqm
 * @since 2023年04月27日 14:58:00
 */
@Data
public class ApiSpuDetailVO implements Serializable {
    /**
     * SPU信息
     */
    @NotNull
    private ApiSpuVO spu;
    /**
     * SKU列表
     */
    @NotNull
    private List<ApiSkuVO> skuList;
}
