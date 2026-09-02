package com.newzkl.platform.base.common.core.model.money;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Money 分形态序列化器 — 输出整数分 (如 {@code 115}), 与列级 {@code MoneyTypeHandler} 的 BIGINT 口径一致
 *
 * <p>用于 DB JSON 列 (MyBatis-Plus {@code JacksonTypeHandler}), 不用于对外出参:
 * 出参口径是元字符串, 见 {@link MoneyJsonSerializer}</p>
 *
 * @author KC
 */
public class MoneyCentJsonSerializer extends JsonSerializer<Money> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(Money value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null || value.isNull()) {
            gen.writeNull();
            return;
        }
        gen.writeNumber(value.getCent());
    }
}
