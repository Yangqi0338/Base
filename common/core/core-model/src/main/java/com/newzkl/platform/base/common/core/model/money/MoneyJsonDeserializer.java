package com.newzkl.platform.base.common.core.model.money;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Money 反序列化器：接受字符串/数字，转换为 Money 对象
 * @ext 元
 */
public class MoneyJsonDeserializer extends JsonDeserializer<Money> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Money deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonToken token = p.currentToken();
        if (token == JsonToken.VALUE_NULL) {
            return Money.nullVal();
        }

        if (token == JsonToken.VALUE_STRING) {
            String text = p.getText();
            if (StrUtil.isBlank(text)) {
                return Money.nullVal();
            }
            return Money.of(text);
        } else if (token == JsonToken.VALUE_NUMBER_FLOAT || token == JsonToken.VALUE_NUMBER_INT) {
            return Money.of(p.getDecimalValue());
        } else {
            return (Money) ctxt.handleUnexpectedToken(Money.class, p);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Money getNullValue(DeserializationContext ctxt) {
        return Money.nullVal();
    }
}
