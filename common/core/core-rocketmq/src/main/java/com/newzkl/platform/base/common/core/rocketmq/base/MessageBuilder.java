package com.newzkl.platform.base.common.core.rocketmq.base;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.common.core.rocketmq.annotation.MQKey;
import com.newzkl.platform.base.common.core.rocketmq.trace.common.OnsMessageTraceBean;
import com.newzkl.platform.base.common.core.rocketmq.trace.enums.DelayTimeLevel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.Message;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

@Data
@Slf4j
public class MessageBuilder {

    private static final String[] DELAY_ARRAY = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h".split(" ");
    private String topic;
    private String tag;
    private String key;
    private Object message;
    private Integer delayTimeLevel;

    public static MessageBuilder of(String topic, String tag) {
        MessageBuilder builder = new MessageBuilder();
        builder.setTopic(topic);
        builder.setTag(tag);
        return builder;
    }

    public static MessageBuilder of(Object message) {
        MessageBuilder builder = new MessageBuilder();
        builder.setMessage(message);
        return builder;
    }

    public MessageBuilder topic(String topic) {
        this.topic = topic;
        return this;
    }

    public MessageBuilder tag(String tag) {
        this.tag = tag;
        return this;
    }

    public MessageBuilder key(String key) {
        this.key = key;
        return this;
    }

    public MessageBuilder delayTimeLevel(DelayTimeLevel delayTimeLevel) {
        this.delayTimeLevel = delayTimeLevel.getLevel();
        return this;
    }

    public Message build() {
        String messageKey = "";
        try {
            Field[] fields = ReflectUtil.getFields(message.getClass());
            for (Field field : fields) {
                MQKey mqKey = AnnotationUtil.getAnnotation(field, MQKey.class);
                String key = field.get(message).toString();
                String prefix = mqKey.prefix();
                messageKey = StrUtil.isEmpty(prefix) ? key : (prefix + key);
            }
        } catch (Exception e) {
            log.error("parse key error : {}", e.getMessage());
        }
        String str = JSONUtil.toJsonStr(message);
        if (StringUtils.isEmpty(topic)) {
            if (StringUtils.isEmpty(getTopic())) {
                throw new RuntimeException("no topic defined to send this message");
            }
        }
        Message message = new Message(topic, str.getBytes(StandardCharsets.UTF_8));
        if (!StringUtils.isEmpty(tag)) {
            message.setTags(tag);
        }
        if (StringUtils.isNotEmpty(messageKey)) {
            message.setKeys(messageKey);
        }
        if (delayTimeLevel != null && delayTimeLevel > 0 && delayTimeLevel <= DELAY_ARRAY.length) {
            message.setDelayTimeLevel(delayTimeLevel);
        }
        OnsMessageTraceBean.producerTrace(message);
        return message;
    }


}
