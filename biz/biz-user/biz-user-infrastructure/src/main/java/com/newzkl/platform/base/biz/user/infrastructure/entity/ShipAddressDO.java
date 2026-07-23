package com.newzkl.platform.base.biz.user.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.biz.user.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.entity.BaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
// TODO[pom-gap mybatis-plus-ext]: import org.dromara.mpe.autofill.annotation.JsonSerializable; (parent 已 depMgmt mybatis-plus-ext, infra 需补依赖)

/**
 * 收货地址数据库实体。
 *
 * <p>与数据库表 ship_address 一一对应。</p>
 *
 * @author sijiwang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(autoResultMap = true)
public class ShipAddressDO extends BaseDO {
    /**
     * 所属账号ID
     */
    @Index
    private Long accountId;
    /**
     * 收货联系人姓名
     */
    private String shipName;
    /**
     * 联系方式
     */
    private String shipPhone;
    /**
     * 省市区街道code拼接
     */
    @Index
    private String shipFullCode;
    /**
     * 省市区街道name拼接
     */
    // TODO[pom-gap mybatis-plus-ext]: @JsonSerializable (autofill, parent 已 depMgmt mybatis-plus-ext, infra 需补依赖)
    private String shipFullName;
    /**
     * 详细收货地址
     */
    private String shipDetailAddress;
    /**
     * 收货邮编
     */
    private String shipZipCode;
    /**
     * 是否默认地址：0-否 1-是
     */
    private CommonEnum.YesOrNo isDefault;
    /**
     * 角色类型：1-个人 2-企业 3-门店
     */
    private Integer roleType;
    /**
     * 角色关联ID
     */
    private RoleEnum.CompanyRole role;
}
