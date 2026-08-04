package com.newzkl.platform.base.biz.order.application.service;



import com.newzkl.platform.base.biz.order.model.dto.ExcelErrorVO;
import com.newzkl.platform.base.biz.order.model.req.DeliverCommand;
import com.newzkl.platform.base.biz.order.model.req.OrderCreateCommand;
import com.newzkl.platform.base.biz.order.model.req.query.SpuOrderQuery;
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
public interface IOrderService {

    void receiveSkuOrder(Long spuOrderId, List<Long> skuOrderIdList);

    void completeSkuOrder(Long spuOrderId, List<Long> skuOrderIdList);

    void orderBalancePay(Long... id);

    Integer channelRealOrderFreight(OrderCreateCommand orderCreateCommand);

    void memberPaySuccess(Long orderId);

    void deliverCreate(DeliverCommand deliverCommand);

    ExcelErrorVO fullDeliver(List<FullDeliverExcelVO> lst);

    ExcelErrorVO splitDeliver(List<SplitDeliverExcelVO> lst);

    Map<OrderEnum.State, Integer> spuOrderStateCountMap(SpuOrderQuery spuOrderQuery);

    /**
     * 超时关闭订单
     */
    void closeOrder(Long orderId);

    List<OrderStateCountVO> countOrderState(SpuOrderQuery spuOrderQuery);
}
