package com.newzkl.platform.base.biz.user.model.relation.vo;

import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.biz.user.model.relation.req.ConditionReq;
import com.newzkl.platform.base.biz.user.model.relation.res.condition.Condition;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 等级视图对象。
 *
 * @author muc_fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LevelVO extends BaseRes implements Condition {
    /**
     * ID
     */
    private Long id;
    /**
     * 业务类型 0 甄选师
     */
    private RoleEnum.CompanyRole type;
    /**
     * 是否开启
     */
    private Integer enable;
    /**
     * 等级值
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
    private ConditionCalVO condition;

    @Override
    public double isMeet(ConditionReq conditionCommand) {
        if (condition == null) {
            return 0.0;
        }
        return condition.isMeet(conditionCommand);
    }
}
