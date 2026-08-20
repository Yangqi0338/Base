package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;

import java.util.List;

/**
 * 用户账号
 *
 * @author fang
 */
@Data
public class AccountParentQuery extends PageQuery {
    /** 主键ID */
    private Long id;

    /**
     * 查询范围
     * @ext 0 直推 1 所有; 无对应枚举, 保留 Integer
     */
    private Integer scope;

    /**
     * 父id列表
     */
    private String pidList;

    /**
     * 身份
     */
    private AccountEnum.Client client;

    /**
     * 角色id列表
     */
    private List<Long> roleIdList;

    /**
     * 设置单个角色ID (内部包装为列表)
     *
     * @param roleId 角色ID
     */
    public void setRoleId(Long roleId) {
        this.roleIdList = doWrapperList(roleIdList, roleId);
    }

}
