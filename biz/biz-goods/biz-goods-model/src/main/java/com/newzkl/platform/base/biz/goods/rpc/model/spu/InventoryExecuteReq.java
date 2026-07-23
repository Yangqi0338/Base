package com.newzkl.platform.base.biz.goods.rpc.model.spu;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/1/510:30
 */
@Data
public class InventoryExecuteReq implements Serializable {
    /**
     * 类型
     */
    private Integer type;
    /**
     * sku数据
     */
    private List<Sku> skuList;

    @Data
    public static class Sku implements Serializable {
        private Long id;
        private Integer count;
    }
}
