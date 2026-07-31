package com.newzkl.platform.base.biz.order.facade.model.common;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author muc_fang
 * @Description: spu销售属性
 * @date 2023/8/217:12
 */
@Data
public class SkuSaleAttributeApiVO implements Serializable {
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
