package com.newzkl.platform.base.common.core.utils.money;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.newzkl.platform.base.common.core.model.dto.Money;

import java.io.IOException;

/**
 * Money 序列化器：输出 "11.11" 字符串格式，避免 JS Number 精度丢失
 */
public class MoneyJsonSerializer extends JsonSerializer<Money> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(Money value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null || value.isNull()) {
            gen.writeNull();
            return;
        }
        gen.writeString(value.getAmount().toPlainString());
    }
}
