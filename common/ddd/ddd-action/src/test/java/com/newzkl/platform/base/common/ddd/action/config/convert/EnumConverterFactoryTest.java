package com.newzkl.platform.base.common.ddd.action.config.convert;

import com.newzkl.platform.base.common.core.model.enums.SmsEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * {@link EnumConverterFactory} query/form 枚举转换回归测试
 *
 * <p>覆盖 Spring 内置 {@code StringToEnumConverterFactory} 只认 name 的缺口:
 * {@code @RequestParam} 传 code 时原本抛 {@code ConversionFailedException} → 400</p>
 *
 * @author KC
 */
class EnumConverterFactoryTest {

    /**
     * 注册了枚举转换工厂的转换服务
     */
    private FormattingConversionService conversionService;

    /**
     * 每例前重建转换服务, 注册方式对齐 {@link WebConvertConfig#addFormatters}
     */
    @BeforeEach
    void setUp() {
        DefaultFormattingConversionService service = new DefaultFormattingConversionService();
        new WebConvertConfig().addFormatters(service);
        conversionService = service;
    }

    @Test
    @DisplayName("query 传数字 code 命中枚举 (Spring 内置做不到)")
    void numberCodeMatched() {
        assertEquals(SmsEnum.Type.SUPPLIER_REFUND_ADDRESS, convert("1017"));
    }

    @Test
    @DisplayName("query 传 name 命中枚举")
    void nameMatched() {
        assertEquals(SmsEnum.Type.DESTROY_USER, convert("DESTROY_USER"));
    }

    @Test
    @DisplayName("query 传越界 code 转为 null 且不抛异常")
    void outOfRangeToNull() {
        assertNull(convert("1018"));
    }

    @Test
    @DisplayName("query 传未知字符串转为 null 且不抛异常")
    void unknownToNull() {
        assertNull(convert("NOT_EXIST"));
    }

    /**
     * 按 query 参数语义转换字符串为枚举
     *
     * @param source query 参数原始值
     * @return 转换后的枚举值
     */
    private SmsEnum.Type convert(String source) {
        return conversionService.convert(source, SmsEnum.Type.class);
    }
}
