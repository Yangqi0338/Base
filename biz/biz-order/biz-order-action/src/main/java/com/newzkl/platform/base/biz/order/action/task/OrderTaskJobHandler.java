package com.newzkl.platform.base.biz.order.action.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.newzkl.platform.base.biz.order.application.service.OrderService;
import com.newzkl.platform.base.biz.order.application.service.QueryService;
import com.newzkl.platform.base.biz.order.application.service.RefundService;
import com.newzkl.platform.base.biz.order.domain.adapt.api.DictApi;
import com.newzkl.platform.base.biz.order.domain.adapt.api.LocalMessageApi;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.IRefundRepository;
import com.newzkl.platform.base.biz.order.domain.service.IOrderDomain;
import com.newzkl.platform.base.biz.order.domain.service.IRefundDomain;
import com.newzkl.platform.base.biz.order.model.dto.RefundDTO;
import com.newzkl.platform.base.biz.order.model.dto.SpuOrderDTO;
import com.newzkl.platform.base.biz.order.model.req.query.OrderQuery;
import com.newzkl.platform.base.biz.order.model.req.query.RefundQuery;
import com.newzkl.platform.base.biz.order.model.req.query.SkuOrderQuery;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderConfigVO;
import com.newzkl.platform.base.biz.order.model.vo.FreightExt;
import com.newzkl.platform.base.biz.order.model.vo.SkuOrderVO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.RefundOperateTypeEnum;
import com.newzkl.platform.base.common.ddd.model.enums.sys.DictEnum;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder.isDev;

/**
 * @author fang
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderTaskJobHandler {

    private final IOrderDomain orderDomain;
    private final OrderService orderService;
    private final QueryService queryService;
    private final DictApi dictApi;
    private final LocalMessageApi localMessageApi;
    private final IRefundRepository refundRepository;
    private final RefundService refundService;
    private final IRefundDomain refundDomain;

    private static final String SUCCESS = "SUCCESS";

    LoadingCache<DictEnum.Key, OrderConfigVO> orderConfig = CacheBuilder.newBuilder()
            .maximumSize(20)
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build(new CacheLoader<>() {
                @Nullable
                @Override
                public OrderConfigVO load(@Nullable DictEnum.Key key) {
                    return dictApi.get(key.getCode());
                }
            });

    /**
     * 派发订单
     */
    //@Scheduled(cron = "0 0/1 * * * ? ")
    @XxlJob("orderSend")
    public ReturnT<String> orderSend(String param)  {
        Date date = new Date();
        DateTime offsetDate = null;
        if(isDev()){
            offsetDate = DateUtil.offset(date, DateField.MINUTE, -1);
        }else {
            offsetDate = DateUtil.offset(date, DateField.MINUTE, -15);
        }
        LocalDateTime offsetLocalDate = DateUtil.toLocalDateTime(offsetDate);
        //取出超过*分钟, 并且处于派发中的订单
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.setOrderState(OrderEnum.State.SENDING);
        orderQuery.setCreateStartTime(offsetLocalDate);
        List<Long> orderIdList = queryService.orderIdList(orderQuery);
        orderDomain.sendOrder(orderIdList);
        log.info("自动派发订单");
        return new ReturnT<>(SUCCESS);
    }
    /**
     * 自动收货
     */
    //@Scheduled(cron = "0 0/1 * * * ? ")
    @XxlJob("orderReceive")
    public ReturnT<String> orderReceive(String param) throws ExecutionException {
        Date date = new Date();
        DateTime offsetDate = null;
        if(isDev()){
            offsetDate = DateUtil.offset(date, DateField.MINUTE, -7);
        }else {
            Integer autoReceive = orderConfig.get(DictEnum.Key.ORDER_CONFIG).getAutoReceive();
            offsetDate = DateUtil.offset(date, DateField.HOUR, -24 * autoReceive);
        }
        LocalDateTime offsetLocalDate = DateUtil.toLocalDateTime(offsetDate);
        //待收货,超过发货时间*天,无售后
        SkuOrderQuery orderQuery = new SkuOrderQuery();
        orderQuery.setOrderState(OrderEnum.State.WAIT_RECEIVE);
        orderQuery.setLessDeliverTime(offsetLocalDate);
        orderQuery.setRefundingCount(0);
        List<SkuOrderVO> skuOrderVOList = queryService.skuOrderVOList(orderQuery).getRecords();
        Map<Long, List<SkuOrderVO>> skuOrderVOMap = skuOrderVOList.stream().collect(Collectors.groupingBy(SkuOrderVO::getSpuOrderId));
        for (Long spuOrderId : skuOrderVOMap.keySet()) {
            List<Long> skuOrderIdList = skuOrderVOMap.get(spuOrderId).stream().map(SkuOrderVO::getId).collect(Collectors.toList());
            if(ObjectUtil.isNotEmpty(skuOrderIdList)){
                orderService.receiveSkuOrder(spuOrderId, skuOrderIdList);
            }
        }
        log.info("自动收货订单");
        return new ReturnT<>(SUCCESS);
    }
    /**
     * 自动完成
     */
    //@Scheduled(cron = "0 0/1 * * * ? ")
    @XxlJob("orderComplete")
    public ReturnT<String> orderComplete(String param) throws ExecutionException {
        Date date = new Date();
        DateTime offsetDate = null;
        if(isDev()){
            offsetDate = DateUtil.offset(date, DateField.MINUTE, -7);
        }else {
            Integer notRefund = orderConfig.get(DictEnum.Key.ORDER_CONFIG).getNotRefund();
            offsetDate = DateUtil.offset(date, DateField.HOUR, -24 * notRefund);
        }
        LocalDateTime offsetLocalDate = DateUtil.toLocalDateTime(offsetDate);
        //已收货, 超过发货时间*天, 无售后
        SkuOrderQuery orderQuery = new SkuOrderQuery();
        orderQuery.setOrderState(OrderEnum.State.DOWN_RECEIVE);
        orderQuery.setLessReceiveTime(offsetLocalDate);
        orderQuery.setRefundingCount(0);
        List<SkuOrderVO> skuOrderVOList = queryService.skuOrderVOList(orderQuery).getRecords();
        //收货订单, 按SPU订单分组
        Map<Long, List<SkuOrderVO>> skuOrderVOMap = skuOrderVOList.stream().collect(Collectors.groupingBy(SkuOrderVO::getSpuOrderId));
        for (Long spuOrderId : skuOrderVOMap.keySet()) {
            List<Long> skuOrderIdList = skuOrderVOMap.get(spuOrderId).stream().map(SkuOrderVO::getId).collect(Collectors.toList());
            if(ObjectUtil.isNotEmpty(skuOrderIdList)){
                orderService.completeSkuOrder(spuOrderId, skuOrderIdList);
            }
        }
        log.info("自动完成订单");
        return new ReturnT<>(SUCCESS);
    }

    /**
     * 渠道商待付款 超时自动关闭
     * @param param
     * @return
     */
    @XxlJob("channelCancelOrder")
    public ReturnT<String> channelCancelOrder(String param){
        LocalDateTime oneDayAgo;
        if(isDev()){//测试环境7分钟
            oneDayAgo = LocalDateTime.now().minusMinutes(7);
        }else {//生产环境5天
            oneDayAgo = LocalDateTime.now().minusDays(5);
        }
        List<SpuOrderDTO> spuOrders = orderDomain.listDOByOrderStateAndUpdateTimeLessThan(OrderEnum.State.CHANNEL_WAIT_PAY, oneDayAgo);
        if (CollUtil.isEmpty(spuOrders)){
            log.info("渠道商没有待付款且超时的订单");
            return new ReturnT<>(SUCCESS);
        }
        for (SpuOrderDTO spuOrder : spuOrders) {
            orderDomain.channelCancelOrder(spuOrder.getId(),"渠道商采购金不足，超时未支付订单关闭！");
        }
        return new ReturnT<>(SUCCESS);
    }
    // ----------------------------------------------售后单任务--------------------------------------------------
    /**
     * 售后同意
     */
    //@Scheduled(cron = "0 0/1 * * * ? ")
    @XxlJob("refundAgree")
    public ReturnT<String> refundAgree(String param) throws ExecutionException {
        Date date = new Date();
        DateTime offsetDate = null;
        if(isDev()){
            offsetDate = DateUtil.offset(date, DateField.MINUTE, -7);
        }else {
            Integer notRefund = orderConfig.get(DictEnum.Key.ORDER_CONFIG).getNotRefund();
            offsetDate = DateUtil.offset(date, DateField.HOUR, -24 * notRefund);
        }
        LocalDateTime offsetLocalDate = DateUtil.toLocalDateTime(offsetDate);
        //已收货, 超过发货时间*天, 无售后
        RefundQuery refundQuery = new RefundQuery();
        refundQuery.setRefundStateList(Arrays.asList(
                RefundEnum.State.CHANNEL_WAIT,
                RefundEnum.State.SUPPLIER_WAIT,
                RefundEnum.State.RECEIVE_WAIT));
        refundQuery.setStateTimeLess(offsetLocalDate);
        List<RefundDTO> refundVOList = refundRepository.page(refundQuery).getRecords();
        for (RefundDTO refundVO : refundVOList) {
            if(RefundEnum.State.CHANNEL_WAIT == refundVO.getRefundState()){
                refundService.channelAudit(refundVO.getId(),null , CommonEnum.YesOrNo.YES, "系统自动审核", true);
            }else if(RefundEnum.State.SUPPLIER_WAIT == refundVO.getRefundState()){
                refundService.supplierAudit(refundVO.getId(), CommonEnum.YesOrNo.YES, true);
            }else if(RefundEnum.State.RECEIVE_WAIT == refundVO.getRefundState()){
                refundDomain.confirmRefundFreight(refundVO.getId());
                localMessageApi.sendRefundOperationRecord(refundVO, RefundEnum.State.RECEIVE_WAIT, RefundEnum.State.RECEIVE_WAIT, RefundOperateTypeEnum.SUPPLIER_CONFIRM_RECEIPT);
            }
        }
        log.info("自动售后同意");
        return new ReturnT<>(SUCCESS);
    }

    /**
     * 售后单 待提交物流 已拒绝 超时自动关闭
     * @param param
     * @return
     * @throws ExecutionException
     */
    @XxlJob("refundOrderClose")
    public ReturnT<String> refundOrderClose(String param) throws ExecutionException {
        Date date = new Date();
        DateTime offsetDate = null;
        if(isDev()){
            offsetDate = DateUtil.offset(date, DateField.MINUTE, -7);
        }else {
            Integer notRefund = orderConfig.get(DictEnum.Key.ORDER_CONFIG).getAutoAgreeRefund();
            offsetDate = DateUtil.offset(date, DateField.HOUR, -24 * notRefund);
        }
        LocalDateTime offsetLocalDate = DateUtil.toLocalDateTime(offsetDate);
        //待提交物流 和 已拒绝 但未关闭的订单
        RefundQuery refundQuery = new RefundQuery();
        refundQuery.setRefundStateList(Arrays.asList(RefundEnum.State.REFUSE,RefundEnum.State.FREIGHT_WAIT));
        refundQuery.setStateTimeLess(offsetLocalDate);
        List<RefundDTO> refundVOList = refundRepository.page(refundQuery).getRecords();
        for (RefundDTO refundVO : refundVOList) {
            RefundDTO refundEdit = new RefundDTO();
            FreightExt freightExt =refundVO.getFreightExt();
            if (Objects.isNull(freightExt)){
                freightExt = new FreightExt();
            }
            freightExt.setCloseReason("超时系统自动关闭");
            refundEdit.setFreightExt(freightExt);
            refundRepository.updateState(refundEdit, refundVO.getId(), refundVO.getOrderType(), refundVO.getRefundState(), RefundEnum.State.CLOSE, refundVO.getChannelId());
            refundDomain.skuOrderEditForRefundClose(refundVO.getSpuOrderId(), refundVO.getItem());
            localMessageApi.sendRefundOperationRecord(refundVO, RefundEnum.State.CHANNEL_WAIT, RefundEnum.State.CHANNEL_WAIT, RefundOperateTypeEnum.BUYER_TIMEOUT_CLOSE);
            log.info("当前售后单（商家拒绝）超时自动关闭：{}",refundVO.getId());
        }

        return new ReturnT<>(SUCCESS);
    }
}
