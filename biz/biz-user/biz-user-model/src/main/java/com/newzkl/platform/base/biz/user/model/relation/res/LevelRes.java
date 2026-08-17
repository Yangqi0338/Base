package com.newzkl.platform.base.biz.user.model.relation.res;

import com.newzkl.platform.base.biz.user.model.relation.vo.ConditionCalVO;
import com.newzkl.platform.base.biz.user.model.relation.vo.PermissionVO;
import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import com.newzkl.platform.base.biz.user.model.relation.req.ConditionReq;
import com.newzkl.platform.base.biz.user.model.relation.res.condition.Condition;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 等级视图对象
 *
 * @author muc_fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LevelRes extends BaseRes implements Condition {
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
