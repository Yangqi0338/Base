package com.newzkl.platform.base.biz.order.action.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

import com.newzkl.platform.base.biz.order.application.service.IOrderService;
import com.newzkl.platform.base.biz.order.domain.adapt.api.GoodsSpuApi;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.IOrderRepository;
import com.newzkl.platform.base.biz.order.facade.model.hdh.ItemInfo;
import com.newzkl.platform.base.biz.order.facade.model.hdh.OrderCallbackRequest;
import com.newzkl.platform.base.biz.order.facade.model.hdh.PkgInfo;
import com.newzkl.platform.base.biz.order.model.req.DeliverCommand;
import com.newzkl.platform.base.biz.order.model.req.DeliverItemCommand;
import com.newzkl.platform.base.biz.order.model.support.api.openapi.ApiSkuVO;
import com.newzkl.platform.base.biz.order.model.vo.OrderVO;
import com.newzkl.platform.base.biz.order.model.vo.SkuOrderVO;
import com.newzkl.platform.base.common.ddd.domain.utils.COrderStateMachine;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.ThirdPartyStateMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class HdhEvent {
    
    @Autowired
    private IOrderRepository orderRepository;
    
    @Autowired
    private IOrderService orderService;

    @Autowired
    private GoodsSpuApi spuApi;
    
    /**
     * 处理订单状态回调的核心业务逻辑
     *
     * @param callbackRequest 回调请求参数
     *
     * @return 处理成功返回true，否则false
     */
    public boolean handleStatusCallback(OrderCallbackRequest callbackRequest) {
        // 1. 解析外部订单号
        String userOrderNum = callbackRequest.getUserOrderNum();
        if (StrUtil.isBlank(userOrderNum)) {
            log.warn("回调外部订单号为空");
            return false;
        }
        Long orderId;
        try {
            
            orderId = Long.valueOf(userOrderNum);
        }
        catch (NumberFormatException e) {
            log.warn("外部订单号格式错误：{}", userOrderNum, e);
            return false;
        }
        
        // 2. 查询订单（确保存在）
        OrderVO orderVO = orderRepository.orderVO(orderId);
        if (orderVO == null) {
            log.warn("未查询到订单，外部订单号：{}", userOrderNum);
            return false;
        }
        log.info("查询到订单，ID：{}，当前状态：{}", orderVO.getId(), orderVO.getOrderState());
        
        // 3. 三方状态码映射为我方状态码
        Integer thirdPartyCode = callbackRequest.getOrderStatusCode();
        OrderEnum.State ourCode = ThirdPartyStateMapping.getOurCodeByThirdPartyCode(thirdPartyCode);
        if (ourCode == null) {
            log.warn("三方状态码映射失败，三方码：{}，订单ID：{}", thirdPartyCode, orderVO.getId());
            return false;
        }
        
        // 4. 状态机校验转换合法性
        OrderEnum.State currentState = orderVO.getOrderState();
        if (currentState == null) {
            log.warn("状态解析失败，当前状态码：{}，目标状态码：{}，订单ID：{}", null, ourCode, orderVO.getId());
            return false;
        }
        
        // 若状态未变更，直接返回成功
        if (currentState == ourCode) {
            log.info("订单状态未变更，无需处理，订单ID：{}，状态：{}", orderVO.getId(), currentState.getInfo());
            return true;
        }
        
        // 校验转换合法性
        OrderEnum.State validatedTarget;
        try {
            validatedTarget = COrderStateMachine.transition(currentState, ourCode);
        }
        catch (IllegalArgumentException e) {
            log.error("状态转换非法，订单ID：{}，当前状态：{}，目标状态：{}，原因：{}",
                orderVO.getId(),
                currentState.getInfo(),
                ourCode.getInfo(),
                e.getMessage());
            return false;
        }
        if (ourCode == OrderEnum.State.WAIT_RECEIVE) {
            List<PkgInfo> pkgList = callbackRequest.getPkgList();
            pkgList.forEach(pkgInfo -> {
                String expressNum = pkgInfo.getExpressNum();
                String expressCompany = pkgInfo.getExpressCompany();
                List<ItemInfo> itemList = pkgInfo.getItemList();
                List<String> skuIdList = itemList.stream().map(ItemInfo::getSkuId).collect(Collectors.toList());
                orderDeliver(skuIdList, orderId, expressCompany, expressNum, itemList);
            });
        }
        
        log.info("订单状态更新成功，订单ID：{}，{}→{}", orderVO.getId(), currentState.getInfo(), validatedTarget.getInfo());
        return true;
    }
    
    /**
     * 处理订单发货逻辑：根据外部SKU映射内部SKU，使用回调中的商品数量构建发货命令
     * 
     * @param outSkuIdList 外部SKU ID列表
     * @param orderId 订单ID
     * @param expressCompanyName 快递公司名称
     * @param expressNo 快递单号
     * @param itemList 回调中的商品信息列表（含外部SKU和发货数量）
     */
    public void orderDeliver(List<String> outSkuIdList, Long orderId, String expressCompanyName, String expressNo,
        List<ItemInfo> itemList) {
        // 1. 校验入参，避免空指针
        if (CollUtil.isEmpty(outSkuIdList) || orderId == null || StrUtil.isBlank(expressNo)
            || CollUtil.isEmpty(itemList)) {
            log.warn("发货参数不完整，订单ID：{}，外部SKU列表：{}，快递单号：{}，商品信息：{}", orderId, outSkuIdList, expressNo, itemList);
            return;
        }
        
        // 2. 调用RPC获取外部SKU与内部SKU的映射关系（ApiSkuVO）
        List<ApiSkuVO> apiSkuVOList = spuApi.querySkuIdListByOutId(outSkuIdList);
        if (CollUtil.isEmpty(apiSkuVOList)) {
            log.warn("未查询到外部SKU对应的内部信息，外部SKU列表：{}，订单ID：{}", outSkuIdList, orderId);
            return;
        }
        
        // 3. 构建映射关系：内部SKU ID → 外部SKU ID（修正命名，明确映射方向）
        Map<Long, String> innerIdToOutSkuMap = apiSkuVOList.stream()
            .filter(sku -> StrUtil.isNotBlank(sku.getOutSkuId())) // 过滤无效外部SKU
            .collect(Collectors.toMap(ApiSkuVO::getId, // key：内部SKU ID
                ApiSkuVO::getOutSkuId, // value：外部SKU ID
                (existing, replacement) -> {
                    log.warn("内部SKU对应多个外部SKU，保留第一个。内部SKU：{}，外部SKU1：{}", existing, replacement);
                    return existing;
                }));
        
        // 4. 构建外部SKU → 发货数量的映射（从回调的ItemInfo中提取）
        Map<String, Integer> outSkuToNumberMap = itemList.stream()
            .filter(item -> StrUtil.isNotBlank(item.getSkuId()) && item.getNumber() != null && item.getNumber() > 0)
            .collect(Collectors.toMap(ItemInfo::getSkuId, // key：外部SKU ID
                ItemInfo::getNumber, // value：发货数量（只保留有效数量）
                (existing, replacement) -> {
                    log.warn("外部SKU对应多个数量，取合计。外部SKU：{}，数量1：{}，数量2：{}", existing, replacement, existing + replacement);
                    return existing + replacement; // 若重复，合计数量
                }));
        
        // 5. 提取内部SKU ID列表，查询订单下的SKU信息（用于分组）
        List<Long> innerSkuIdList = new ArrayList<>(innerIdToOutSkuMap.keySet());
        List<SkuOrderVO> skuOrderVOList = orderRepository.querySkuOrderByOrderId(orderId, innerSkuIdList);
        if (CollUtil.isEmpty(skuOrderVOList)) {
            log.warn("订单下无匹配的SKU信息，订单ID：{}，内部SKU列表：{}", orderId, innerSkuIdList);
            return;
        }
        
        // 6. 按SPU订单ID分组，构建发货命令
        Map<Long, List<SkuOrderVO>> spuOrderIdToSkuMap =
            skuOrderVOList.stream().collect(Collectors.groupingBy(SkuOrderVO::getSpuOrderId));
        
        for (Map.Entry<Long, List<SkuOrderVO>> entry : spuOrderIdToSkuMap.entrySet()) {
            Long spuOrderId = entry.getKey();
            List<SkuOrderVO> skuOrderList = entry.getValue();
            
            // 构建当前SPU订单的发货命令
            DeliverCommand deliverCommand = new DeliverCommand();
            deliverCommand.setSpuOrderId(spuOrderId);
            deliverCommand.setExpressCompanyName(expressCompanyName);
            deliverCommand.setExpressNo(expressNo);
            deliverCommand.setExpressMobile("");
            
            // 转换为发货商品列表（使用回调中的数量）
            List<DeliverItemCommand> deliverItemList = skuOrderList.stream().map(skuOrder -> {
                Long innerSkuId = skuOrder.getSkuId();
                String outSkuId = innerIdToOutSkuMap.get(innerSkuId);
                
                // 校验外部SKU映射关系
                if (StrUtil.isBlank(outSkuId)) {
                    log.warn("内部SKU未找到对应外部SKU，跳过发货。内部SKU：{}，订单ID：{}", innerSkuId, orderId);
                    return null;
                }
                
                // 获取发货数量（处理空值和无效值）
                Integer deliverNum = outSkuToNumberMap.get(outSkuId);
                if (deliverNum == null || deliverNum <= 0) {
                    log.warn("外部SKU未找到有效发货数量，跳过发货。外部SKU：{}，内部SKU：{}，订单ID：{}", outSkuId, innerSkuId, orderId);
                    return null;
                }
                
                // 构建发货商品详情
                DeliverItemCommand itemCommand = new DeliverItemCommand();
                itemCommand.setSkuId(innerSkuId);
                itemCommand.setCount(deliverNum);
                return itemCommand;
            })
                .filter(Objects::nonNull) // 过滤无效项
                .collect(Collectors.toList());
            
            // 若有有效发货商品，执行发货
            if (!CollUtil.isEmpty(deliverItemList)) {
                deliverCommand.setDeliverItemCommandList(deliverItemList);
                orderService.deliverCreate(deliverCommand);
                log.info("SPU订单发货命令已提交，SPU订单ID：{}，快递单号：{}，发货商品数：{}", spuOrderId, expressNo, deliverItemList.size());
            }
            else {
                log.warn("SPU订单无有效发货商品，跳过发货。SPU订单ID：{}，订单ID：{}", spuOrderId, orderId);
            }
        }
    }
}
