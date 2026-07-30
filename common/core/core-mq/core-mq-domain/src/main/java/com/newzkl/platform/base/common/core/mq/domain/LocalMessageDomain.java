package com.newzkl.platform.base.common.core.mq.domain;

import com.newzkl.platform.base.common.core.mq.model.dto.LocalMessageDTO;
import com.newzkl.platform.base.common.core.mq.model.enums.MQEnum;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 本地消息领域端口
 *
 * <p>幂等消费编排入口, 供消费者 / 补偿 Job 调用。</p>
 *
 * @author sample
 */
public interface LocalMessageDomain {

    /**
     * 本地消息是否存在
     *
     * @param localMessageId 本地消息 id
     * @return 是否存在
     */
    boolean exists(Long localMessageId);

    /**
     * 消费抢占 (CAS): WAIT → SUCCESS
     *
     * @param localMessageId 本地消息 id
     * @return 是否抢占成功
     */
    boolean consume(Long localMessageId);

    /**
     * 消费失败落库
     *
     * @param localMessageId 本地消息 id
     * @param errorMsg       异常信息
     */
    void consumeFail(Long localMessageId, String errorMsg);

    /**
     * 创建本地消息
     *
     * @param localMessage 本地消息 DTO
     * @return 新增记录 id
     */
    Long create(LocalMessageDTO localMessage);

    /**
     * 拉取指定发送状态的消息列表
     *
     * @param sendState 发送状态
     * @param limit     限量
     * @return 消息列表
     */
    List<LocalMessageDTO> listBySendState(MQEnum.SendState sendState, int limit);

    /**
     * 拉取待重发的消息列表
     *
     * @return 待重发列表
     */
    List<LocalMessageDTO> listSendFail();

    /**
     * 更新发送状态
     *
     * @param id        本地消息 id
     * @param sendState 发送状态 code
     * @param sendTime  发送时间
     */
    void messageSendUpdate(Long id, Integer sendState, LocalDateTime sendTime);
}
