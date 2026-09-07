package com.newzkl.platform.base.biz.goods.model.goods.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * SKU 变更事件
 *
 * <p>SKU 变更 / 删除 / 改价三个 tag 共用本负载 事件类型由 tag 区分
 * 见 GoodsMessageApi#publishSkuEdit / publishSkuDelete / publishSkuPrice</p>
 *
 * @author muc_fang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkuEditEvent implements Serializable {
    /**
     * 商品ID
     */
    private Long spuId;
    /**
     * SKU_ID集合
     */
    private List<Long> skuIdList;
}
