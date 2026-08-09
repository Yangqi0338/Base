package com.newzkl.platform.base.biz.order.facade.model.api.order;


import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;

import java.util.List;


/**
 * 订单汇总查询参数
 *
 * @author fang
 */
@Data
public class OrderSummaryReq extends BizPageQuery {

    /**
     * 订单类型
     * @ext 无对应枚举, 保留 Integer
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
