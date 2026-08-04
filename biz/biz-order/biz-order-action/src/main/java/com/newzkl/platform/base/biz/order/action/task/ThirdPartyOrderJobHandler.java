package com.newzkl.platform.base.biz.order.action.task;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.IOrderRepository;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.ThirdPartyOrderRepository;
import com.newzkl.platform.base.biz.order.domain.spi.ThirdPartyOrderProcessor;
import com.newzkl.platform.base.biz.order.facade.model.order.ThirdPartyOrderRecordDTO;
import com.newzkl.platform.base.biz.order.model.dto.OrderDTO;
import com.newzkl.platform.base.common.core.utils.common.FeiShuMessageSendUtil;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.PlatformTypeEnum;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author sijiwang 三方订单数据补偿任务
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ThirdPartyOrderJobHandler {

    private final ThirdPartyOrderRepository thirdPartyOrderRepository;
    private final IOrderRepository orderRepository;

    @Value("${feishu.bot.huiDingHuo.webhook:https://open.feishu.cn/open-apis/bot/v2/hook/f1cc7bc3-3f2f-48a0-8357-1e0b9b8d0a50}")
    private String webhook;
    
    private static final String SUCCESS = "SUCCESS";
    
    @XxlJob("createOrder")
    public ReturnT<String> create(String param) {
        log.info("XXL Job任务[createOrder]开始执行，参数：{}", param);
        try {
            // 1. 查询需要补偿的失败订单
            List<ThirdPartyOrderRecordDTO> compensationList =
                    thirdPartyOrderRepository.findByStatusAndNextRetryTimeBefore(PlatformTypeEnum.HUI_DING_HUO,
                    CommonEnum.RequestStatusEnum.FAILED,
                    HuiDingHuoApiUtils.ORDER_CREATE_URL);
            log.info("获取到需要补偿的失败订单数量：{}", compensationList.size());
            
            if (CollUtil.isEmpty(compensationList)) {
                log.info("无需要补偿的订单，任务提前结束");
                return new ReturnT<>(SUCCESS);
            }
            
            // 2. 遍历处理每个订单
            for (ThirdPartyOrderRecordDTO request : compensationList) {
                String bizOrderNo = request.getBizOrderNo();
                log.info("开始处理订单补偿，业务订单号：{}", bizOrderNo);
                
                try {
                    // 查询订单状态
                    OrderDTO orderDTO = orderRepository.order(Long.valueOf(bizOrderNo));
                    if (orderDTO == null) {
                        log.warn("订单不存在，跳过补偿，业务订单号：{}", bizOrderNo);
                        continue;
                    }

                    // 判断是否需要补偿
                    if (orderDTO.getOrderState() == OrderEnum.State.WAIT_DELIVERY) {
                        log.info("订单状态为待发货，执行补偿逻辑，业务订单号：{}，订单状态：{}", bizOrderNo, orderDTO.getOrderState());
                        ThirdPartyOrderProcessor.find().compensation(request);
                        log.info("订单补偿逻辑执行完成，业务订单号：{}", bizOrderNo);
                    }
                    else {
                        log.info("订单状态非待发货，标记为成功，业务订单号：{}，当前订单状态：{}", bizOrderNo, orderDTO.getOrderState());
                        request.setRequestStatus(CommonEnum.RequestStatusEnum.SUCCESS);
                        thirdPartyOrderRepository.saveRecord(request);
                    }
                }
                catch (Exception e) {
                    log.error("处理订单补偿时发生异常，业务订单号：{}", bizOrderNo, e);
                }
            }
            
            log.info("XXL Job任务[createOrder]执行完成，共处理订单数量：{}", compensationList.size());
        }
        catch (Exception e) {
            log.error("XXL Job任务[createOrder]执行失败", e);
            return ReturnT.FAIL;
        }
        return new ReturnT<>(SUCCESS);
    }
    
    @XxlJob("feiShuOrder")
    public ReturnT<String> feiShuOrder(String param) {
        log.info("XXL Job任务[feiShuOrder]开始执行，参数：{}", param);
        // 惠订货平台监控
        sendFeishuNotificationForPlatform(PlatformTypeEnum.HUI_DING_HUO, HuiDingHuoApiUtils.ORDER_CREATE_URL, webhook);
        
        // 乐泰平台监控
        sendFeishuNotificationForPlatform(PlatformTypeEnum.LE_TAI, null, webhook);
        
        log.info("XXL Job任务[feiShuOrder]执行完成");
        
        return new ReturnT<>(SUCCESS);
    }
    
    /**
     * 通用方法：为指定平台发送飞书监控通知
     * 
     * @param platformType 平台类型（如惠订货、乐泰）
     * @param apiUrl 接口URL（用于查询失败订单）
     * @param webhookUrl 飞书机器人Webhook地址
     */
    private void sendFeishuNotificationForPlatform(PlatformTypeEnum platformType, String apiUrl, String webhookUrl) {
        String platformName = platformType.getDescription();
        log.info("开始处理[{}]平台的飞书通知", platformName);
        
        try {
            // 1. 查询该平台的失败订单列表
            List<ThirdPartyOrderRecordDTO> failedOrders =
                    thirdPartyOrderRepository.findByStatusAndNextRetryTimeBefore(platformType, CommonEnum.RequestStatusEnum.FAILED, apiUrl);
            log.info("[{}]平台获取到失败订单总数：{}", platformName, failedOrders.size());
            
            if (CollUtil.isEmpty(failedOrders)) {
                log.info("[{}]平台无失败订单，无需发送飞书消息", platformName);
                return;
            }
            
            // 2. 筛选昨日创建的失败订单（通用时间范围计算）
            LocalDate yesterday = LocalDate.now().minusDays(1);
            LocalDateTime yesterdayStart = yesterday.atStartOfDay();
            LocalDateTime yesterdayEnd = yesterday.plusDays(1).atStartOfDay().minusNanos(1);
            List<ThirdPartyOrderRecordDTO> yesterdayFailedOrders = failedOrders.stream().filter(request -> {
                LocalDateTime createdAt = request.getCreatedAt();
                if (createdAt == null) {
                    log.warn("[{}]平台订单创建时间为空，跳过统计，业务订单号：{}", platformName, request.getBizOrderNo());
                    return false;
                }
                return createdAt.isAfter(yesterdayStart.minusNanos(1)) && createdAt.isBefore(yesterdayEnd.plusNanos(1));
            }).toList();
            int yesterdayFailCount = yesterdayFailedOrders.size();
            log.info("[{}]平台筛选出昨日（{}）创建的失败订单数量：{}", platformName, yesterday, yesterdayFailCount);
            
            // 3. 发送飞书消息
            int totalFailCount = failedOrders.size();
            String updateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            
            log.info("[{}]平台准备发送飞书消息，总失败数：{}，昨日失败数：{}，更新时间：{}",
                platformName,
                totalFailCount,
                yesterdayFailCount,
                updateTime);

            Map<String, Object> cardMap = FeiShuMessageSendUtil.buildFailureStatsCard(platformName, totalFailCount, yesterdayFailCount, updateTime);

            FeiShuMessageSendUtil.sendCardMessage(webhookUrl, cardMap);
            log.info("[{}]平台飞书消息发送成功", platformName);
        }
        catch (Exception e) {
            log.error("[{}]平台处理飞书通知时发生异常", platformName, e);
        }
    }
}