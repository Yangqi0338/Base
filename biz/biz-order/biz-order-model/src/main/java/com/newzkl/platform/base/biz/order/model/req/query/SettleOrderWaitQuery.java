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
}
