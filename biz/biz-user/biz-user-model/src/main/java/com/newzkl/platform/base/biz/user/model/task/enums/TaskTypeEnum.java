package com.newzkl.platform.base.biz.user.model.task.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 营销任务类型枚举
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.taskConfig.model.enums.TaskTypeEnum}。
 * code/desc 逐字沿用。</p>
 *
 * @author KC
 */
@Getter
@AllArgsConstructor
public enum TaskTypeEnum {

    /**
     * 观看激励广告
     */
    AD_WATCH(1, "观看激励广告"),

    /**
     * 购买商品
     */
    GOODS_BUY(2, "购买商品"),
    ;

    /**
     * 编码
     */
    @EnumValue
    @JsonValue
    private final Integer code;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 根据编码获取枚举
     *
     * @param code 编码
     * @return 匹配的枚举, 无匹配返回 null
     */
    public static TaskTypeEnum getByCode(Integer code) {
        for (TaskTypeEnum item : values()) {
            if (item.code.equals(code)) {
                return item;
            }
        }
        return null;
    }
}
