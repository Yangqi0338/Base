package com.newzkl.platform.base.biz.order.model.req.query;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;

import java.util.List;

/**
* 发货单
* @author fang
*/
@Data
public class DeliverQuery extends BizPageQuery {

    /**
     * SPU订单ID
     */
    private List<Long> spuOrderIdList;
    public void setSpuOrderId(Long spuOrderId) {
        this.spuOrderIdList = doWrapperList(this.spuOrderIdList, spuOrderId);
    }
}
