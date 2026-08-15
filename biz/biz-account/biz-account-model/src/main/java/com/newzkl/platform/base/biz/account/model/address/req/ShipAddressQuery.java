package com.newzkl.platform.base.biz.account.model.address.req;

import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 收货地址查询入参
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.address.model.req.ShipAddressQuery}。
 * 旧 {@code id} / {@code idList} / {@code accountId} 字段由父类 {@code BizPageQuery} 提供, 此处不重复声明。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ShipAddressQuery extends BizPageQuery {

    /**
     * 角色类型 ID
     */
    private RoleEnum.CompanyRole role;

    /**
     * 是否默认
     * @ext 0 否 1 是; 无对应枚举, 保留 Integer
     */
    private Integer isDefault;
}
