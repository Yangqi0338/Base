package com.newzkl.platform.base.biz.auth.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 模块枚举 (Redis key 前缀用)
 *
 * <p>与 biz-account / biz-order / biz-finance 等域各自持有的同名枚举一致 (本仓既有的按域复制惯例),
 * 保证生成的 key 前缀完全一致。biz-auth 只用到 {@link ModuleEnum#COMMON}, 其余保留以对齐语义。</p>
 *
 * @author sijiwang
 */
@Getter
@AllArgsConstructor
public enum ModuleEnum {
    /**
     * 通用
     */
    COMMON("", "通用"),
    /**
     * 网关
     */
    GATEWAY("gateway", "网关"),
    ;

    private final String code;
    private final String desc;

    /**
     * 根据编码获取枚举
     *
     * @param code 模块编码
     * @return 匹配的枚举, 无匹配返回 null
     */
    public static ModuleEnum getByCode(String code) {
        for (ModuleEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
