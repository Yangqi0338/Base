package com.newzkl.platform.base.biz.user.model.relation.dto;

import com.newzkl.platform.base.biz.user.model.relation.vo.ConditionVO;
import com.newzkl.platform.base.biz.user.model.relation.vo.PermissionVO;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 等级领域模型
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LevelDTO extends BaseRes {

    /**
     * 类型(角色id)
     */
    private Integer type;
    /**
     * 是否开启
     */
    private Integer enable;
    /**
     * 等级值 数值型: 1 2 3 4 ......
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
