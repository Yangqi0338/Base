package com.newzkl.platform.base.biz.order.model.support.api.spu;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author muc_fang
 * @Description: spu销售属性
 * @date 2023/8/217:12
 */
@Data
public class SkuSaleAttributeRpcVO implements Serializable {
    /**
     * 属性名称
     */
    @NotEmpty
    private String name;
    /**
     * 属性值
     */
    @NotEmpty
    private String value;
}
