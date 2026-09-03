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
     * 订单号
     */
    private List<String> orderNoList;
    public void setOrderNo(String orderNo) {
        this.orderNoList = doWrapperList(this.orderNoList, orderNo);
    }
}
