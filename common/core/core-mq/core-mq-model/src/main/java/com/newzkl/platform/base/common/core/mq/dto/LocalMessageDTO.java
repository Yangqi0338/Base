package com.newzkl.platform.base.common.core.mq.dto;

import com.newzkl.platform.base.common.core.mq.enums.MessageEnum;
import com.newzkl.platform.base.common.ddd.model.dto.BaseDTO;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 本地消息 DTO。
 *
 * <p>字段镜像 LocalMessageDO (去基类 id/time)。</p>
 *
 * @author fang
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LocalMessageDTO extends BaseDTO {

    /**
     * 消息主题。
     */
    private String topic;

    /**
     * 消息标签。
     */
    private String tag;

    /**
     * 外部唯一键。
     * @ext 用于延迟消息唤醒
     */
    private String outKey;

    /**
     * 发送状态。
     */
    private MessageEnum.SendState sendState;

    /**
     * 消息体 JSON。
     */
    private String messageContent;

    /**
     * 消息体类全路径。
     */
    private String messageClass;

    /**
     * RocketMQ 消息 ID。
     */
    private String messageId;

    /**
     * 消费状态。
     * @ext 0-待消费, 1-消费成功, 2-消费失败, 3-异常
     */
    private MessageEnum.ConsumeState consumeState;

    /**
     * 可消费标识。
     * @ext 0-不可消费, 1-可消费
     */
    private CommonEnum.YesOrNo canConsume;

    /**
     * 发送次数。
     */
    private Integer sendCount;

    /**
     * 发送时间。
     */
    private LocalDateTime sendTime;

    /**
     * 消费时间。
     */
    private LocalDateTime consumeTime;

    /**
     * 消费异常信息。
     */
    private String consumeErrorMsg;

    /**
     * 消费异常次数。
     */
    private Integer consumeErrorCount;

    /**
     * 是否可以重试。
     *
     * @param maxRetryCount 最大重试次数
     * @return 是否可重试
     */
    public boolean canRetry(int maxRetryCount) {
        return this.sendCount != null && this.sendCount < maxRetryCount;
    }
}
