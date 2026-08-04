package com.newzkl.platform.base.biz.order.model.req.query;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;

import java.util.List;

/**
* 售后单明细
* @author fang
*/
@Data
public class RefundItemQuery extends PageQuery {

    private Long refundId;
}
