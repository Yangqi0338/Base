package com.newzkl.platform.base.common.core.model.enums;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * {@link EnumDeserializerModifier} 反序列化行为回归测试
 *
 * <p>覆盖线上 bug: 前端传越界枚举 code (如 SmsEnum.Type=1018) 时 Jackson 默认按 ordinal 索引解析,
 * 抛 {@code InvalidFormatException} 直接 400。期望改为反序列化成 {@code null}, 由 validation 兜底</p>
 *
 * @author KC
 */
class EnumDeserializerModifierTest {

    /**
     * 装配了枚举修饰器的 ObjectMapper
     */
    private ObjectMapper mapper;

    /**
     * 每例前重建 ObjectMapper, 装配方式对齐 ddd-action JacksonConfig#enumDeserializeModule
     */
    @BeforeEach
    void setUp() {
        SimpleModule module = new SimpleModule("enumDeserializeModule");
        module.setDeserializerModifier(new EnumDeserializerModifier());
        mapper = new ObjectMapper().registerModule(module);
    }

    @Test
    @DisplayName("字符串 code 命中枚举")
    void stringCodeMatched() throws Exception {
        assertEquals(SmsEnum.Type.Login, read("\"1001\""));
    }

    @Test
    @DisplayName("数字 code 命中枚举 (不再按 ordinal 索引)")
    void numberCodeMatched() throws Exception {
        assertEquals(SmsEnum.Type.SUPPLIER_REFUND_ADDRESS, read("1017"));
    }

    @Test
    @DisplayName("枚举 name 命中枚举")
    void nameMatched() throws Exception {
        assertEquals(SmsEnum.Type.DESTROY_USER, read("\"DESTROY_USER\""));
    }

    @Test
    @DisplayName("越界数字 code 反序列化为 null 且不抛异常")
    void outOfRangeNumberToNull() throws Exception {
        assertNull(read("1018"));
    }

    @Test
    @DisplayName("未知字符串反序列化为 null 且不抛异常")
    void unknownStringToNull() throws Exception {
        assertNull(read("\"NOT_EXIST\""));
    }

    @Test
    @DisplayName("空字符串反序列化为 null")
    void blankToNull() throws Exception {
        assertNull(read("\"\""));
    }

    @Test
    @DisplayName("null 字面量反序列化为 null")
    void nullLiteralToNull() throws Exception {
        assertNull(read("null"));
    }

    @Test
    @DisplayName("枚举集合: 元素逐个按值匹配, 越界元素为 null (无需专门的集合反序列化器)")
    void collectionElementsMatchedIndividually() throws Exception {
        Holder holder = mapper.readValue("{\"typeList\":[\"1001\",1017,\"DESTROY_USER\",1018]}", Holder.class);
        assertEquals(4, holder.getTypeList().size());
        assertEquals(SmsEnum.Type.Login, holder.getTypeList().get(0));
        assertEquals(SmsEnum.Type.SUPPLIER_REFUND_ADDRESS, holder.getTypeList().get(1));
        assertEquals(SmsEnum.Type.DESTROY_USER, holder.getTypeList().get(2));
        assertNull(holder.getTypeList().get(3));
    }

    /**
     * 反序列化单个 type 字段
     *
     * @param typeJson type 字段的 JSON 片段
     * @return 反序列化后的枚举值
     * @throws Exception 反序列化异常 (期望不发生)
     */
    private SmsEnum.Type read(String typeJson) throws Exception {
        return mapper.readValue("{\"type\":" + typeJson + "}", Holder.class).getType();
    }

    /**
     * 承载枚举字段的测试载体
     *
     * <p>本模块 testCompile 关闭注解处理 ({@code proc=none}), 故手写 getter/setter 不用 lombok</p>
     */
    static class Holder {
        /**
         * 短信类型
         */
        private SmsEnum.Type type;

        /**
         * 短信类型集合
         */
        private List<SmsEnum.Type> typeList;

        /**
         * 取短信类型
         *
         * @return 短信类型
         */
        public SmsEnum.Type getType() {
            return type;
        }

        /**
         * 设短信类型
         *
         * @param type 短信类型
         */
        public void setType(SmsEnum.Type type) {
            this.type = type;
        }

        /**
         * 取短信类型集合
         *
         * @return 短信类型集合
         */
        public List<SmsEnum.Type> getTypeList() {
            return typeList;
        }

        /**
         * 设短信类型集合
         *
         * @param typeList 短信类型集合
         */
        public void setTypeList(List<SmsEnum.Type> typeList) {
            this.typeList = typeList;
        }
    }
}
