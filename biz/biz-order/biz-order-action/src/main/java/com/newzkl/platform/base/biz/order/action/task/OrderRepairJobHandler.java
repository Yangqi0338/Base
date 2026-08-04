package com.newzkl.platform.base.biz.order.action.task;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.newzkl.platform.base.biz.order.domain.adapt.api.BalancePayApi;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.IOrderRepository;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.IRefundRepository;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.ISettleRepository;
import com.newzkl.platform.base.biz.order.domain.service.IOrderDomain;
import com.newzkl.platform.base.biz.order.domain.service.ISettleDomain;
import com.newzkl.platform.base.biz.order.model.dto.SkuOrderDTO;
import com.newzkl.platform.base.biz.order.model.dto.SpuOrderDTO;
import com.newzkl.platform.base.biz.order.model.req.*;
import com.newzkl.platform.base.biz.order.model.req.query.RefundQuery;
import com.newzkl.platform.base.biz.order.model.req.query.SettleRecordQuery;
import com.newzkl.platform.base.biz.order.model.req.query.SkuOrderQuery;
import com.newzkl.platform.base.biz.order.model.req.query.SpuOrderQuery;
import com.newzkl.platform.base.biz.order.model.support.api.SettlementConfigOutVO;
import com.newzkl.platform.base.biz.order.model.support.api.count.SaleCountDTO;
import com.newzkl.platform.base.biz.order.model.vo.RefundVO;
import com.newzkl.platform.base.biz.order.model.vo.SettleRecordVO;
import com.newzkl.platform.base.biz.order.model.vo.SkuOrderVO;
import com.newzkl.platform.base.biz.order.model.vo.SpuOrderVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;



/**
 * @author fang
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderRepairJobHandler {

    private final IGoodsCountFacade goodsCountFacade;
    private final IRoleFacade userCountFacade;
    private final IOrderRepository orderRepository;
    private final IRefundRepository refundRepository;
    private final ISettleDomain settleDomain;
    private final IOrderDomain orderDomain;
    private final ISettleRepository settleRepository;
    private final BalancePayApi balancePayApi;

    /**
     * 初始化待结算订单
     */
    @Transactional(rollbackFor = Exception.class)
    public void initSettleOrderWait()  {
        // 询SPU订单: 生成运费待结算单
        SpuOrderQuery spuOrderQuery = new SpuOrderQuery();
        spuOrderQuery.setOrderStateList(Arrays.asList(OrderEnum.State.WAIT_RECEIVE,OrderEnum.State.DOWN_RECEIVE,OrderEnum.State.SUCCESS));
        spuOrderQuery.setSettleSendStatus(CommonEnum.YesOrNo.NO);
        List<SpuOrderDTO> orderList = orderRepository.spuOrderList(spuOrderQuery).getRecords();

        for (SpuOrderDTO order : orderList) {
            FreightSettleOrderWaitCommand freightSettleOrderWaitCommand = new FreightSettleOrderWaitCommand();
            freightSettleOrderWaitCommand.setSpuOrderId(order.getId());
            freightSettleOrderWaitCommand.setSpuId(order.getSpuId());
            freightSettleOrderWaitCommand.setSupplierId(order.getSupplierId());
            freightSettleOrderWaitCommand.setAmount(order.getFreightAmount());
            if(!freightSettleOrderWaitCommand.getAmount().equals(0)){
                settleDomain.freightSettleOrderWaitSave(Collections.singletonList(freightSettleOrderWaitCommand),
                        orderRepository.settleOrderType(order.getSupplierId()));
            }

            order.setSettleSendState(CommonEnum.YesOrNo.YES);
        }
        orderRepository.spuOrderUpdate(orderList);
        // 查询SKU订单: 生成待结算单
        List<Long> waitSettlementOrderId = new ArrayList<>();
        List<SkuOrderDTO> waitSettlementOrder = new ArrayList<>();
        SkuOrderQuery skuOrderQuery = new SkuOrderQuery();
        skuOrderQuery.setSettleSendState(CommonEnum.YesOrNo.NO);
        skuOrderQuery.setOrderStateList(Arrays.asList(OrderEnum.State.DOWN_RECEIVE,OrderEnum.State.SUCCESS));
        List<SkuOrderDTO> skuOrderVOList = orderRepository.skuOrderList(skuOrderQuery).getRecords();
        List<Long> supplierIdList = skuOrderVOList.stream().map(SkuOrderDTO::getSupplierId).distinct().collect(Collectors.toList());
        List<SettlementConfigOutVO> settleList = orderRepository.settlementConfigBatch(supplierIdList);
        Map<Long, SettlementConfigOutVO> settleMap = settleList.stream().collect(Collectors.toMap(SettlementConfigOutVO::getId, Function.identity()));
        for (SkuOrderDTO skuOrder : skuOrderVOList) {
            if(skuOrder.getSupplierId() < 1000000
                ||CommonEnum.YesOrNo.YES == skuOrder.getSettleSendState()){
                continue;
            }
            SettlementConfigOutVO settlementConfigRpcVO = settleMap.get(skuOrder.getSupplierId());
            if(settlementConfigRpcVO == null){
                ThrowsException.exception(BaseErrorCode.PARAM, "供应商结算配置异常");
            }
            if(RoleEnum.OrderType.ORDER_SUCCESS == settlementConfigRpcVO.getOrderType()){
                if(Collections.singletonList(OrderEnum.State.SUCCESS).contains(skuOrder.getOrderState())){
                    waitSettlementOrder.add(skuOrder);
                    waitSettlementOrderId.add(skuOrder.getId());
                }
            }else if(RoleEnum.OrderType.RECEIVE == settlementConfigRpcVO.getOrderType()){
                if(Arrays.asList(OrderEnum.State.DOWN_RECEIVE, OrderEnum.State.SUCCESS).contains(skuOrder.getOrderState())){
                    waitSettlementOrder.add(skuOrder);
                    waitSettlementOrderId.add(skuOrder.getId());
                }
            }
        }
        if(ObjectUtil.isNotEmpty(waitSettlementOrderId)){
            SkuOrderQuery skuQuery = new SkuOrderQuery();
            skuQuery.setIdList(waitSettlementOrderId);
            SkuOrderDTO sku = new SkuOrderDTO();
            sku.setSettleSendState(CommonEnum.YesOrNo.YES);
            orderRepository.skuOrderUpdate(sku, skuQuery);
        }
        if (ObjectUtil.isNotEmpty(waitSettlementOrder)) {
            List<SettleOrderWaitCommand> commandList = TransferUtils.transfers(waitSettlementOrder, SettleOrderWaitCommand::new, (c, v)->{
                v.setSkuOrderId(c.getId());
                v.setOrderMoney(c.getSupplierAmount());
                v.setSkuCount(c.getCount());
                v.setType(0);
            });
            settleDomain.settleOrderWaitSave(commandList,0);
        }
    }
    /**
     * 订单统计刷新
     */
    //@Scheduled(cron = "0 0/5 * * * ? ")
    @XxlJob("orderCount")
    public ReturnT<String> orderCount(String param)  {
        try{
            //重置商品统计
            goodsCountFacade.resetSpuOrderCount();
            //重置用户统计
            userCountFacade.resetUserOrderCount();
            //查询订单
            Map<Long, SaleCountDTO> saleCountDTOS = new HashMap<>();
            Map<String, OrderPayCountReq> orderPayCountReqs = new HashMap<>();
            SpuOrderQuery spuOrderQuery = new SpuOrderQuery();
            spuOrderQuery.setOrderStateList(Arrays.asList(4,6,8,10,12));
            List<SpuOrderVO> orderVOS = orderRepository.spuOrderList(spuOrderQuery).getRecords();
            for (SpuOrderVO orderVO : orderVOS) {
                //商品
                SaleCountDTO saleCountDTO = saleCountDTOS.get(orderVO.getSpuId());
                if(saleCountDTO == null){
                    saleCountDTO = new SaleCountDTO(orderVO.getSpuId());
                    saleCountDTOS.put(orderVO.getSpuId(), saleCountDTO);
                }
                saleCountDTO.setBuyCount(saleCountDTO.getBuyCount() + (orderVO.getSkuCount() == null ? 0 : orderVO.getSkuCount()));
                saleCountDTO.setGoodsAmount(saleCountDTO.getGoodsAmount() + orderVO.getGoodsAmount());
                saleCountDTO.setSupplierAmount(saleCountDTO.getSupplierAmount() + orderVO.getSupplierAmount());
                //供应商用户
                String supplierKey = orderVO.getSupplierId() * RoleEnum.CompanyRole.SUPPLIER.getCode() + DateUtil.format(orderVO.getCreateTime(), DatePattern.PURE_DATE_PATTERN);
                OrderPayCountReq supplier = orderPayCountReqs.get(supplierKey);
                if(supplier == null){
                    supplier = new OrderPayCountReq();
                    supplier.setAccountId(orderVO.getSupplierId());
                    supplier.setRole(RoleEnum.CompanyRole.SUPPLIER);
                    supplier.setTime(DateUtil.format(orderVO.getCreateTime(), DatePattern.PURE_DATE_PATTERN));
                    orderPayCountReqs.put(supplierKey, supplier);
                }
                supplier.setTotalOrderAmount(supplier.getTotalOrderAmount() + orderVO.getSupplierAmount() + orderVO.getFreightAmount());
                supplier.setTotalOrderNumber(supplier.getTotalOrderNumber() + 1);
                supplier.setTotalOrderGoodsAmount(supplier.getTotalOrderAmount() + orderVO.getSupplierAmount());
                supplier.setTotalOrderGoodsNumber(supplier.getTotalOrderGoodsNumber() + orderVO.getSkuCount());
                //渠道商用户
                String channelKey = orderVO.getChannelId() * RoleEnum.CompanyRole.CHANNEL.getCode() + DateUtil.format(orderVO.getCreateTime(), DatePattern.PURE_DATE_PATTERN);
                OrderPayCountReq channel = orderPayCountReqs.get(channelKey);
                if(channel == null){
                    channel = new OrderPayCountReq();
                    channel.setAccountId(orderVO.getChannelId());
                    channel.setRole(RoleEnum.CompanyRole.CHANNEL);
                    channel.setTime(DateUtil.format(orderVO.getCreateTime(), DatePattern.PURE_DATE_PATTERN));
                    orderPayCountReqs.put(channelKey, channel);
                }
                channel.setTotalOrderAmount(channel.getTotalOrderAmount() + orderVO.getGoodsAmount() + orderVO.getFreightAmount());
                channel.setTotalOrderNumber(channel.getTotalOrderNumber() + 1);
            }
            //查询售后
            Map<String, RefundCountReq> refundCountMap = new HashMap<>();
            RefundQuery refundQuery = new RefundQuery();
            refundQuery.setRefundStateList(Arrays.asList(RefundEnum.State.MONEY_ING,RefundEnum.State.SUCCESS));
            List<RefundVO> refundVOList = refundRepository.refundVOList(refundQuery).getRecords();
            for (RefundVO refundVO : refundVOList) {
                //供应商用户
                String supplierKey = refundVO.getSupplierId() * RoleEnum.CompanyRole.SUPPLIER.getCode() + DateUtil.format(refundVO.getCreateTime(), DatePattern.PURE_DATE_PATTERN);
                RefundCountReq supplier = refundCountMap.get(supplierKey);
                if(supplier == null){
                    supplier = new RefundCountReq();
                    supplier.setAccountId(refundVO.getSupplierId());
                    supplier.setRole(RoleEnum.CompanyRole.SUPPLIER);
                    supplier.setTime(DateUtil.format(refundVO.getCreateTime(), DatePattern.PURE_DATE_PATTERN));
                    refundCountMap.put(supplierKey, supplier);
                }
                supplier.setTotalRefundAmount(supplier.getTotalRefundAmount() + refundVO.getSupplierAmount() + refundVO.getFreightAmount());
                supplier.setTotalRefundNumber(supplier.getTotalRefundNumber() + 1);
                //渠道商用户
                String channelKey = refundVO.getChannelId() * RoleEnum.CompanyRole.CHANNEL.getCode() + DateUtil.format(refundVO.getCreateTime(), DatePattern.PURE_DATE_PATTERN);
                RefundCountReq channel = refundCountMap.get(channelKey);
                if(channel == null){
                    channel = new RefundCountReq();
                    channel.setAccountId(refundVO.getChannelId());
                    channel.setRole(RoleEnum.CompanyRole.CHANNEL);
                    channel.setTime(DateUtil.format(refundVO.getCreateTime(), DatePattern.PURE_DATE_PATTERN));
                    refundCountMap.put(channelKey, channel);
                }
                channel.setTotalRefundAmount(channel.getTotalRefundAmount() + refundVO.getRefundAmount());
                channel.setTotalRefundNumber(channel.getTotalRefundNumber() + 1);
            }
            //执行
            goodsCountFacade.spuSaleCount(new ArrayList<>(saleCountDTOS.values()));
            userCountFacade.orderPayCount(new ArrayList<>(orderPayCountReqs.values()));
            userCountFacade.refundCount(new ArrayList<>(refundCountMap.values()));
        }catch (Exception e){
            log.warn("订单刷新统计异常", e);
        }
        return new ReturnT<>();
    }

    public void finance(List<Long> idList) {
        SettleRecordQuery settleRecordQuery = new SettleRecordQuery();
        settleRecordQuery.setIdList(idList);
        List<SettleRecordVO> list = settleRepository.settleRecordVOList(settleRecordQuery).getRecords();
        for (SettleRecordVO settleRecordVO : list) {
            //财务金额划分
            Long supplierId = settleRecordVO.getSupplierId();
            Integer settleMoney = settleRecordVO.getSettleMoney();
            log.info("开始打款:supplierId:"+ supplierId + ":settleMoney" + settleMoney);
            SupplierSettleReq supplierSettleReq = new SupplierSettleReq();
            supplierSettleReq.setAccountId(supplierId);
            supplierSettleReq.setSettleAmount(settleMoney);
            supplierSettleReq.setJoinSettleOrderNo(settleRecordVO.getId());
            balancePayApi.supplierSettle(supplierSettleReq);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void repairSettle() {
        //取出已收货,未结算,收货时间小于7月20号的sku订单
        SkuOrderQuery skuOrderQuery = new SkuOrderQuery();
        LocalDateTime offsetLocalDate = DateUtil.parseLocalDateTime("2024-07-23 19:00:00", DatePattern.NORM_DATETIME_PATTERN);
        skuOrderQuery.setSettleSendState(CommonEnum.YesOrNo.NO);
        skuOrderQuery.setLessReceiveTime(offsetLocalDate);
        List<SkuOrderDTO> skuOrderVOList = orderRepository.skuOrderList(skuOrderQuery).getRecords();
        //判断供应商结算节点并写入待结算记录
        List<Long> waitSettlementOrderId = new ArrayList<>();
        List<SkuOrderDTO> waitSettlementOrder = new ArrayList<>();
        List<Long> supplierIdList = skuOrderVOList.stream().map(SkuOrderDTO::getSupplierId).distinct().collect(Collectors.toList());
        List<SettlementConfigOutVO> settlementConfigList = orderRepository.settlementConfigBatch(supplierIdList);
        Map<Long, SettlementConfigOutVO> settlementConfigMap = settlementConfigList.stream().collect(Collectors.toMap(SettlementConfigOutVO::getId, Function.identity()));
        for (SkuOrderDTO skuOrderVO : skuOrderVOList) {
            SettlementConfigOutVO settlementConfigVO = settlementConfigMap.get(skuOrderVO.getSupplierId());
            if(settlementConfigVO == null) {
                //ThrowsException.exception(BaseErrorCode.PARAM, "供应商结算配置异常");
                continue;
            }
            if(RoleEnum.OrderType.RECEIVE == settlementConfigVO.getOrderType()){
                if(CommonEnum.YesOrNo.NO == skuOrderVO.getSettleSendState()){
                    waitSettlementOrder.add(skuOrderVO);
                    waitSettlementOrderId.add(skuOrderVO.getId());
                }
                //触发分润
                //orderRepository.wakeUpDelayMessage(Tag.EARNING + skuOrderVO.getId());
            }
        }
        //写入结算记录
        List<SettleOrderWaitCommand> commandList = TransferUtils.transfers(waitSettlementOrder, SettleOrderWaitCommand::new, (c,v)->{
            v.setSkuOrderId(c.getId());
            v.setOrderMoney(c.getSupplierAmount());
            v.setSkuCount(c.getCount());
            v.setType(0);
        });
        settleDomain.settleOrderWaitSave(commandList,0);
        //修改结算状态
        if(ObjectUtil.isNotEmpty(waitSettlementOrderId)){
            SkuOrderQuery skuQuery = new SkuOrderQuery();
            skuQuery.setIdList(waitSettlementOrderId);
            SkuOrderDTO sku = new SkuOrderDTO();
            sku.setSettleSendState(CommonEnum.YesOrNo.YES);
            orderRepository.skuOrderUpdate(sku, skuQuery);
        }
        //执行结算, 写入结算记录, 修改结算商品的信息
        SecurityContextHolder.set("waitSettlementOrderId", waitSettlementOrderId);
    }
}
