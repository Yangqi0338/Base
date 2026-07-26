package com.newzkl.platform.base.biz.account.model.level.res;

import com.newzkl.platform.base.biz.account.model.level.vo.ConditionVO;
import com.newzkl.platform.base.biz.account.model.level.vo.PermissionVO;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 等级出参。
 *
 * <p>旧 {@code /user/level/levelList} 直接外泄领域实体 {@code Level}, 新架构以出参对象替代,
 * 字段一一对应, 不改前端契约。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LevelRes extends BaseRes {

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
