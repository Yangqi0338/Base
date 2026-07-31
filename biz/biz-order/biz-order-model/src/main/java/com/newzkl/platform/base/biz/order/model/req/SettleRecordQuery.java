package com.newzkl.platform.base.biz.order.model.req;


import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;

import java.util.List;

/**
* 结算记录表
* @author fang
*/
@Data
public class SettleRecordQuery extends PageQuery {

    /**
     * ID
     */
    private Long id;
    /**
     * ID集合
     */
    private List<Long> idList;
    /**
     * 供应商ID
     */
    private Long supplierId;
}
