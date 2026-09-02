package com.newzkl.platform.base.common.ddd.action.config.json;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.newzkl.platform.base.common.core.model.annotation.JsonTranslate;
import com.newzkl.platform.base.common.core.model.enums.EnumDeserializerModifier;
import com.newzkl.platform.base.common.core.model.json.JsonTimeModule;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.model.money.MoneyJsonDeserializer;
import com.newzkl.platform.base.common.core.model.money.MoneyJsonSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 全局 JSON 出入参装配
 *
 * <p>统一以 {@link Module} bean 注册: Spring Boot 的
 * {@code StandardJackson2ObjectMapperBuilderCustomizer} 汇总容器内全部 Module bean 后一次性
 * {@code modulesToInstall}。不可自写 {@code Jackson2ObjectMapperBuilderCustomizer} 调
 * {@code builder.modulesToInstall(...)}: 该方法非加法, 多个 customizer 相互覆盖</p>
 *
 * <p>不重复配置 Boot 已有默认: {@code FAIL_ON_UNKNOWN_PROPERTIES}/{@code DEFAULT_VIEW_INCLUSION}
 * 默认已关, {@code WRITE_DATES_AS_TIMESTAMPS} 默认已关,
 * {@code Jdk8Module}/{@code ParameterNamesModule}/{@code JavaTimeModule} 由
 * {@code Jackson2ObjectMapperBuilder.registerWellKnownModulesIfAvailable} 自动注册</p>
 *
 * @author KC
 */
@Configuration
public class JacksonConfig {

    /**
     * 枚举反序列化模块 — 按 name/{@code IEnum#getCode()} 匹配, 未命中返回 null 交 validation 兜底
     *
     * <p>替换 Jackson 默认的数字按 ordinal 索引行为 (越界直接抛 {@code InvalidFormatException} → 400)。
     * 集合元素无需专门装配: {@code CollectionDeserializer} 复用此处产出的元素反序列化器</p>
     *
     * @return Jackson 模块
     */
    @Bean
    public Module enumDeserializeModule() {
        SimpleModule module = new SimpleModule("enumDeserializeModule");
        module.setDeserializerModifier(new EnumDeserializerModifier());
        return module;
    }

    /**
     * {@link Money} 序列化模块 — 出参为元为单位两位小数字符串 (如 {@code "11.11"}), 入参接受字符串/数字
     *
     * <p>不接线则出参回落 Jackson 默认反射输出 {@code {cent,amount,currency...}}, 内部结构外泄</p>
     *
     * @return Jackson 模块
     */
    @Bean
    public Module moneyJacksonModule() {
        SimpleModule module = new SimpleModule("moneyJacksonModule");
        module.addSerializer(Money.class, new MoneyJsonSerializer());
        module.addDeserializer(Money.class, new MoneyJsonDeserializer());
        return module;
    }

    /**
     * {@link JsonTranslate} 伴生字段模块 — 标注字段追加同名 + "Desc" 文案输出
     *
     * @return Jackson 模块
     */
    @Bean
    public Module jsonTranslateModule() {
        SimpleModule module = new SimpleModule("jsonTranslateModule");
        module.setSerializerModifier(new JsonTranslateSerializerModifier());
        return module;
    }

    /**
     * JSR-310 时间格式模块 — 对齐旧线上 {@code yyyy-MM-dd HH:mm:ss} 契约, 覆盖 Boot 默认 ISO-8601
     *
     * @return Jackson 模块
     */
    @Bean
    public Module jsonTimeModule() {
        return new JsonTimeModule();
    }

    /**
     * {@code @JsonUnwrapped(prefix=...)} 驼峰模块 — 平展字段输出 {@code prefix + 首字母大写字段名}
     *
     * <p>不接线则 Jackson 原生按纯字符串拼接输出 {@code issuername}, 违反前端驼峰契约</p>
     *
     * @return Jackson 模块
     */
    @Bean
    public Module jsonUnwrappedCamelModule() {
        return new JsonUnwrappedCamelModule();
    }

    /**
     * Long 转字符串模块 — 出参 {@link Long} 一律写成字符串
     *
     * <p>雪花 id ({@code SnowflakeGenerator}) 为 19 位, 超出 JS Number 安全整数
     * {@code 2^53-1} (16 位), 浏览器收到后被转科学计数法/尾数截断, 回传即对不上原值</p>
     *
     * <p>只转包装类 {@link Long}, 不转基本类型 {@code long}: 业务 DTO 的 id/外键一律用 Long 包装,
     * 而 {@code long} 多用于 {@code Page.total}、计数、耗时这类小值域字段,
     * 转成字符串会破坏前端分页组件与数值运算。如需连基本类型一起转,
     * 追加 {@code addSerializer(Long.TYPE, ToStringSerializer.instance)}</p>
     *
     * @return Jackson 模块
     */
    @Bean
    public Module longToStringModule() {
        SimpleModule module = new SimpleModule("longToStringModule");
        module.addSerializer(Long.class, ToStringSerializer.instance);
        return module;
    }
}
