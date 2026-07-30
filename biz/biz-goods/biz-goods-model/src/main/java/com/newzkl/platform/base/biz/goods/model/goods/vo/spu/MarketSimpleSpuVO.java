package com.newzkl.platform.base.biz.goods.model.goods.vo.spu;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 市场全量商品简化视图对象
 *
 * <p>字段与 new-scm {@code MarketSimpleSpuVO} 逐一对齐: 继承 {@code SpuSimpleVO} 的
 * code / name / img, 另附是否已铺货标识。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MarketSimpleSpuVO extends SpuSimpleVO {

    /**
     * 是否已铺货
     */
    private Boolean choose = false;
}
