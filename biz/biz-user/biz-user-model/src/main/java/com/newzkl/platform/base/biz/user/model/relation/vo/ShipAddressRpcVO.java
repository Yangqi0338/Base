package com.newzkl.platform.base.biz.user.model.relation.vo;

import com.newzkl.platform.base.biz.user.model.enums.RoleEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 收货地址 RPC 视图对象。
 *
 * @author sijiwang
 */
@Data
public class ShipAddressRpcVO implements Serializable {
    /**
     * 主键ID（雪花算法）
     */
    private Long id;
    /**
     * 所属账号ID（关联用户表主键）
     */
    private Long accountId;
    /**
     * 收货联系人姓名
     */
    private String shipName;
    /**
     * 联系方式（支持手机号/固定电话，含国际区号）
     */
    private String shipPhone;
    /**
     * 收货地址编码，省CODE（6位）
     */
    private String shipProvinceCode;
    /**
     * 收货省份名称
     */
    private String shipProvinceName;
    /**
     * 收货地址编码，市CODE（6位）
     */
    private String shipCityCode;
    /**
     * 收货城市名称
     */
    private String shipCityName;
    /**
     * 收货地址编码，区CODE（6位）
     */
    private String shipAreaCode;
    /**
     * 收货区县名称
     */
    private String shipAreaName;
    /**
     * 收货地址编码，街道CODE（6位，可选）
     */
    private String shipStreetCode;
    /**
     * 收货街道名称（可选）
     */
    private String shipStreetName;
    /**
     * 收货地区拼接
     */
    private String shipFullRegion;
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
    private Integer isDefault;
    /**
     * 角色类型：1-个人 2-企业 3-门店
     */
    private Integer roleType;
    /**
     * 角色关联ID（如门店ID/企业ID）
     */
    private RoleEnum.CompanyRole role;
    /**
     * 是否删除：0-未删除 1-已删除（逻辑删除）
     */
    private Integer isDeleted;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 删除时间（逻辑删除时填充）
     */
    private LocalDateTime deletedTime;
}
