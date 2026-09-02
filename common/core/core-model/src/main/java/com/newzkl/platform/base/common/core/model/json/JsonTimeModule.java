package com.newzkl.platform.base.common.core.model.json;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.PackageVersion;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * JSR-310 时间格式模块
 *
 * <p>Spring Boot 默认 {@code JavaTimeModule} 输出 ISO-8601 ({@code 2026-08-29T11:13:53}),
 * 与前端既有契约 ({@code yyyy-MM-dd HH:mm:ss}) 不符。本模块覆写三类时间的 ser/deser,
 * 格式与旧线上 {@code ScmTimeModule} 逐字对齐</p>
 *
 * <p>落 core-model 是因为两个边界共用同一格式: Web 出入参 (由 {@code JacksonConfig} 注册) 与
 * DB JSON 列 (由 {@code JsonColumnObjectMapper} 注册)。JSON 列同用此格式的理由:
 * 旧库 JSON 列由 fastjson {@code JSON.toJSONString} 写出, LocalDateTime 正是
 * {@code yyyy-MM-dd HH:mm:ss}, 换 ISO 解析器会让迁移过来的存量行整片读成 null</p>
 *
 * @author KC
 */
public class JsonTimeModule extends SimpleModule {

    /**
     * 日期时间格式
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 日期格式
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 时间格式
     */
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * 装配三类时间的双向格式化器
     */
    public JsonTimeModule() {
        super(PackageVersion.VERSION);
        addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATE_TIME_FORMATTER));
        addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DATE_TIME_FORMATTER));
        addSerializer(LocalDate.class, new LocalDateSerializer(DATE_FORMATTER));
        addDeserializer(LocalDate.class, new LocalDateDeserializer(DATE_FORMATTER));
        addSerializer(LocalTime.class, new LocalTimeSerializer(TIME_FORMATTER));
        addDeserializer(LocalTime.class, new LocalTimeDeserializer(TIME_FORMATTER));
    }
}
