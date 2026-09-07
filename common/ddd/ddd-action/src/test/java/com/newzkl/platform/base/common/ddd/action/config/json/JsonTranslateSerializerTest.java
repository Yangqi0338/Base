package com.newzkl.platform.base.common.ddd.action.config.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newzkl.platform.base.common.core.model.annotation.JsonTranslate;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.properties.SysProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * {@link JsonTranslate} 伴生字段两段取值测试
 *
 * <p>先按 index 取语义位, 再按 type 重写文案, 逐条锁住取位边界与重写开关</p>
 * <p>{@code SysProperties.translateMap} 是静态字段, 每例前置空、后置还原, 避免用例间串味</p>
 *
 * @author KC
 */
class JsonTranslateSerializerTest {

    /**
     * 只装 jsonTranslate 模块的 ObjectMapper
     */
    private ObjectMapper mapper;

    /**
     * 用例前的静态翻译表, 用于收尾还原
     */
    private Map<String, String> originTranslateMap;

    /**
     * 每例前重建 ObjectMapper 并清空静态翻译表
     */
    @BeforeEach
    void setUp() {
        JacksonConfig config = new JacksonConfig();
        mapper = new ObjectMapper().registerModule(config.jsonTranslateModule());
        originTranslateMap = SysProperties.translateMap;
        SysProperties.translateMap = new HashMap<>();
    }

    /**
     * 每例后还原静态翻译表
     */
    @AfterEach
    void tearDown() {
        SysProperties.translateMap = originTranslateMap;
    }

    @Test
    @DisplayName("默认 index=0 取首位语义")
    void defaultIndexTakesFirstSemantic() throws Exception {
        Holder holder = new Holder();
        holder.flag = CommonEnum.YesOrNo.YES;
        assertEquals("\"是\"", field(holder, "flagDesc"));
    }

    @Test
    @DisplayName("index=1 取第二位语义")
    void indexOneTakesSecondSemantic() throws Exception {
        Holder holder = new Holder();
        holder.enabled = CommonEnum.YesOrNo.NO;
        assertEquals("\"禁用\"", field(holder, "enabledDesc"));
    }

    @Test
    @DisplayName("index 越界回落首位语义")
    void indexOutOfRangeFallsBackToFirst() throws Exception {
        Holder holder = new Holder();
        holder.overflow = CommonEnum.YesOrNo.YES;
        assertEquals("\"是\"", field(holder, "overflowDesc"));
    }

    @Test
    @DisplayName("描述无分隔符时 index 不生效, 文案原样输出")
    void singleSemanticIgnoresIndex() throws Exception {
        Holder holder = new Holder();
        holder.symbol = CommonEnum.Symbol.POSITIVE;
        assertEquals("\"正数\"", field(holder, "symbolDesc"));
    }

    @Test
    @DisplayName("type=FIX 用 translateMap 重写取出的语义位文案")
    void fixTypeRewritesByTranslateMap() throws Exception {
        SysProperties.translateMap.put("启用", "已开启");
        Holder holder = new Holder();
        holder.enabled = CommonEnum.YesOrNo.YES;
        assertEquals("\"已开启\"", field(holder, "enabledDesc"));
    }

    @Test
    @DisplayName("扩展占位 type 命中也不重写文案")
    void reservedTypeKeepsSemanticText() throws Exception {
        SysProperties.translateMap.put("启用", "已开启");
        Holder holder = new Holder();
        holder.cached = CommonEnum.YesOrNo.YES;
        assertEquals("\"启用\"", field(holder, "cachedDesc"));
    }

    @Test
    @DisplayName("原字段保留 code 输出, 伴生字段另行追加")
    void rawFieldKeptAsCode() throws Exception {
        Holder holder = new Holder();
        holder.enabled = CommonEnum.YesOrNo.YES;
        assertEquals("1", field(holder, "enabled"));
    }

    @Test
    @DisplayName("字段名已以 Desc 结尾时原地改写, 不叠加伴生字段")
    void descSuffixFieldRewrittenInPlace() throws Exception {
        Holder holder = new Holder();
        holder.statusDesc = CommonEnum.YesOrNo.YES;
        assertEquals("\"启用\"", field(holder, "statusDesc"));
        assertNull(mapper.readTree(mapper.writeValueAsString(holder)).get("statusDescDesc"));
    }

    @Test
    @DisplayName("字段为 null 时伴生字段输出 null")
    void nullValueWritesNull() throws Exception {
        assertEquals("null", field(new Holder(), "flagDesc"));
    }

    /**
     * 取序列化结果中指定字段的 JSON 片段
     *
     * @param holder 待序列化载体
     * @param name   字段名
     * @return 该字段的 JSON 值片段
     * @throws Exception 序列化异常 (期望不发生)
     */
    private String field(Holder holder, String name) throws Exception {
        return mapper.readTree(mapper.writeValueAsString(holder)).get(name).toString();
    }

    /**
     * 承载各档注解取值的测试载体
     *
     * <p>私有字段 + 公有 getter, 对齐 lombok @Data 出参对象的注解合并路径</p>
     */
    static class Holder {
        /**
         * 默认档: index=0 + type=FIX
         */
        @JsonTranslate
        private CommonEnum.YesOrNo flag;

        /**
         * 第二语义位
         */
        @JsonTranslate(index = 1)
        private CommonEnum.YesOrNo enabled;

        /**
         * 越界语义位
         */
        @JsonTranslate(index = 9)
        private CommonEnum.YesOrNo overflow;

        /**
         * 单语义枚举
         */
        @JsonTranslate(index = 1)
        private CommonEnum.Symbol symbol;

        /**
         * 扩展占位翻译方式
         */
        @JsonTranslate(index = 1, type = CommonEnum.TranslateType.CACHE)
        private CommonEnum.YesOrNo cached;

        /**
         * 字段名自带 Desc 后缀
         */
        @JsonTranslate(index = 1)
        private CommonEnum.YesOrNo statusDesc;

        public CommonEnum.YesOrNo getFlag() {
            return flag;
        }

        public CommonEnum.YesOrNo getEnabled() {
            return enabled;
        }

        public CommonEnum.YesOrNo getOverflow() {
            return overflow;
        }

        public CommonEnum.Symbol getSymbol() {
            return symbol;
        }

        public CommonEnum.YesOrNo getCached() {
            return cached;
        }

        public CommonEnum.YesOrNo getStatusDesc() {
            return statusDesc;
        }
    }
}
