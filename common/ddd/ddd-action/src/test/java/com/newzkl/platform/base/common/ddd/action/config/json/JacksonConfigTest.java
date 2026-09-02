package com.newzkl.platform.base.common.ddd.action.config.json;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newzkl.platform.base.common.core.model.enums.SmsEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link JacksonConfig} 模块装配回归测试
 *
 * <p>逐个 {@code @Bean Module} 装进同一个 ObjectMapper (等价 Spring Boot
 * 汇总全部 Module bean 后一次性 modulesToInstall), 验证出入参契约</p>
 *
 * @author KC
 */
class JacksonConfigTest {

    /**
     * 装配了全部模块的 ObjectMapper
     */
    private ObjectMapper mapper;

    /**
     * 每例前按 Boot 的装配方式重建 ObjectMapper
     */
    @BeforeEach
    void setUp() {
        JacksonConfig config = new JacksonConfig();
        mapper = new ObjectMapper()
                .registerModule(config.enumDeserializeModule())
                .registerModule(config.jsonTranslateModule())
                .registerModule(config.jsonTimeModule())
                .registerModule(config.jsonUnwrappedCamelModule())
                .registerModule(config.longToStringModule());
    }

    @Test
    @DisplayName("Long 出参写成字符串 (雪花 id 超 JS Number 安全整数)")
    void longWrittenAsString() throws Exception {
        Holder holder = new Holder();
        holder.setId(1234567890123456789L);
        assertEquals("\"1234567890123456789\"", field(holder, "id"));
    }

    @Test
    @DisplayName("基本类型 long 保持数字 (Page.total 等小值域字段不受影响)")
    void primitiveLongKeptAsNumber() throws Exception {
        Holder holder = new Holder();
        holder.setTotal(100L);
        assertEquals("100", field(holder, "total"));
    }

    @Test
    @DisplayName("Long 入参接受字符串, 回程不丢精度")
    void longReadFromString() throws Exception {
        Holder holder = mapper.readValue("{\"id\":\"1234567890123456789\"}", Holder.class);
        assertEquals(1234567890123456789L, holder.getId());
    }

    @Test
    @DisplayName("LocalDateTime 输出 yyyy-MM-dd HH:mm:ss (非 ISO-8601)")
    void dateTimeFormatted() throws Exception {
        Holder holder = new Holder();
        holder.setCreateTime(LocalDateTime.of(2026, 8, 29, 11, 13, 53));
        assertEquals("\"2026-08-29 11:13:53\"", field(holder, "createTime"));
    }

    @Test
    @DisplayName("LocalDateTime 入参接受 yyyy-MM-dd HH:mm:ss")
    void dateTimeParsed() throws Exception {
        Holder holder = mapper.readValue("{\"createTime\":\"2026-08-29 11:13:53\"}", Holder.class);
        assertEquals(LocalDateTime.of(2026, 8, 29, 11, 13, 53), holder.getCreateTime());
    }

    @Test
    @DisplayName("LocalDate 输出 yyyy-MM-dd, LocalTime 输出 HH:mm:ss")
    void dateAndTimeFormatted() throws Exception {
        Holder holder = new Holder();
        holder.setBizDate(LocalDate.of(2026, 8, 29));
        holder.setBizTime(LocalTime.of(11, 13, 53));
        assertEquals("\"2026-08-29\"", field(holder, "bizDate"));
        assertEquals("\"11:13:53\"", field(holder, "bizTime"));
    }

    @Test
    @DisplayName("枚举越界 code 反序列化为 null (不抛异常)")
    void enumOutOfRangeToNull() throws Exception {
        Holder holder = mapper.readValue("{\"type\":1018}", Holder.class);
        assertEquals(null, holder.getType());
    }

    @Test
    @DisplayName("带 prefix 的 @JsonUnwrapped 平展成驼峰 (issuerName 而非 issuername)")
    void unwrappedPrefixCamelized() throws Exception {
        Holder holder = new Holder();
        Issuer issuer = new Issuer();
        issuer.setName("张三");
        issuer.setId(841333562827845L);
        holder.setIssuer(issuer);
        String json = mapper.writeValueAsString(holder);
        assertEquals("\"张三\"", mapper.readTree(json).get("issuerName").toString());
        assertEquals("\"841333562827845\"", mapper.readTree(json).get("issuerId").toString());
    }

    @Test
    @DisplayName("驼峰平展字段可反序列化回原对象")
    void unwrappedPrefixCamelizedRead() throws Exception {
        Holder holder = mapper.readValue("{\"issuerName\":\"张三\",\"issuerId\":\"841333562827845\"}", Holder.class);
        assertEquals("张三", holder.getIssuer().getName());
        assertEquals(841333562827845L, holder.getIssuer().getId());
    }

    @Test
    @DisplayName("无 prefix 的 @JsonUnwrapped 保持原名平展 (BaseRes.executor 契约不变)")
    void unwrappedWithoutPrefixKeptAsIs() throws Exception {
        Holder holder = new Holder();
        Issuer plain = new Issuer();
        plain.setName("李四");
        holder.setPlain(plain);
        assertEquals("\"李四\"", field(holder, "name"));
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
     * 承载各类型字段的测试载体
     *
     * <p>手写 getter/setter 不用 lombok, 避免测试编译受注解处理器配置影响</p>
     */
    static class Holder {
        /**
         * 雪花 id (包装类型)
         */
        private Long id;

        /**
         * 总数 (基本类型)
         */
        private long total;

        /**
         * 创建时间
         */
        private LocalDateTime createTime;

        /**
         * 业务日期
         */
        private LocalDate bizDate;

        /**
         * 业务时间
         */
        private LocalTime bizTime;

        /**
         * 短信类型
         */
        private SmsEnum.Type type;

        /**
         * 带 prefix 平展的对象
         */
        @JsonUnwrapped(prefix = "issuer")
        private Issuer issuer;

        /**
         * 无 prefix 平展的对象
         */
        @JsonUnwrapped
        private Issuer plain;

        /**
         * 取带 prefix 平展的对象
         *
         * @return 平展对象
         */
        public Issuer getIssuer() {
            return issuer;
        }

        /**
         * 设带 prefix 平展的对象
         *
         * @param issuer 平展对象
         */
        public void setIssuer(Issuer issuer) {
            this.issuer = issuer;
        }

        /**
         * 取无 prefix 平展的对象
         *
         * @return 平展对象
         */
        public Issuer getPlain() {
            return plain;
        }

        /**
         * 设无 prefix 平展的对象
         *
         * @param plain 平展对象
         */
        public void setPlain(Issuer plain) {
            this.plain = plain;
        }

        /**
         * 取 id
         *
         * @return id
         */
        public Long getId() {
            return id;
        }

        /**
         * 设 id
         *
         * @param id id
         */
        public void setId(Long id) {
            this.id = id;
        }

        /**
         * 取总数
         *
         * @return 总数
         */
        public long getTotal() {
            return total;
        }

        /**
         * 设总数
         *
         * @param total 总数
         */
        public void setTotal(long total) {
            this.total = total;
        }

        /**
         * 取创建时间
         *
         * @return 创建时间
         */
        public LocalDateTime getCreateTime() {
            return createTime;
        }

        /**
         * 设创建时间
         *
         * @param createTime 创建时间
         */
        public void setCreateTime(LocalDateTime createTime) {
            this.createTime = createTime;
        }

        /**
         * 取业务日期
         *
         * @return 业务日期
         */
        public LocalDate getBizDate() {
            return bizDate;
        }

        /**
         * 设业务日期
         *
         * @param bizDate 业务日期
         */
        public void setBizDate(LocalDate bizDate) {
            this.bizDate = bizDate;
        }

        /**
         * 取业务时间
         *
         * @return 业务时间
         */
        public LocalTime getBizTime() {
            return bizTime;
        }

        /**
         * 设业务时间
         *
         * @param bizTime 业务时间
         */
        public void setBizTime(LocalTime bizTime) {
            this.bizTime = bizTime;
        }

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
    }

    /**
     * 被平展的内嵌对象 (对齐 {@code AccountVO} 的 name/id 结构)
     */
    static class Issuer {
        /**
         * 名称
         */
        private String name;

        /**
         * 主键
         */
        private Long id;

        /**
         * 取名称
         *
         * @return 名称
         */
        public String getName() {
            return name;
        }

        /**
         * 设名称
         *
         * @param name 名称
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * 取主键
         *
         * @return 主键
         */
        public Long getId() {
            return id;
        }

        /**
         * 设主键
         *
         * @param id 主键
         */
        public void setId(Long id) {
            this.id = id;
        }
    }
}
