package com.newzkl.platform.base.biz.order.facade.model.api.order;


import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;

import java.util.List;


@Data
public class OrderSummaryReq extends BizPageQuery {

    /**
     * 订单类型
     */
    private Integer orderType;

    /**
     * 渠道id列表
     */
    private List<Long> channelIdList;

    /**
     * 供应商id列表
     */
    private List<Long> supplierIdList;

}
