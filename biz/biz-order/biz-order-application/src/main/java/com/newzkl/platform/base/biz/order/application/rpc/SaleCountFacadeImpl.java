package com.newzkl.platform.base.biz.order.application.rpc;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.newzkl.platform.base.biz.order.facade.ISaleCountFacade;
import com.newzkl.platform.base.biz.order.facade.model.api.order.OrderAmountReq;
import com.newzkl.platform.base.biz.order.facade.model.api.order.OrderSummaryReq;
import com.newzkl.platform.base.biz.order.facade.model.count.OrderSummaryVO;
import com.newzkl.platform.base.biz.order.facade.model.count.SaleCountVO;
import com.newzkl.platform.base.biz.order.facade.model.order.OrderAmountVO;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/4/2417:05
 */
@DubboService
@Component
public class SaleCountFacadeImpl implements ISaleCountFacade {

    @Autowired
    private SpuOrderDAO spuOrderDAO;
    @Autowired
    private RefundDAO refundDAO;

    @Resource
    private SkuOrderDAO skuOrderDAO;

    @Override
    public SaleCountVO supplierSaleCountVO(Long accountId) {
        SaleCountVO saleCountVO = new SaleCountVO();
        SaleCountVO orderCountVO = spuOrderDAO.supplierSaleCountVO(accountId);
        SaleCountVO refundSaleCountVO = refundDAO.supplierRefundCountVO(accountId);
        if(orderCountVO != null){
            saleCountVO.setWaitPayNumber(orderCountVO.getWaitPayNumber());
            saleCountVO.setWaitDeliveryNumber(orderCountVO.getWaitDeliveryNumber());
        }
        if(refundSaleCountVO != null){
            saleCountVO.setRefundPage(refundSaleCountVO.getRefundPage());
        }
        return saleCountVO;
    }

    @Override
    public SaleCountVO channelSaleCountVO(Long accountId) {
        SaleCountVO saleCountVO = new SaleCountVO();
        SaleCountVO orderCountVO = spuOrderDAO.channelSaleCountVO(accountId);
        SaleCountVO refundSaleCountVO = refundDAO.channelRefundCountVO(accountId);
        if(orderCountVO != null){
            saleCountVO.setWaitPayNumber(orderCountVO.getWaitPayNumber());
            saleCountVO.setWaitDeliveryNumber(orderCountVO.getWaitDeliveryNumber());
        }
        if(refundSaleCountVO != null){
            saleCountVO.setRefundPage(refundSaleCountVO.getRefundPage());
        }
        return saleCountVO;
    }

    @Override
    public List<OrderSummaryVO> orderCountSummary(OrderSummaryReq query) {
        query.startPage();
        List<OrderSummaryVO> summaryVOList = spuOrderDAO.channelCountVO(query);
        return summaryVOList;
    }

    @Override
    public OrderAmountVO orderAmountByDuration(OrderAmountReq req) {
        QueryWrapper<OrderAmountVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("create_time", req.getStartTime(), req.getEndTime());
        //queryWrapper.eq("order_type", OrderEnum.OrderType.Channel);
        queryWrapper.in("order_state",
                Arrays.asList(OrderEnum.State.SENDING.getCode(),
                        OrderEnum.State.WAIT_DELIVERY.getCode(),
                OrderEnum.State.DOWN_RECEIVE.getCode(),
                OrderEnum.State.SUCCESS.getCode(),
                OrderEnum.State.CLOSE.getCode()));

        return skuOrderDAO.orderAmountByDuration(queryWrapper);

    }
}
