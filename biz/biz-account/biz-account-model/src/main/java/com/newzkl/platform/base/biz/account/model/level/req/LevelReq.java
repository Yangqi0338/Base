package com.newzkl.platform.base.biz.account.model.level.req;

import com.newzkl.platform.base.biz.account.model.level.vo.ConditionVO;
import com.newzkl.platform.base.biz.account.model.level.vo.PermissionVO;
import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 等级写入入参
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.level.model.command.LevelCommand}。
 * 旧入参无 {@code value} 字段, 等级值由领域层写死为 1, 此处保持一致。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LevelReq extends BaseReq {

    /**
     * 类型 (角色 ID)
     */
    private Integer type;

    /**
     * 是否开启: 0 否 1 是
     */
    private Integer enable;

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
