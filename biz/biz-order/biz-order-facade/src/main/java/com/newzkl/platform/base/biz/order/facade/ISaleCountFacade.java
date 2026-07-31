package com.newzkl.platform.base.biz.order.facade;



import com.newzkl.platform.base.biz.order.facade.model.api.order.OrderAmountReq;
import com.newzkl.platform.base.biz.order.facade.model.api.order.OrderSummaryReq;
import com.newzkl.platform.base.biz.order.facade.model.count.OrderSummaryVO;
import com.newzkl.platform.base.biz.order.facade.model.count.SaleCountVO;
import com.newzkl.platform.base.biz.order.facade.model.order.OrderAmountVO;

import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/4/2417:01
 */
public interface ISaleCountFacade {

    SaleCountVO supplierSaleCountVO(Long accountId);

    SaleCountVO channelSaleCountVO(Long accountId);

    List<OrderSummaryVO> orderCountSummary(OrderSummaryReq query);

    OrderAmountVO orderAmountByDuration(OrderAmountReq req);
}
