package com.newzkl.platform.base.biz.order.domain.spi;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.newzkl.platform.base.biz.order.domain.service.ThirdPartyOrderResult;
import com.newzkl.platform.base.biz.order.domain.service.ThirdPartyOrderService;
import com.newzkl.platform.base.biz.order.facade.ThirdPartyOrderFacade;
import com.newzkl.platform.base.biz.order.model.dto.OrderDTO;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderSkuVO;
import com.newzkl.platform.base.common.ddd.domain.Processor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.JsonUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ThirdPartyOrderProcessor extends Processor implements ThirdPartyOrderStrategy {

    private final ObjectProvider<ThirdPartyOrderStrategy> strategyProvider;

    private final ThirdPartyOrderService thirdPartyOrderService;

    public static ThirdPartyOrderProcessor find(){
        return SpringUtil.getBean(ThirdPartyOrderProcessor.class);
    }

    // 批量处理（供定时任务调用）
    public void processPendingRecords(int batchSize) {

        List<ThirdPartySyncRecord> pendingRecords = recordRepository.findPendingRetryRecords(
                batchSize, LocalDateTime.now()
        );
        if (pendingRecords.isEmpty()) {
            return;
        }
        log.info("本次扫描到 {} 条待补偿记录", pendingRecords.size());

        // 循环处理（每条记录单独事务，由 retrySingleRecord 的 REQUIRES_NEW 保证）
        for (ThirdPartySyncRecord record : pendingRecords) {
            try {
                retrySingleRecord(record);
            } catch (Exception e) {
                // 捕获异常，防止一条记录报错导致整个批次中断
                log.error("处理补偿记录 {} 时发生未知异常", record.getId(), e);
            }
        }
    }

    @Override
    public ThirdPartyOrderResult create(List<OrderSkuVO> outGoods, OrderDTO order) {
        // 1. 乐观锁更新：将状态改为 RETRYING，防止并发执行
        int updated = recordRepository.updateStatusToRetrying(record.getId(), record.getVersion());
        if (updated == 0) {
            log.warn("记录 {} 已被其他任务处理，跳过", record.getId());
            return;
        }

        // 2. 获取对应的订单聚合根
        Order order = orderRepository.findById(record.getOrderId())
                .orElseThrow(() -> new BizException("订单已不存在"));

        // 3. 查找匹配的 SPI 处理器（完全复用协调器的查找逻辑）
        List<ThirdPartyOrderSyncHandler> handlers = handlerProvider.stream().collect(Collectors.toList());
        ThirdPartyOrderSyncHandler matchedHandler = handlers.stream()
                .filter(h -> h.supports(record.getType()))
                .findFirst()
                .orElse(null);

        if (matchedHandler == null) {
            // 极端情况：该插件已被卸载，标记为永久失败，不再重试
            record.setStatus(Status.FAILED_PERMANENT);
            record.setLastErrorMsg("Handler not found, mark as permanent failure");
            recordRepository.save(record);
            return;
        }

        // 4. 执行同步（重试）
        try {
            SyncResult result = matchedHandler.sync(order, buildContext(record));
            // 成功：更新状态
            record.setStatus(Status.SUCCESS);
            record.setResponseData(JsonUtils.toJson(result));
            record.setLastRetryTime(LocalDateTime.now());
            recordRepository.save(record);
            log.info("补偿重试成功, recordId: {}", record.getId());
        } catch (Exception e) {
            // 失败：增加重试次数，计算下次重试时间（指数退避）
            record.setRetryCount(record.getRetryCount() + 1);
            record.setLastRetryTime(LocalDateTime.now());
            record.setLastErrorMsg(e.getMessage());

            if (record.getRetryCount() >= record.getMaxRetryCount()) {
                record.setStatus(Status.FAILED_PERMANENT); // 超过最大次数，标记终态
                log.error("记录 {} 达到最大重试次数，标记为永久失败", record.getId());
            } else {
                // 计算下次重试时间：2^retryCount 分钟后退避 (1min, 2min, 4min, 8min...)
                long delayMinutes = (long) Math.pow(2, record.getRetryCount());
                record.setNextRetryTime(LocalDateTime.now().plusMinutes(delayMinutes));
                record.setStatus(Status.FAILED); // 依然保持失败状态，等待下次扫描
            }
            recordRepository.save(record);
        }
    }

    @Override
    public ThirdPartyOrderResult delivery(List<OrderSkuVO> outGoods, OrderDTO order) {
        return null;
    }

    @Override
    public void compensation(ThirdPartyOrderDTO request) {

    }

    @Override
    public boolean supports(Object type) {
        // 必须为false，本身不参与业务
        return false;
    }
}
