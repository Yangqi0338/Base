package com.newzkl.platform.base.common.ddd.action.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.newzkl.platform.base.common.core.model.enums.IEnum;

import java.io.IOException;

/**
 * {@link com.newzkl.platform.base.common.core.model.annotation.JsonTranslate} 伴生字段序列化器
 *
 * <p>从字段值提取可读文案: IEnum 取 getValue(), 普通枚举取 name(), 其他取 toString()。
 * 由 {@link JsonTranslateSerializerModifier} 注入到 "{字段}Desc" 伴生写出器</p>
 *
 * @author KC
 */
public class JsonTranslateSerializer extends JsonSerializer<Object> {

    /**
     * 序列化伴生 Desc 字段
     *
     * @param value       原字段值
     * @param gen         JSON 生成器
     * @param serializers 序列化上下文
     * @throws IOException 写出异常
     */
    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        String text;
        if (value instanceof IEnum<?> ie) {
            text = ie.getValue();
        } else if (value instanceof Enum<?> e) {
            text = e.name();
        } else {
            text = value.toString();
        }
        gen.writeString(text);
    }
}
