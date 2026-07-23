package com.newzkl.platform.base.biz.finance.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * ID类型枚举（所有需要生成的ID类型在这里定义，自带配置信息）
 */
@Getter
@AllArgsConstructor
public enum IdTypeEnum {

    /**
     * 用户ID生成器
     */
    USER_CHANGCE("C", "user_id_changce", 8, "用户ID生成器"),

    ;

    // 前缀（如U、P、O）
    private final String prefix;

    // 数据库中计数器的generator_key（唯一标识）
    private final String generatorKey;

    // 数字部分初始位数（不足时补0，超出时自动扩位）
    private final int initialDigitLength;

    // 描述（用于数据库初始化）
    private final String description;

    /**
     * 根据前缀匹配枚举
     */
    public static IdTypeEnum getByPrefix(String prefix) {
        return Arrays.stream(values())
                .filter(e -> e.getPrefix().equals(prefix))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("未找到前缀为 [" + prefix + "] 的ID类型"));
    }
}