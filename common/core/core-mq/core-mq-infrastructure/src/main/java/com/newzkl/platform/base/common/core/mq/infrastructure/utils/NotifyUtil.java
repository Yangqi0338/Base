package com.newzkl.platform.base.common.core.mq.infrastructure.utils;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import com.newzkl.platform.base.common.core.mq.model.notify.NotifyEventCommand;
import com.newzkl.platform.base.common.core.mq.model.notify.NotifyEventMq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 开放平台通知发送工具
 *
 * <p>底层跨系统通知的统一出口, 与 {@link MQUtil} 并列。将 {@link NotifyEventCommand}
 * 包装为 {@link NotifyEventMq} 后经 MQ (topic {@code scm_main}, tag {@code developer:notify}) 投递;
 * 开发者 appId / 回调地址解析与 HTTP 回调由 message 域消费方处理, 本工具不依赖任何业务域。</p>
 *
 * @author KC
 */
@Slf4j
@Component
public class NotifyUtil {

    /**
     * 单条发送开发者通知
     *
     * @param accountId 接收方账户ID
     * @param command   通知事件入参
     */
    public static void singleSend(Long accountId, NotifyEventCommand command) {
        if (accountId == null) {
            return;
        }
        batchSend(Collections.singletonList(accountId), command);
    }

    /**
     * 批量发送开发者通知
     *
     * @param accountIds 接收方账户ID列表
     * @param command    通知事件入参
     */
    public static void batchSend(List<Long> accountIds, NotifyEventCommand command) {
        if (CollUtil.isEmpty(accountIds) || command == null) {
            return;
        }
        NotifyEventMq notifyEventMq = new NotifyEventMq();
        notifyEventMq.setAccountIds(accountIds);
        notifyEventMq.setServiceType(command.getServiceType());
        notifyEventMq.setBusinessType(command.getBusinessType());
        notifyEventMq.setEventContent(command.getEventInfo());
        MQUtil.send(MQ.Tag.DEVELOPER_NOTIFY_EVENT, notifyEventMq);
    }
}
