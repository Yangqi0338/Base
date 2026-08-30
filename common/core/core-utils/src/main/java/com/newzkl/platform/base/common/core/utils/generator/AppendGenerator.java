package com.newzkl.platform.base.common.core.utils.generator;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.StrJoiner;

/**
 * 拼接式编码生成器
 *
 * <p>不自造号, 只把上下文参数按固定分隔符拼成编码, 用于派生编码 (如 skuCode 派生自 spuCode)。
 * 数字段统一补零到 {@link #SEQ_LENGTH} 位, 其余段直接转字符串</p>
 *
 * @author KC
 */
public class AppendGenerator implements Generator {

    /**
     * 段分隔符
     */
    private static final String SEPARATOR = "-";

    /**
     * 数字段补零长度
     */
    private static final int SEQ_LENGTH = 3;

    @Override
    public Number nextId(Object entity) {
        throw new UnsupportedOperationException("拼接式编码不支持数值ID");
    }

    /**
     * 按分隔符拼接各段为编码
     *
     * @param entity 段数组, 单个对象视为一段
     * @return 拼接后的编码, 无段时返回空串
     */
    @Override
    public String nextUUID(Object entity) {
        if (entity == null) {
            return "";
        }
        Object[] segments = entity instanceof Object[] ? (Object[]) entity : new Object[]{entity};
        StrJoiner code = StrJoiner.of(SEPARATOR);
        for (Object segment : segments) {
            if (segment == null) {
                continue;
            }
            code.append(segment instanceof Number
                    ? String.format("%0" + SEQ_LENGTH + "d", segment)
                    : Convert.toStr(segment));
        }
        return code.toString();
    }
}
