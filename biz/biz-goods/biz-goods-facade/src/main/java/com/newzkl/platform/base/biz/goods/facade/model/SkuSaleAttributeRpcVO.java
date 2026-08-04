package com.newzkl.platform.base.biz.goods.facade.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;

/**
 * spu 销售属性 (facade 自带 model)
 *
 * <p>facade 模块物理禁引 biz-goods-model (硬线2 防腐), 故在 facade.model 内自带一份.
 * 字段与 biz-goods-model 的 rpc.model.spu.SkuSaleAttributeRpcVO 逐字对齐</p>
 *
 * @author muc_fang
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
