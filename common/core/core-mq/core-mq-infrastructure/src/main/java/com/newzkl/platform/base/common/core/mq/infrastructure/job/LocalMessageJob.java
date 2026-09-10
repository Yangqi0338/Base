package com.newzkl.platform.base.common.core.mq.infrastructure.job;

import com.newzkl.platform.base.common.core.mq.domain.LocalMessageDomain;
import com.newzkl.platform.base.common.core.mq.infrastructure.producer.AbstractMQProducer;
import com.newzkl.platform.base.common.core.mq.infrastructure.utils.MQUtil;
import com.newzkl.platform.base.common.core.mq.model.dto.LocalMessageDTO;
import com.newzkl.platform.base.common.core.mq.model.enums.MQEnum;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 本地消息扫描发送 / 失败重发定时任务
 *
 * <p>移植自模板 {@code LocalMessageJob}; 基于 Base core-rocketmq {@code MQUtil} 重发。</p>
 *
 * @author fang
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class LocalMessageJob {

    /**
     * 单批扫描量
     */
    private static final int BATCH_SIZE = 100;

    private final LocalMessageDomain localMessageDomain;

    /**
     * 扫描发送 WAIT 状态本地消息
     */
    @XxlJob("sendMessage")
    public void sendMessage() {
        List<LocalMessageDTO> list = localMessageDomain.listBySendState(MQEnum.SendState.WAIT, BATCH_SIZE);
        int success = 0;
        for (LocalMessageDTO m : list) {
            if (resend(m)) {
                success++;
            }
        }
        log.info("sendMessage 完成 total={} success={}", list.size(), success);
    }

    /**
     * 失败消息重发
     * @ext 发送成功但消费待处理超时的消息
     */
    @XxlJob("reSendMessage")
    public void reSendMessage() {
        List<LocalMessageDTO> list = localMessageDomain.listSendFail();
        int success = 0;
        for (LocalMessageDTO m : list) {
            if (resend(m)) {
                success++;
            }
        }
        log.info("reSendMessage 完成 total={} success={}", list.size(), success);
    }

    /**
     * 重发单条本地消息, 由生产者内部复用本地消息 id 落库并回写发送状态
     *
     * @param m 本地消息
     * @return 是否发送成功
     */
    private boolean resend(LocalMessageDTO m) {
        try {
            MQUtil.send(m.getTopic(), m.getTag(), m.getId(), m.getMessageContent());
            return true;
        } catch (Exception e) {
            log.error("重发本地消息失败, id : {}, tag : {}", m.getId(), m.getTag(), e);
            return false;
        }
    }
}
