package com.newzkl.platform.base.biz.order.model.req;

import lombok.Data;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/12/714:01
 */
@Data
public class DeliverItemCommand {
    /**
     * SKU_ID
     */
    private Long skuId;
    /**
     * 发货数量
     */
    private Integer count;
}
