package com.newzkl.platform.base.biz.order.model.req;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;

import java.util.List;

/**
* 发货单
* @author fang
*/
@Data
public class DeliverQuery extends PageQuery {

    /**
     * ID
     */
    private Long id;
    /**
     * ID集合
     */
    private List<Long> idList;
    /**
     * SPU订单ID
     */
    private Long spuOrderId;
    /**
     * SPU订单ID集合
     */
    private List<Long> spuOrderIdList;
}
