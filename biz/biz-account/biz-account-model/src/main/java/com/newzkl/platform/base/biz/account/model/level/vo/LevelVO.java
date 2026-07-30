package com.newzkl.platform.base.biz.account.model.level.vo;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 等级领域视图对象
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.level.model.entity.Level}。
 * 旧 {@code LevelVO} 携带的 {@code isMeet} 等级计算行为属升级引擎, 未随本切片迁移。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LevelVO extends BaseRes {

    /**
     * 类型 (角色 ID)
     */
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
     * 等级权限
     */
    private PermissionVO permission;

    /**
     * 升级条件
     */
    private ConditionVO condition;
}
