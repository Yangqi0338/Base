package com.newzkl.platform.base.common.core.mq.infrastructure.repository;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.newzkl.platform.base.common.core.mq.domain.LocalMessageRepository;
import com.newzkl.platform.base.common.core.mq.infrastructure.dao.LocalMessageDAO;
import com.newzkl.platform.base.common.core.mq.model.dto.LocalMessageDTO;
import com.newzkl.platform.base.common.core.mq.infrastructure.entity.LocalMessageDO;
import com.newzkl.platform.base.common.core.mq.model.enums.MQEnum;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaUpdateWrapper;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;

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
    public Long create(LocalMessageDTO localMessage) {
        LocalMessageDO localMessageDO = TransferUtils.transfer(localMessage, LocalMessageDO.class);
        localMessageDAO.insert(localMessageDO);
        return localMessageDO.getId();
    }

    @Override
    public LocalMessageDTO localMessageDTO(Long localMessageId) {
        LocalMessageDO localMessageDO = localMessageDAO.selectById(localMessageId);
        return TransferUtils.transfer(localMessageDO, LocalMessageDTO.class);
    }

    @Override
    public boolean exists(Long localMessageId) {
        return localMessageDAO.exists(new LambdaQueryWrapper<LocalMessageDO>()
                .eq(LocalMessageDO::getId, localMessageId));
    }

    public List<LocalMessageDTO> list(LocalMessageDTO dto, Integer limit) {
        // 查询15分钟前发送成功但消费待处理的消息
        List<LocalMessageDO> list = localMessageDAO.selectPage(page(1, Opt.ofNullable(limit).orElse(Integer.MAX_VALUE))
                ,localMessageDAO.getLw(dto)).getRecords();
        return TransferUtils.transfers(list, LocalMessageDTO.class);
    }

    @Override
    public boolean messageConsumeUpdate(Long id, MQEnum.ConsumeState toState, String errorMsg) {
        LambdaUpdateWrapper<LocalMessageDO> wrapper = new BaseLambdaUpdateWrapper<LocalMessageDO>()
                .setIncrBy(StrUtil.isNotBlank(errorMsg), LocalMessageDO::getConsumeErrorCount, 1)
                .notEmptySet(LocalMessageDO::getConsumeState, toState)
                .set(MQEnum.ConsumeState.WAIT != toState, LocalMessageDO::getConsumeTime, LocalDateTime.now())
                .eq(LocalMessageDO::getId, id)
                .eq(LocalMessageDO::getConsumeState, MQEnum.ConsumeState.WAIT)
                .eq(LocalMessageDO::getCanConsume, CommonEnum.YesOrNo.YES)
                ;
        return localMessageDAO.update(wrapper) > 0;
    }

    @Override
    public void messageSendUpdate(Long id, MQEnum.SendState sendState) {
        localMessageDAO.update(new BaseLambdaUpdateWrapper<LocalMessageDO>()
                .setIncrCount(LocalMessageDO::getSendCount)
                .set(LocalMessageDO::getSendState, sendState)
                .set(LocalMessageDO::getSendTime, LocalDateTime.now())
                .set(LocalMessageDO::getCanConsume, CommonEnum.YesOrNo.YES)
                .eq(LocalMessageDO::getId, id)
        );
    }

    @Override
    public boolean setCanConsumeByOutKey(CommonEnum.YesOrNo state, String outKey) {
        return localMessageDAO.update(new BaseLambdaUpdateWrapper<LocalMessageDO>()
                .set(LocalMessageDO::getCanConsume, state)
                .eq(LocalMessageDO::getOutKey, outKey)
        ) > 0;
    }
}
