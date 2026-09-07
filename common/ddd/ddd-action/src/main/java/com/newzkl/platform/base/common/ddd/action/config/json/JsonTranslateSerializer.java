package com.newzkl.platform.base.common.ddd.action.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.enums.IEnum;
import com.newzkl.platform.base.common.core.model.properties.SysProperties;

import java.io.IOException;

/**
 * {@link com.newzkl.platform.base.common.core.model.annotation.JsonTranslate} 伴生字段序列化器
 *
 * <p>取值分两段: 先按 index 从多语义描述里取出语义位, 再按 type 指定的来源重写取出的文案</p>
 * <p>原始文案来源: IEnum 取 getValue(index), 普通枚举取 name(), 其他取 toString()</p>
 * <p>由 {@link JsonTranslateSerializerModifier} 按注解取值构造, 注入到 "{字段}Desc" 伴生写出器</p>
 *
 * @author KC
 */
public class JsonTranslateSerializer extends JsonSerializer<Object> {

    /**
     * 语义位下标
     */
    private final int index;

    /**
     * 翻译方式
     */
    private final CommonEnum.TranslateType type;

    /**
     * 构造伴生字段序列化器
     *
     * @param index 语义位下标, 从 0 开始
     * @param type  翻译方式
     */
    public JsonTranslateSerializer(int index, CommonEnum.TranslateType type) {
        this.index = index;
        this.type = type;
    }

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
            text = ie.getValue(index);
        } else if (value instanceof Enum<?> e) {
            text = e.name();
        } else {
            text = value.toString();
        }
        gen.writeString(translate(text));
    }

    /**
     * 按翻译方式重写语义位文案
     *
     * <p>FIX 走 SysProperties 静态替换表; FILE/DB/CACHE 为扩展占位, 暂原样返回</p>
     *
     * @param text 语义位文案
     * @return 重写后的文案
     */
    private String translate(String text) {
        return type == CommonEnum.TranslateType.FIX ? SysProperties.translateText(text) : text;
    }
}
