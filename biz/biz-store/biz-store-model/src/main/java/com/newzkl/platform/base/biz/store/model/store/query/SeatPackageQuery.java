package com.newzkl.platform.base.biz.store.model.store.query;

import com.newzkl.platform.base.common.ddd.model.auth.OauthRole;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;

/**
 * 席位套餐分页查询
 */
@Data
public class SeatPackageQuery extends PageQuery {

    /**
     * 查询内容
     * @ext 匹配套餐名称/ID
     */
    private String searchContent;

    /**
     * 状态
     * @ext 取值范围: 0=禁用, 1=启用
     */
    private Integer state;

    /**
     * 角色
     */
    @OauthRole
    private RoleEnum.CompanyRole role;

}
