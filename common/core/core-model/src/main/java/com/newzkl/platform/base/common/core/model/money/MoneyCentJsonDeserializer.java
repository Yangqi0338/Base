package com.newzkl.platform.base.common.core.model.money;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Money 分形态反序列化器 — 入参整数分 (如 {@code 115}), 与 {@link MoneyCentJsonSerializer} 对偶
 *
 * <p>用于 DB JSON 列 (MyBatis-Plus {@code JacksonTypeHandler}), 数字一律按分解读;
 * 对外入参按元解读, 见 {@link MoneyJsonDeserializer}</p>
 *
 * @author KC
 */
public class MoneyCentJsonDeserializer extends JsonDeserializer<Money> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Money deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonToken token = p.currentToken();
        if (token == JsonToken.VALUE_NULL) {
            return Money.nullVal();
        }
        if (token == JsonToken.VALUE_NUMBER_INT) {
            return Money.of(p.getLongValue());
        }
        return (Money) ctxt.handleUnexpectedToken(Money.class, p);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Money getNullValue(DeserializationContext ctxt) {
        return Money.nullVal();
    }
}
