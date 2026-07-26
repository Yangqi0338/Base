package com.newzkl.platform.base.common.core.mq;

import com.newzkl.platform.base.common.core.mq.dto.LocalMessageDTO;
import com.newzkl.platform.base.common.core.mq.enums.MQEnum;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 本地消息仓储端口。
 *
 * <p>移植自 adopt-chicken {@code ILocalMessageRepository}, 去 I 前缀; 依赖指向本仓模型。</p>
 *
 * @author sample
 */
public interface LocalMessageRepository {

    /**
     * 本地消息创建。
     *
     * @param localMessage 本地消息 DTO
     * @return 新增记录 id
     */
    Long localMessageCreate(LocalMessageDTO localMessage);

    /**
     * 本地消息保存 (id 空 → 新增, 否则更新)。
     *
     * @param localMessage 本地消息 DTO
     * @return 记录 id
     */
    Long localMessageSave(LocalMessageDTO localMessage);

    /**
     * 按 id 查本地消息。
     *
     * @param localMessageId 本地消息 id
     * @return 本地消息 DTO, 无则 null
     */
    LocalMessageDTO localMessageDTO(Long localMessageId);

    /**
     * 本地消息是否存在。
     *
     * @param localMessageId 本地消息 id
     * @return 是否存在
     */
    boolean exists(Long localMessageId);

    /**
     * 查询待重发消息。
     * @ext 15 分钟前发送成功但消费待处理
     *
     * @return 待重发列表
     */
    List<LocalMessageDTO> querySendFailMsg();

    /**
     * 查询指定发送状态的消息列表。
     *
     * @param sendState 发送状态
     * @param limit     限量
     * @return 消息列表
     */
    List<LocalMessageDTO> querySendState(MQEnum.SendState sendState, int limit);

    /**
     * 消息消费修改 (CAS)。
     * @ext 仅当当前 consumeState = WAIT 时才更新, 用于抢占幂等
     *
     * @param id      本地消息 id
     * @param toState 目标消费状态 code
     * @return 是否抢占成功 (受影响行数 &gt; 0)
     */
    boolean messageConsumeUpdate(Long id, Integer toState);

    /**
     * 消息发送状态修改。
     *
     * @param id        本地消息 id
     * @param sendState 发送状态 code
     * @param sendTime  发送时间
     */
    void messageSendUpdate(Long id, Integer sendState, LocalDateTime sendTime);

    /**
     * 消费结果修改。
     *
     * @param id              本地消息 id
     * @param consumeState    消费状态 code
     * @param consumeTime     消费时间
     * @param exceptionString 异常信息
     */
    void consumeUpdate(Long id, Integer consumeState, LocalDateTime consumeTime, String exceptionString);

    /**
     * 按 outKey 修改可消费标识。
     *
     * @param state  可消费标识
     * @param outKey 外部唯一键
     * @return 是否更新成功
     */
    boolean localMessageCanConsumeEditByOutKey(CommonEnum.YesOrNo state, String outKey);
}
