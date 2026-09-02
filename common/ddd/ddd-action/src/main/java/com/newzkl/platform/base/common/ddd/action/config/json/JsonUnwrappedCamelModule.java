package com.newzkl.platform.base.common.ddd.action.config.json;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.NameTransformer;

import java.io.Serializable;

/**
 * {@code @JsonUnwrapped(prefix=...)} 驼峰命名模块
 *
 * <p>Jackson 原生实现 {@code NameTransformer.simpleTransformer(prefix, suffix)} 是纯字符串拼接:
 * {@code @JsonUnwrapped(prefix = "issuer")} 平展 {@code name}/{@code id} 得到
 * {@code issuername}/{@code issuerid}, 不符合前端驼峰契约。本模块插入一个只覆写
 * {@code findUnwrappingNameTransformer} 的 {@link NopAnnotationIntrospector},
 * 在 prefix 非空时把被平展字段首字母大写, 输出 {@code issuerName}/{@code issuerId}</p>
 *
 * <p>只对 prefix 生效: 无 prefix 的 {@code @JsonUnwrapped} (如 {@code BaseRes.executor}) 走 Jackson
 * 默认原名平展; 用了 suffix 的一并交回默认实现, 避免猜测后缀的大小写意图</p>
 *
 * @author KC
 */
public class JsonUnwrappedCamelModule extends SimpleModule {

    /**
     * 装配 prefix 驼峰化的注解解析器
     */
    public JsonUnwrappedCamelModule() {
        super("jsonUnwrappedCamelModule");
    }

    /**
     * 以 insert 方式接线, 成为 {@code AnnotationIntrospectorPair} 的 primary
     *
     * <p>不可用 append: pair 对本方法取 primary 非 null 结果, append 会被默认实现的
     * simpleTransformer 抢先返回</p>
     *
     * @param context 模块装配上下文
     */
    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);
        context.insertAnnotationIntrospector(new CamelPrefixIntrospector());
    }

    /**
     * 仅接管平展改名、其余一律交回默认实现的注解解析器
     */
    private static class CamelPrefixIntrospector extends NopAnnotationIntrospector {

        private static final long serialVersionUID = 1L;

        /**
         * 解析平展改名规则
         *
         * @param member 被 {@code @JsonUnwrapped} 标注的成员
         * @return prefix 非空且无 suffix 时返回驼峰改名器, 否则 null 交回默认实现
         */
        @Override
        public NameTransformer findUnwrappingNameTransformer(AnnotatedMember member) {
            JsonUnwrapped annotation = member.getAnnotation(JsonUnwrapped.class);
            if (annotation == null || !annotation.enabled()) {
                return null;
            }
            if (annotation.prefix().isEmpty() || !annotation.suffix().isEmpty()) {
                return null;
            }
            return new CamelPrefixTransformer(annotation.prefix());
        }
    }

    /**
     * prefix + 首字母大写字段名 的双向改名器
     */
    private static class CamelPrefixTransformer extends NameTransformer implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 平展前缀
         */
        private final String prefix;

        /**
         * @param prefix 平展前缀
         */
        CamelPrefixTransformer(String prefix) {
            this.prefix = prefix;
        }

        /**
         * 内部字段名 转 对外字段名
         *
         * @param name 内部字段名, 如 {@code name}
         * @return 对外字段名, 如 {@code issuerName}
         */
        @Override
        public String transform(String name) {
            if (name.isEmpty()) {
                return prefix;
            }
            return prefix + StrUtil.upperFirst(name);
        }

        /**
         * 对外字段名 还原 内部字段名
         *
         * @param transformed 对外字段名, 如 {@code issuerName}
         * @return 内部字段名, 如 {@code name}; 不匹配前缀返回 null
         */
        @Override
        public String reverse(String transformed) {
            if (!transformed.startsWith(prefix)) {
                return null;
            }
            String origin = transformed.substring(prefix.length());
            if (origin.isEmpty()) {
                return origin;
            }
            return Character.toLowerCase(origin.charAt(0)) + origin.substring(1);
        }

        @Override
        public String toString() {
            return "CamelPrefixTransformer(" + prefix + ")";
        }
    }
}
