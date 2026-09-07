package com.newzkl.platform.base.common.ddd.action.config.json;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.newzkl.platform.base.common.core.model.annotation.JsonTranslate;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link JsonTranslate} 字段序列化修饰器
 *
 * <p>字段/getter 标 {@link JsonTranslate} 时追加同名 + "Desc" 伴生写出器, 原字段保留原样输出。
 * 字段名本身已以 Desc 结尾则直接改写其序列化器, 不再叠加</p>
 * <p>注解上的 index 与 type 在此处读出, 构造注入 {@link JsonTranslateSerializer}</p>
 *
 * @author KC
 */
public class JsonTranslateSerializerModifier extends BeanSerializerModifier {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config,
                                                     BeanDescription beanDesc,
                                                     List<BeanPropertyWriter> beanProperties) {
        List<BeanPropertyWriter> result = new ArrayList<>(beanProperties);
        for (BeanPropertyWriter writer : beanProperties) {
            JsonTranslate ann = writer.getAnnotation(JsonTranslate.class);
            if (ann == null) {
                continue;
            }
            JsonTranslateSerializer serializer = new JsonTranslateSerializer(ann.index(), ann.type());
            if (writer.getName().endsWith("Desc")) {
                writer.assignSerializer(serializer);
                continue;
            }
            BeanPropertyWriter descWriter = writer.rename(NameTransformer.simpleTransformer("", "Desc"));
            descWriter.assignSerializer(serializer);
            result.add(descWriter);
        }
        return result;
    }
}
