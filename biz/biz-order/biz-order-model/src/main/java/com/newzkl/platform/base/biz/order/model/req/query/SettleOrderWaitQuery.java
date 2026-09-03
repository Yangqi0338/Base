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
     * @ext 无对应枚举, 保留 Integer
     */
    private Integer settleState;
    /**
     * sku订单ID
     * @ext 单值, 供 getLw 精确匹配
     */
    private Long skuOrderId;
    /**
     * 结算类型
     * @ext 0 商品 1 运费 2 售后冲正; 单值供 getLw 精确匹配; 无对应枚举, 保留 Integer (参见 deferred D-55)
     */
    private Integer type;
    /**
     * 交易单号
     * @ext 单值, 供 getLw 精确匹配
     */
    private String orderNo;
    private String skuOrderNo;
}
