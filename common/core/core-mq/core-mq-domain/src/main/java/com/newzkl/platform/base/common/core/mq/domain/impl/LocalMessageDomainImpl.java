package com.newzkl.platform.base.common.core.mq.domain.impl;

import com.newzkl.platform.base.common.core.mq.domain.LocalMessageDomain;
import com.newzkl.platform.base.common.core.mq.domain.LocalMessageRepository;
import com.newzkl.platform.base.common.core.mq.model.dto.LocalMessageDTO;
import com.newzkl.platform.base.common.core.mq.model.enums.MQEnum;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 本地消息领域实现
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
        return localMessageRepository.messageConsumeUpdate(localMessageId, MQEnum.ConsumeState.SUCCESS, null);
    }

    @Override
    public void consumeFail(Long localMessageId, String errorMsg) {
        localMessageRepository.messageConsumeUpdate(localMessageId, MQEnum.ConsumeState.FAIL, errorMsg);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long preSave(LocalMessageDTO dto) {
        dto.setSendState(MQEnum.SendState.WAIT);
        dto.setConsumeState(MQEnum.ConsumeState.WAIT);
        dto.setCanConsume(CommonEnum.YesOrNo.NO);
        dto.setSendCount(0);
        dto.setConsumeErrorCount(0);
        return localMessageRepository.create(dto);
    }

    @Override
    public List<LocalMessageDTO> listBySendState(MQEnum.SendState sendState, int limit) {
        LocalMessageDTO localMessageDTO = new LocalMessageDTO();
        localMessageDTO.setSendState(sendState);
        localMessageDTO.setCanConsume(CommonEnum.YesOrNo.YES);
        return localMessageRepository.list(localMessageDTO, limit);
    }

    @Override
    public List<LocalMessageDTO> listSendFail() {
        LocalMessageDTO localMessageDTO = new LocalMessageDTO();
        localMessageDTO.setSendState(MQEnum.SendState.SUCCESS);
        localMessageDTO.setConsumeState(MQEnum.ConsumeState.WAIT);
        localMessageDTO.setCanConsume(CommonEnum.YesOrNo.YES);
        localMessageDTO.setSendTime(LocalDateTime.now().minusMinutes(15));
        return localMessageRepository.list(localMessageDTO, null);
    }

    @Override
    public void messageSendUpdate(Long id, MQEnum.SendState sendState) {
        localMessageRepository.messageSendUpdate(id, sendState);
    }

    @Override
    public boolean wakeUp(String outKey) {
        return localMessageRepository.setCanConsumeByOutKey(CommonEnum.YesOrNo.YES, outKey);
    }
}
