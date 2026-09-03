package com.newzkl.platform.base.biz.order.application.service;



import com.newzkl.platform.base.biz.order.model.dto.ExcelErrorVO;
import com.newzkl.platform.base.biz.order.model.req.DeliverCommand;
import com.newzkl.platform.base.biz.order.model.req.OrderCreateCommand;
import com.newzkl.platform.base.biz.order.model.req.query.OrderQuery;
import com.newzkl.platform.base.biz.order.model.vo.FullDeliverExcelVO;
import com.newzkl.platform.base.biz.order.model.vo.OrderStateCountVO;
import com.newzkl.platform.base.biz.order.model.vo.SplitDeliverExcelVO;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;

import java.util.List;
import java.util.Map;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/3/129:44
 */
public interface OrderService {

    void receiveSkuOrder(String orderNo, List<String> skuOrderNoList);

    void completeSkuOrder(String orderNo, List<String> skuOrderNoList);

    void orderBalancePay(String... orderNo);

    void orderBalancePay(Long... orderId);

    Long channelRealOrderFreight(OrderCreateCommand orderCreateCommand);

    void memberPaySuccess(String orderNo);

    void memberPaySuccess(Long orderId);

    void orderDirectPay(String orderNo);

    void deliverCreate(DeliverCommand deliverCommand);

    ExcelErrorVO fullDeliver(List<FullDeliverExcelVO> lst);

    ExcelErrorVO splitDeliver(List<SplitDeliverExcelVO> lst);

    /**
     * 超时关闭订单
     */
    void closeOrder(String orderNo);

    List<OrderStateCountVO> countOrderState(OrderQuery orderQuery);
}
