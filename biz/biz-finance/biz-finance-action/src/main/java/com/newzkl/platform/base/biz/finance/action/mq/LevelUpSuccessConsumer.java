package com.newzkl.platform.base.biz.finance.action.mq;

import com.newzkl.platform.base.biz.finance.domain.adapt.api.EarningApi;
import com.newzkl.platform.base.biz.finance.model.event.LevelUpSuccessEvent;
import com.newzkl.platform.base.common.core.mq.infrastructure.annotation.MQConsumer;
import com.newzkl.platform.base.common.core.mq.infrastructure.consumer.AbstractMessageMQPushConsumer;
import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 账户升级成功消费者
 *
 * <p>迁移自旧 scm-message {@code LevelUpSuccessConsumer}, 账户升级后更新分润记录角色</p>
 *
 * @author KC
 */
@Slf4j
@MQConsumer(consumerGroup = MQ.Tag.LEVEL_UP_SUCCESS_MESSAGE, tag = MQ.Tag.LEVEL_UP_SUCCESS)
public class LevelUpSuccessConsumer extends AbstractMessageMQPushConsumer<LevelUpSuccessEvent> {

    @Autowired
    private EarningApi earningApi;

    /**
     * 处理账户升级成功事件
     *
     * @param message 账户升级成功事件
     * @param extMap  消息扩展参数
     */
    @Override
    public void remoteProcess(LevelUpSuccessEvent message, Map<String, Object> extMap) {
        log.info("账户升级成功消费, accountId: {}, newRoleId: {}", message.getAccountId(), message.getNewRoleId());
        earningApi.updateRecordRole(message.getAccountId(), message.getNewRoleId());
    }
}
