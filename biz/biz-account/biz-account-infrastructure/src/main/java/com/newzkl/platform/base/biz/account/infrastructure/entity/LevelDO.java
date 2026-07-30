package com.newzkl.platform.base.biz.account.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.newzkl.platform.base.biz.account.model.level.vo.ConditionVO;
import com.newzkl.platform.base.biz.account.model.level.vo.PermissionVO;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * 等级持久化对象
 *
 * <p>对应旧表 {@code level} (旧 {@code com.zkl.scm.user.infrastructure.entity.LevelDO})。
 * 旧实现把 permission / condition 以 String 存 JSON 并靠 assembler 手工序列化,
 * 本仓改为 {@code JacksonTypeHandler} 自动映射 (与 biz-market 既有 JSON 列 DO 一致,
 * 需 {@code @TableName(autoResultMap = true)})。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "level", autoResultMap = true)
public class LevelDO extends BaseDO {

    /**
     * 类型 (角色 ID)
     */
    @Index
    private Integer type;

    /**
     * 是否开启: 0 否 1 是
     */
    private Integer enable;

    /**
     * 等级值: 1 2 3 ...
     */
    private Integer value;

    /**
     * 等级名称
     */
    private String name;

    /**
     * 等级权限 (JSON 列)
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private PermissionVO permission;

    /**
     * 升级条件 (JSON 列)
     */
    @TableField(value = "`condition`", typeHandler = JacksonTypeHandler.class)
    private ConditionVO condition;

    /**
     * 条件判断类型: 0 满足任意一项 1 全部满足
     */
    private Integer conditionJudgeType;
}
