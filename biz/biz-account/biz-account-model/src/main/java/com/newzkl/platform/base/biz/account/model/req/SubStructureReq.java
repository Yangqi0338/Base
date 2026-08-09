package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;

import java.util.List;

/**
 * 子账号结构查询入参
 */
@Data
public class SubStructureReq extends BizPageQuery {
    /**
     * 范围
     * @ext 0 仅直推; 无对应枚举, 保留 Integer
     */
    private Integer scope;
    /**
     * 父id
     */
    private Long pid;
    /**
     * 父id列表
     * @ext 查询优化用
     */
    private String pidList;

    /**
     * 角色id列表
     */
    private List<Long> roleIdList;

    /**
     * 用户名
     */
    private String username;

    /**
     * 设置单个角色ID (内部包装为列表)
     *
     * @param roleId 角色ID
     */
    public void setRoleId(Long roleId) {
        this.roleIdList = doWrapperList(roleIdList, roleId);
    }
}
