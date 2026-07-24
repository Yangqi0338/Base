package com.newzkl.platform.base.biz.user.model.relation.req;

import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 更新收货地址请求参数。
 *
 * <p>核心规则：ID必填，其他字段按需更新。</p>
 *
 * @author sijiwang
 */
@Data
public class ShipAddressUpdateReq {
    /**
     * 地址ID（必填）
     */
    @NotNull(message = "地址ID不能为空")
    private Long id;
    /**
     * 收货联系人姓名（可选）
     */
    private String shipName;
    /**
     * 联系方式（可选）
     */
    private String shipPhone;
    /**
     * 省份编码（可选）
     */
    private String shipProvinceCode;
    /**
     * 省份名称（可选）
     */
    private String shipProvinceName;
    /**
     * 城市编码（可选）
     */
    private String shipCityCode;
    /**
     * 城市名称（可选）
     */
    private String shipCityName;
    /**
     * 区县编码（可选）
     */
    private String shipAreaCode;
    /**
     * 区县名称（可选）
     */
    private String shipAreaName;
    /**
     * 街道编码（可选）
     */
    private String shipStreetCode;
    /**
     * 街道名称（可选）
     */
    private String shipStreetName;
    /**
     * 详细地址（可选）
     */
    private String shipDetailAddress;
    /**
     * 收货邮编（可选）
     */
    private String shipZipCode;
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
    /**
     * 操作人（系统自动填充）
     */
    private String operator;
}
