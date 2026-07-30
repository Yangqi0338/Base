package com.newzkl.platform.base.biz.user.model.task.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会员任务状态枚举
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.memberTaskRecord.model.enums.TaskStatusEnum}。</p>
 *
 * @author KC
 */
@Getter
@AllArgsConstructor
public enum TaskStatusEnum {

    /**
     * 待完成
     */
    PENDING(1, "待完成"),

    /**
     * 已完成
     */
    COMPLETED(2, "已完成"),

    /**
     * 完成失败
     */
    FAILED(3, "完成失败");

    /**
     * 状态编码
     */
    @EnumValue
    @JsonValue
    private final Integer code;

    /**
     * 状态描述
     */
    private final String desc;

    /**
     * 按编码查枚举
     *
     * <p>迁移说明：旧实现查不到抛 {@code IllegalArgumentException}，调用方靠 try-catch 兜「未知」，
     * 此处改为返回 null，由调用方判空。</p>
     *
     * @param code 状态编码
     * @return 枚举，不存在返回 null
     */
    public static TaskStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (TaskStatusEnum item : values()) {
            if (item.code.equals(code)) {
                return item;
            }
        }
        return null;
    }
}
