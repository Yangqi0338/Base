package com.newzkl.platform.base.biz.goods.rpc.model.spu;

import lombok.Data;

import java.io.Serializable;

/**
 * 商品销售数量
 * @author sijiwang
 */
@Data
public class GoodsSellNumVO implements Serializable {

    /** store_distribution的主键ID */
    private Long id;
    /** 总销量 = sd.sell_num + spu.virtual_sale_num */
    private Integer num;
}
