package com.newzkl.platform.base.common.core.mq.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mq.model.enums.MQEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.handler.RawJsonStringTypeHandler;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.ColumnType;
import org.dromara.autotable.annotation.Index;
import org.dromara.autotable.annotation.mysql.MysqlTypeConstant;
import org.dromara.mpe.autofill.annotation.DefaultValue;
import org.dromara.mpe.autofill.annotation.JsonSerializable;

import java.time.LocalDateTime;

/**
 * 本地消息实体
 *
 * @author fang
 */
@Data
@TableName("local_message")
@EqualsAndHashCode(callSuper = true)
public class LocalMessageDO extends BaseDO {

    /**
     * 消息主题
     */
    @NotNull
    @Index
    @Size(max = 50)
    private String topic;

    /**
     * 消息标签
     */
    @NotNull
    @Index
    @Size(max = 255)
    private String tag;

    /**
     * 外部唯一键
     * @ext 用于延迟消息唤醒
     */
    @Size(max = 255)
    private String outKey;

    /**
     * 发送状态
     */
    @NotNull
    @Index
    private MQEnum.SendState sendState;

    /**
     * 消息体
     */
    @NotNull
    @JsonSerializable(typeHandler = RawJsonStringTypeHandler.class)
    private String messageContent;

    /**
     * 消息体类全路径
     */
    @NotNull
    private String messageClass;

    /**
     * RocketMQ 消息 ID
     */
    @Size(max = 50)
    private String messageId;

    /**
     * 消费状态
     * @ext 0-待消费, 1-消费成功, 2-消费失败, 3-异常
     */
    @NotNull
    @Index
    private MQEnum.ConsumeState consumeState;

    /**
     * 可消费标识
     * @ext 0-不可消费, 1-可消费
     */
    @DefaultValue("0")
    @NotNull
    private CommonEnum.YesOrNo canConsume;

    /**
     * 发送次数
     */
    @DefaultValue("0")
    @NotNull
    private Integer sendCount;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 消费时间
     */
    @Index
    private LocalDateTime consumeTime;

    /**
     * 消费异常信息
     */
    @ColumnType(value = MysqlTypeConstant.TEXT)
    private String consumeErrorMsg;

    /**
     * 消费异常次数
     */
    @DefaultValue("0")
    @NotNull
    private Integer consumeErrorCount;
}
