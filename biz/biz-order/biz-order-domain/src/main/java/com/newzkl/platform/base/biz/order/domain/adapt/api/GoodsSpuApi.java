package com.newzkl.platform.base.biz.order.domain.adapt.api;

/**
 * 商品 SPU 出站端口
 *
 * @author KC
 */
public interface GoodsSpuApi {

    /**
     * 执行库存变更
     *
     * @param command 库存变更入参
     */
    void inventoryExecute(InventoryExecuteCommand command);
}
