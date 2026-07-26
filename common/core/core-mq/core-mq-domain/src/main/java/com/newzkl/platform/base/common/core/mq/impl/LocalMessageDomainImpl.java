package com.newzkl.platform.base.common.core.mq.impl;

import cn.hutool.core.date.DateUtil;
import com.newzkl.platform.base.common.core.mq.LocalMessageDomain;
import com.newzkl.platform.base.common.core.mq.LocalMessageRepository;
import com.newzkl.platform.base.common.core.mq.dto.LocalMessageDTO;
import com.newzkl.platform.base.common.core.mq.enums.MQEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 本地消息领域实现。
 *
 * @author sample
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LocalMessageDomainImpl implements LocalMessageDomain {

    private final LocalMessageRepository localMessageRepository;

    @Override
    public boolean exists(Long localMessageId) {
        return localMessageRepository.exists(localMessageId);
    }

    @Override
    public boolean consume(Long localMessageId) {
        return localMessageRepository.messageConsumeUpdate(localMessageId, MQEnum.ConsumeState.SUCCESS.getCode());
    }

    @Override
    public void consumeFail(Long localMessageId, String errorMsg) {
        localMessageRepository.consumeUpdate(localMessageId, MQEnum.ConsumeState.FAIL.getCode(),
                DateUtil.toLocalDateTime(new Date()), errorMsg);
    }

    @Override
    public Long create(LocalMessageDTO localMessage) {
        return localMessageRepository.localMessageCreate(localMessage);
    }

    @Override
    public List<LocalMessageDTO> listBySendState(MQEnum.SendState sendState, int limit) {
        return localMessageRepository.querySendState(sendState, limit);
    }

    @Override
    public List<LocalMessageDTO> listSendFail() {
        return localMessageRepository.querySendFailMsg();
    }

    @Override
    public void messageSendUpdate(Long id, Integer sendState, LocalDateTime sendTime) {
        localMessageRepository.messageSendUpdate(id, sendState, sendTime);
    }
}
