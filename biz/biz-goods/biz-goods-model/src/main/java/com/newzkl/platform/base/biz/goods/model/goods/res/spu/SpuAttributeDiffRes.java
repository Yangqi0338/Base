package com.newzkl.platform.base.biz.goods.model.goods.res.spu;

import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/10/1716:53
 */
@Data
public class SpuAttributeDiffRes {
    /**
     * 根据spu销售属性变更, 选择修改策略
     * 1 spu销售属性未修改
     *      增量修改SKU
     * 2 spu销售属性值变更
     *      增量修改已有SKU, 新增或删除SKU
     * 3 spu销售属性变更
     *      删除全部SKU并重新新增
     */
    Integer updateType = null;
    Map<String, Set<String>> addSpuSaleAttributeValue;
    Map<String, Set<String>> deleteSpuSaleAttributeValue;
}
