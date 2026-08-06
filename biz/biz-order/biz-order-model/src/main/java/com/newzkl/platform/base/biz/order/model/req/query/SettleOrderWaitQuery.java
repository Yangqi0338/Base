package com.newzkl.platform.base.biz.order.model.req.query;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;

import java.util.List;

/**
* 待结算订单信息表
* @author fang
*/
@Data
public class SettleOrderWaitQuery extends BizPageQuery {

    /**
     * SPU ID集合
     */
    private List<Long> spuIdList;
    /**
     * 供应商ID
     */
    private Long supplierId;
    /**
     * 结算状态
     */
    private Integer settleState;
    /**
     * sku订单ID(单值, 供 getLw 精确匹配)
     */
    private Long skuOrderId;
    /**
     * 类型 0商品 1运费 2售后冲正(单值, 供 getLw 精确匹配)
     */
    private Integer type;
    /**
     * spu订单ID(单值, 供 getLw 精确匹配)
     */
    private Long spuOrderId;
}
