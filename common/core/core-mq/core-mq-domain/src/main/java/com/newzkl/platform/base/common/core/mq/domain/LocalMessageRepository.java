package com.newzkl.platform.base.common.core.mq.domain;

import com.newzkl.platform.base.common.core.mq.model.dto.LocalMessageDTO;
import com.newzkl.platform.base.common.core.mq.model.enums.MQEnum;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 本地消息仓储端口
 *
 * <p>移植自模板 {@code ILocalMessageRepository}, 去 I 前缀; 依赖指向本仓模型。</p>
 *
 * @author sample
 */
public interface LocalMessageRepository {

    /**
     * 本地消息创建
     *
     * @param localMessage 本地消息 DTO
     * @return 新增记录 id
     */
    Long create(LocalMessageDTO localMessage);

    /**
     * 按 id 查本地消息
     *
     * @param localMessageId 本地消息 id
     * @return 本地消息 DTO, 无则 null
     */
    LocalMessageDTO localMessageDTO(Long localMessageId);

    /**
     * 本地消息是否存在
     *
     * @param localMessageId 本地消息 id
     * @return 是否存在
     */
    boolean exists(Long localMessageId);

    /**
     * 查询列表
     *
     * @return 待重发列表
     */
    List<LocalMessageDTO> list(LocalMessageDTO dto, Integer limit);

    /**
     * 消息消费修改 (CAS)
     * @ext 仅当当前 consumeState = WAIT 时才更新, 用于抢占幂等
     *
     * @param id      本地消息 id
     * @param toState 目标消费状态 code
     * @return 是否抢占成功 (受影响行数 &gt; 0)
     */
    boolean messageConsumeUpdate(Long id, MQEnum.ConsumeState toState, String errorMsg);

    /**
     * 消息发送状态修改
     *
     * @param id        本地消息 id
     * @param sendState 发送状态 code
     * @param sendTime  发送时间
     */
    void messageSendUpdate(Long id, MQEnum.SendState sendState);

    /**
     * 按 outKey 修改可消费标识
     *
     * @param state  可消费标识
     * @param outKey 外部唯一键
     * @return 是否更新成功
     */
    boolean setCanConsumeByOutKey(CommonEnum.YesOrNo state, String outKey);
}
