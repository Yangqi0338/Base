package com.newzkl.platform.base.biz.user.model.relation.req;

import com.newzkl.platform.base.biz.user.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.query.BusinessPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 收货地址分页查询请求参数。
 *
 * <p>支持多条件组合查询，默认按默认地址+更新时间排序。</p>
 *
 * <p>迁移说明：源类继承 MyBatis-Plus Page（extension），model 层无 MP extension 依赖，
 * 改继承 {@link BusinessPageQuery}；infra 层据此构造 MP Page 分页。</p>
 *
 * @author sijiwang
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ShipAddressPageReq extends BusinessPageQuery {
    /**
     * 所属账号ID（必填，数据隔离）
     */
    private Long accountId;
    /**
     * 收货人姓名（模糊查询）
     */
    private String shipName;
    /**
     * 联系方式（模糊查询）
     */
    private String shipPhone;
    /**
     * 是否默认地址：0-否 1-是（可选）
     */
    private Integer isDefault;
    /**
     * 角色类型：1-个人 2-企业 3-门店（可选）
     */
    private Integer roleType;
    /**
     * 角色关联ID（可选）
     */
    private RoleEnum.CompanyRole role;
}
