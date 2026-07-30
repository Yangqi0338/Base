package com.newzkl.platform.base.biz.sys.model.template.enums;

import lombok.Getter;

/**
 * 模板操作类型枚举
 *
 * <p>迁移说明: 源 {@code TemplateOperationEnum}, code 与文案逐字保留。</p>
 *
 * @author KC
 */
@Getter
public enum TemplateOperationEnum {

    /**
     * 启用
     */
    ENABLE(1, "启用"),

    /**
     * 禁用
     */
    DISABLE(0, "禁用"),

    /**
     * 设为默认
     */
    SET_DEFAULT(2, "设为默认");

    /**
     * 操作码
     */
    private final int code;

    /**
     * 操作描述
     */
    private final String desc;

    TemplateOperationEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 按操作码取枚举
     *
     * @param code 操作码
     * @return 操作枚举
     * @throws IllegalArgumentException 操作码非法时抛出 (与源行为一致)
     */
    public static TemplateOperationEnum getByCode(int code) {
        for (TemplateOperationEnum operation : values()) {
            if (operation.code == code) {
                return operation;
            }
        }
        throw new IllegalArgumentException("无效的模板操作类型：" + code);
    }
}
