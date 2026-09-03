package com.newzkl.platform.base.biz.order.action.task;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.OrderRepository;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.RefundRepository;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.SettleRepository;
import com.newzkl.platform.base.biz.order.domain.service.SettleDomain;
import com.newzkl.platform.base.biz.order.model.dto.SkuOrderDTO;
import com.newzkl.platform.base.biz.order.model.req.FreightSettleOrderWaitCommand;
import com.newzkl.platform.base.biz.order.model.req.SettleOrderWaitCommand;
import com.newzkl.platform.base.biz.order.model.req.query.SkuOrderQuery;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import com.newzkl.platform.base.common.ddd.facade.SettlementConfigOutVO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final OrderRepository orderRepository;
    private final RefundRepository refundRepository;
    private final SettleDomain settleDomain;
    private final SettleRepository settleRepository;

    /**
     * 初始化待结算订单
     */
    @Transactional(rollbackFor = Exception.class)
    public void initSettleOrderWait()  {
        // SpuOrder 层折叠: 原首段"询 SPU 订单生成运费待结算单"依赖已删 orderRepository.spuOrderList/spuOrderSave
        // 及 SpuOrderDTO.supplierId/settleSendState(OrderDTO 无对应字段), 且本方法为无 @XxlJob 无调用方的
        // 一次性数据修复死码(硬编码日期), 故运费待结算首段整体删除; SKU 待结算段依赖健在, 保留。
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
            if(EarningsEnum.SettleType.ORDER_SUCCESS == settlementConfigRpcVO.getOrderType()){
                if(Collections.singletonList(OrderEnum.State.SUCCESS).contains(skuOrder.getOrderState())){
                    waitSettlementOrder.add(skuOrder);
                    waitSettlementOrderId.add(skuOrder.getId());
                }
            }else if(EarningsEnum.SettleType.RECEIVE == settlementConfigRpcVO.getOrderType()){
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
            orderRepository.skuOrderSave(sku, skuQuery);
        }
        if (ObjectUtil.isNotEmpty(waitSettlementOrder)) {
            List<SettleOrderWaitCommand> commandList = TransferUtils.transfers(waitSettlementOrder, SettleOrderWaitCommand::new, (c, v)->{
                v.setSkuOrderId(c.getId());
                v.setOrderMoney(c.getSupplierAmount());
                v.setSkuCount(c.getCount());
                v.setType(0);
            });
            settleDomain.settleOrderWaitSave(commandList, EarningsEnum.SettleType.ORDER_SUCCESS);
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
            if(EarningsEnum.SettleType.RECEIVE == settlementConfigVO.getOrderType()){
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
        settleDomain.settleOrderWaitSave(commandList, EarningsEnum.SettleType.ORDER_SUCCESS);
        //修改结算状态
        if(ObjectUtil.isNotEmpty(waitSettlementOrderId)){
            SkuOrderQuery skuQuery = new SkuOrderQuery();
            skuQuery.setIdList(waitSettlementOrderId);
            SkuOrderDTO sku = new SkuOrderDTO();
            sku.setSettleSendState(CommonEnum.YesOrNo.YES);
            orderRepository.skuOrderSave(sku, skuQuery);
        }
        //执行结算, 写入结算记录, 修改结算商品的信息
        SecurityContextHolder.set("waitSettlementOrderId", waitSettlementOrderId);
    }
}
