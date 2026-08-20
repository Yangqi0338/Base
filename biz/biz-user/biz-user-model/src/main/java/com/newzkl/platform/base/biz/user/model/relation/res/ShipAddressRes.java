package com.newzkl.platform.base.biz.user.model.relation.res;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

/**
 * 收货地址视图对象
 *
 * <p>对外展示的精简模型，隐藏敏感/内部字段。</p>
 *
 * @author sijiwang
 */
@Data
public class ShipAddressRes extends BaseRes {
    /**
     * 所属账号ID
     */
    private Long accountId;
    /**
     * 收货联系人姓名
     */
    private String shipName;
    /**
     * 联系方式（脱敏展示）
     */
    private String shipPhone;
    /**
     * 省份编码
     */
    private String shipProvinceCode;
    /**
     * 省份名称
     */
    private String shipProvinceName;
    /**
     * 城市编码
     */
    private String shipCityCode;
    /**
     * 城市名称
     */
    private String shipCityName;
    /**
     * 区县编码
     */
    private String shipAreaCode;
    /**
     * 区县名称
     */
    private String shipAreaName;
    /**
     * 街道编码
     */
    private String shipStreetCode;
    /**
     * 街道名称
     */
    private String shipStreetName;
    /**
     * 省市区街道拼接
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
     * 角色关联ID
     */
    private AccountEnum.Identity identity;
}
