package com.newzkl.platform.base.biz.user.model.relation.req;

import com.newzkl.platform.base.biz.user.model.relation.vo.ConditionVO;
import com.newzkl.platform.base.biz.user.model.relation.vo.PermissionVO;
import lombok.Data;

/**
 * 等级请求。
 *
 * @author fang
 */
@Data
public class LevelReq {
    /**
     * ID
     */
    private Long id;
    /**
     * 业务类型 0 甄选师
     */
    private Integer type;
    /**
     * 是否开启
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
