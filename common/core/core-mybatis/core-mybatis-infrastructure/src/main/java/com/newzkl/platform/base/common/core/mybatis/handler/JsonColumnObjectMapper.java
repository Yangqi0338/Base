package com.newzkl.platform.base.common.core.mybatis.handler;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.newzkl.platform.base.common.core.model.json.JsonTimeModule;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.model.money.MoneyCentJsonDeserializer;
import com.newzkl.platform.base.common.core.model.money.MoneyCentJsonSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * DB JSON 列专用 ObjectMapper — 所有 {@code @JsonSerializable} 字段经 MyBatis-Plus {@code JacksonTypeHandler} 走此实例
 *
 * <p>MP 只认 {@code JacksonTypeHandler} 上那个静态 {@code OBJECT_MAPPER}: {@code TableFieldInfo} 对
 * {@code IJsonTypeHandler} 走 {@code MybatisUtils.newJsonTypeHandler(...)} 直接 new 实例、绕过
 * {@code TypeHandlerRegistry}, 且把 handler 类名烧进 {@code #{...}} 的写 SQL, 所以换 handler 类要改遍
 * 每个 DO 的注解; 行为一律收在本类, 业务侧零改动</p>
 *
 * <p>五项与默认 {@code new ObjectMapper()} 的差异:</p>
 * <ol>
 *     <li>{@link Money} 按<b>分</b>落库 ({@link MoneyCentJsonSerializer}), 与列级 {@link MoneyTypeHandler}
 *     的 BIGINT 口径对齐。默认反射序列化会写成 {@code {cent,null,zero,amount,currency,centFactor}},
 *     回读时 null/zero/centFactor 无 setter 直接抛 {@code UnrecognizedPropertyException} 打死整个列表接口。
 *     对外出参仍是元字符串, 由 Web 侧 ObjectMapper 独立装配, 两者互不影响</li>
 *     <li>{@link JsonTimeModule} 提供 JSR-310 支持: 裸 ObjectMapper 连 {@code JavaTimeModule} 都没有,
 *     JSON 列里任何 {@code LocalDateTime} 字段 (如 {@code BaseRes.createTime}) 一写就抛
 *     {@code InvalidDefinitionException}。格式与 Web 出参共用 {@code yyyy-MM-dd HH:mm:ss} ——
 *     旧库 JSON 列由 fastjson 写出正是此格式, 换 ISO 解析器会把迁移过来的存量行读成 null</li>
 *     <li>关掉 {@code FAIL_ON_UNKNOWN_PROPERTIES}: JSON 列是内部存储, DO 删字段后存量行仍带旧 key,
 *     不容错会让历史数据读不出来</li>
 *     <li>{@link JsonInclude.Include#NON_NULL}: 成员为 null 不写进 JSON, 存量列不再堆 {@code "x":null}。
 *     注意整字段 null 本就不落库 (MP 默认 {@code FieldStrategy.NOT_NULL} 不拼该列,
 *     且 {@code BaseTypeHandler.setParameter} 对 null 走 {@code ps.setNull});
 *     {@code Money.nullVal()} 是非 null 引用, 仍写 JSON null (Include 只看 Java 引用)</li>
 *     <li>类型不匹配返回 null 而非抛异常, 见下</li>
 * </ol>
 *
 * <p>容错分两级, 都只对<b>读</b>生效 —— 写失败仍抛, 静默丢数据比报错更糟:</p>
 * <ul>
 *     <li><b>字段级</b> ({@link NullOnMismatchHandler}): 单个成员类型不匹配只让该成员 null,
 *     同对象其余成员照常填充</li>
 *     <li><b>整列级</b> (覆写 {@link #readValue(String, JavaType)}): 坏 JSON、根节点结构完全不匹配
 *     等字段级救不回来的情形, 整列按 null 处理。{@code JacksonTypeHandler.parse} 原本
 *     {@code throw new RuntimeException(e)}, 一行脏数据即打死整个列表接口</li>
 * </ul>
 *
 * @author KC
 */
@Slf4j
public class JsonColumnObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = 1L;

    /**
     * 告警日志里 JSON 片段的最大长度, 超出截断
     */
    private static final int LOG_JSON_MAX_LENGTH = 500;

    /**
     * 装配 Money 分口径模块、JSR-310 时间模块、未知字段容错、null 成员不落库、类型不匹配降级
     */
    public JsonColumnObjectMapper() {
        SimpleModule module = new SimpleModule("moneyCentModule");
        module.addSerializer(Money.class, new MoneyCentJsonSerializer());
        module.addDeserializer(Money.class, new MoneyCentJsonDeserializer());
        registerModule(module);
        registerModule(new JsonTimeModule());
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        setSerializationInclusion(JsonInclude.Include.NON_NULL);
        addHandler(new NullOnMismatchHandler());
    }

    /**
     * 整列兜底 — 字段级降级救不回来时整列按 null 处理, 不向上抛
     * @ext {@code JacksonTypeHandler.parse} 调的正是本重载, {@code readValue(String, Class)}
     * 与 {@code readValue(String, TypeReference)} 均内部委托到此, 一处覆写即全覆盖
     * @param content   列里的 JSON 文本
     * @param valueType 目标类型
     * @return 反序列化结果, 无法解析时 null
     */
    @Override
    public <T> T readValue(String content, JavaType valueType) {
        try {
            return super.readValue(content, valueType);
        } catch (JacksonException e) {
            log.warn("JSON 列反序列化失败, 整列按 null 处理: targetType={}, json={}",
                    valueType, StrUtil.maxLength(content, LOG_JSON_MAX_LENGTH), e);
            return null;
        }
    }

    /**
     * 字段级降级 — 成员类型与 JSON 不匹配时只把该成员置 null, 保住同对象其余成员
     * @ext 典型来源: DO 字段类型改过 (String → VO / Integer → 枚举) 而存量行仍是旧形态
     */
    private static class NullOnMismatchHandler extends DeserializationProblemHandler {

        /**
         * token 形态不匹配 (期望对象却是标量、期望数组却是对象等)
         * @ext 必须 {@code skipChildren} 吃掉整个子结构, 否则解析游标停在半路会串位
         */
        @Override
        public Object handleUnexpectedToken(DeserializationContext ctxt, JavaType targetType,
                                            JsonToken t, JsonParser p, String failureMsg) throws IOException {
            log.warn("JSON 列字段形态不匹配, 该字段按 null 处理: targetType={}, token={}, reason={}",
                    targetType, t, failureMsg);
            p.skipChildren();
            return null;
        }

        /**
         * 字符串值转不成目标类型 (如 {@code "abc"} 给 Long、非法枚举名)
         */
        @Override
        public Object handleWeirdStringValue(DeserializationContext ctxt, Class<?> targetType,
                                            String valueToConvert, String failureMsg) {
            log.warn("JSON 列字段值转换失败, 该字段按 null 处理: targetType={}, value={}, reason={}",
                    targetType, StrUtil.maxLength(valueToConvert, LOG_JSON_MAX_LENGTH), failureMsg);
            return null;
        }

        /**
         * 数字值转不成目标类型 (如枚举 ordinal 越界)
         */
        @Override
        public Object handleWeirdNumberValue(DeserializationContext ctxt, Class<?> targetType,
                                            Number valueToConvert, String failureMsg) {
            log.warn("JSON 列字段值转换失败, 该字段按 null 处理: targetType={}, value={}, reason={}",
                    targetType, valueToConvert, failureMsg);
            return null;
        }

        /**
         * 目标类型没有能吃下当前 JSON 形态的构造器 (如存量行是 {@code "某公司"}, 字段已改成 VO)
         */
        @Override
        public Object handleMissingInstantiator(DeserializationContext ctxt, Class<?> instClass,
                                                ValueInstantiator valueInsta, JsonParser p, String msg) throws IOException {
            log.warn("JSON 列字段无匹配构造器, 该字段按 null 处理: targetType={}, reason={}", instClass, msg);
            p.skipChildren();
            return null;
        }
    }
}
