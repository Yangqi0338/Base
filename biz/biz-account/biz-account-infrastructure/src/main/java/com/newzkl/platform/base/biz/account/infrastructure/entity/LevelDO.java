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
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(autoResultMap = true)
public class LevelDO extends BaseDO {

    /**
     * 类型
     * @ext 角色 ID
     */
    @Index
    private Integer type;

    /**
     * 是否开启
     * @ext 0 否, 1 是
     */
    private Integer enable;

    /**
     * 等级值
     * @ext 1 2 3 ...
     */
    private Integer value;

    /**
     * 等级名称
     */
    private String name;

    /**
     * 等级权限
     * @ext JSON 列
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private PermissionVO permission;

    /**
     * 升级条件
     * @ext JSON 列
     */
    @TableField(value = "`condition`", typeHandler = JacksonTypeHandler.class)
    private ConditionVO condition;

    /**
     * 条件判断类型
     * @ext 0 满足任意一项, 1 全部满足
     */
    private Integer conditionJudgeType;
}
