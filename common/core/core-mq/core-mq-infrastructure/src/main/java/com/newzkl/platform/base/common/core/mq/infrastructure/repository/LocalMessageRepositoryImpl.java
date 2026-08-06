package com.newzkl.platform.base.common.core.mq.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.newzkl.platform.base.common.core.mq.domain.LocalMessageRepository;
import com.newzkl.platform.base.common.core.mq.infrastructure.dao.LocalMessageDAO;
import com.newzkl.platform.base.common.core.mq.model.dto.LocalMessageDTO;
import com.newzkl.platform.base.common.core.mq.infrastructure.entity.LocalMessageDO;
import com.newzkl.platform.base.common.core.mq.model.enums.MQEnum;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 本地消息仓储实现
 *
 * <p>移植自模板 {@code LocalMessageRepositoryImpl}; 用 hutool BeanUtil 完成 DTO&lt;-&gt;DO 转换。</p>
 *
 * @author sample
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class LocalMessageRepositoryImpl extends RepositorySupport implements LocalMessageRepository {

    private final LocalMessageDAO localMessageDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long localMessageCreate(LocalMessageDTO localMessage) {
        LocalMessageDO localMessageDO = TransferUtils.transfer(localMessage, LocalMessageDO.class);
        if (localMessageDO.getConsumeState() == null) {
            localMessageDO.setConsumeState(MQEnum.ConsumeState.WAIT);
        }
        if (localMessageDO.getSendCount() == null) {
            localMessageDO.setSendCount(0);
        }
        if (localMessageDO.getConsumeErrorCount() == null) {
            localMessageDO.setConsumeErrorCount(0);
        }
        if (localMessageDO.getCanConsume() == null) {
            localMessageDO.setCanConsume(CommonEnum.YesOrNo.YES);
        }
        localMessageDAO.insert(localMessageDO);
        return localMessageDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long localMessageSave(LocalMessageDTO localMessage) {
        if (localMessage.getId() == null || localMessage.getId() == 0) {
            return localMessageCreate(localMessage);
        }
        LocalMessageDO localMessageDO = TransferUtils.transfer(localMessage, LocalMessageDO.class);
        localMessageDAO.updateById(localMessageDO);
        return localMessage.getId();
    }

    @Override
    public LocalMessageDTO localMessageDTO(Long localMessageId) {
        LocalMessageDO localMessageDO = localMessageDAO.selectById(localMessageId);
        return TransferUtils.transfer(localMessageDO, LocalMessageDTO.class);
    }

    @Override
    public boolean exists(Long localMessageId) {
        if (localMessageId == null) {
            return false;
        }
        return localMessageDAO.exists(new BaseLambdaQueryWrapper<LocalMessageDO>()
                .notEmptyEq(LocalMessageDO::getId, localMessageId));
    }

    @Override
    public List<LocalMessageDTO> querySendFailMsg() {
        // 查询15分钟前发送成功但消费待处理的消息
        List<LocalMessageDO> list = localMessageDAO.selectList(new LambdaQueryWrapper<LocalMessageDO>()
                .eq(LocalMessageDO::getSendState, MQEnum.SendState.SUCCESS)
                .eq(LocalMessageDO::getConsumeState, MQEnum.ConsumeState.WAIT)
                .eq(LocalMessageDO::getConsumeErrorCount, 0)
                .lt(LocalMessageDO::getSendTime, LocalDateTime.now().minusMinutes(15))
        );
        return TransferUtils.transfers(list, LocalMessageDTO.class);
    }

    @Override
    public List<LocalMessageDTO> querySendState(MQEnum.SendState sendState, int limit) {
        List<LocalMessageDO> list = localMessageDAO.selectList(localMessageDAO.getLw(sendState)
                .last("LIMIT " + Math.max(1, limit)));
        return TransferUtils.transfers(list, LocalMessageDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean messageConsumeUpdate(Long id, Integer toState) {
        LambdaUpdateWrapper<LocalMessageDO> wrapper = new LambdaUpdateWrapper<LocalMessageDO>()
                .set(LocalMessageDO::getConsumeState, toState)
                .eq(LocalMessageDO::getId, id)
                .eq(LocalMessageDO::getConsumeState, MQEnum.ConsumeState.WAIT);
        // 终态(成功/失败/异常)回写消费时间
        if (!MQEnum.ConsumeState.WAIT.getCode().equals(toState)) {
            wrapper.set(LocalMessageDO::getConsumeTime, LocalDateTime.now());
        }
        return localMessageDAO.update(wrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void messageSendUpdate(Long id, Integer sendState, LocalDateTime sendTime) {
        localMessageDAO.update(new LambdaUpdateWrapper<LocalMessageDO>()
                .set(LocalMessageDO::getSendState, sendState)
                .set(LocalMessageDO::getSendTime, sendTime)
                .setSql("send_count = send_count + 1")
                .eq(LocalMessageDO::getId, id)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void consumeUpdate(Long id, Integer consumeState, LocalDateTime consumeTime, String exceptionString) {
        localMessageDAO.update(new LambdaUpdateWrapper<LocalMessageDO>()
                .set(LocalMessageDO::getConsumeState, consumeState)
                .set(LocalMessageDO::getConsumeTime, consumeTime)
                .set(LocalMessageDO::getConsumeErrorMsg, exceptionString)
                .setSql("consume_error_count = consume_error_count + 1")
                .eq(LocalMessageDO::getId, id)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean localMessageCanConsumeEditByOutKey(CommonEnum.YesOrNo state, String outKey) {
        return localMessageDAO.update(new LambdaUpdateWrapper<LocalMessageDO>()
                .set(LocalMessageDO::getCanConsume, state)
                .eq(LocalMessageDO::getOutKey, outKey)
        ) > 0;
    }
}
