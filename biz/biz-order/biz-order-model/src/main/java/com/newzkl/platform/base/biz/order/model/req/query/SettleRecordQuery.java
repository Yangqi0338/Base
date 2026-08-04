package com.newzkl.platform.base.biz.order.model.req.query;


import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;

import java.util.List;

/**
* 结算记录表
* @author fang
*/
@Data
public class SettleRecordQuery extends BizPageQuery {
    /**
     * 供应商ID
     */
    private Long supplierId;
}
