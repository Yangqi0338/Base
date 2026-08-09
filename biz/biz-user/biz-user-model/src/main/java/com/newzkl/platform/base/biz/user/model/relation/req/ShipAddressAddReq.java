package com.newzkl.platform.base.biz.user.model.relation.req;

import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 新增收货地址请求参数
 *
 * <p>入参校验规则：核心字段非空，默认值合理。</p>
 *
 * @author sijiwang
 */
@Data
public class ShipAddressAddReq {
    /**
     * 收货联系人姓名
     */
    @NotBlank(message = "收货人姓名不能为空")
    private String shipName;
    /**
     * 联系方式
     * @ext 支持手机号/固定电话
     */
    @NotBlank(message = "收货人电话不能为空")
    private String shipPhone;
    /**
     * 省份编码
     * @ext 6位行政编码
     */
    @NotBlank(message = "省份编码不能为空")
    private String shipProvinceCode;
    /**
     * 省份名称
     */
    @NotBlank(message = "省份名称不能为空")
    private String shipProvinceName;
    /**
     * 城市编码
     * @ext 6位行政编码
     */
    @NotBlank(message = "城市编码不能为空")
    private String shipCityCode;
    /**
     * 城市名称
     */
    @NotBlank(message = "城市名称不能为空")
    private String shipCityName;
    /**
     * 区县编码
     * @ext 6位行政编码
     */
    @NotBlank(message = "区县编码不能为空")
    private String shipAreaCode;
    /**
     * 区县名称
     */
    @NotBlank(message = "区县名称不能为空")
    private String shipAreaName;
    /**
     * 街道编码
     * @ext 6位行政编码，可选
     */
    private String shipStreetCode;
    /**
     * 街道名称（可选）
     */
    private String shipStreetName;
    /**
     * 详细地址（必填）
     */
    @NotBlank(message = "详细地址不能为空")
    private String shipDetailAddress;
    /**
     * 收货邮编（可选）
     */
    private String shipZipCode;
    /**
     * 是否默认地址
     * @ext 0-否 1-是（默认0）
     */
    private Integer isDefault = 0;
    /**
     * 角色类型
     * @ext 1-个人 2-企业 3-门店（可选）
     */
    private Integer roleType;
    /**
     * 角色关联ID
     * @ext 可选
     */
    private RoleEnum.CompanyRole role;
    /**
     * 操作人
     * @ext 系统自动填充
     */
    private String operator;
    /**
     * 所属账号ID
     * @ext 从上下文获取，无需前端传
     */
    private Long accountId;
}
