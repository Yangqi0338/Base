package com.newzkl.platform.base.biz.order.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 库存变更入参
 *
 * @author KC
 */
@Data
public class InventoryExecuteCommand implements Serializable {

    /**
     * 变更类型
     *
     * @ext 0 扣减, 1 回补
     */
    private Integer type;

    /**
     * 变更明细
     */
    private List<Sku> skuList = new ArrayList<>();

    /**
     * 库存变更明细项
     *
     * @author KC
     */
    @Data
    public static class Sku implements Serializable {

        /**
         * SKU ID
         */
        private Long id;

        /**
         * 变更数量
         */
        private Integer count;
    }
}
