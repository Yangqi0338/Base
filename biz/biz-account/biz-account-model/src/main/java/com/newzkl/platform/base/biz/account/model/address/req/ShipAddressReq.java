package com.newzkl.platform.base.biz.account.model.address.req;

import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 收货地址写入入参
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.address.model.req.ShipAddressCommand}。
 * {@code accountId} / {@code roleId} 由领域层按当前登录态回填, 前端传值不生效。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ShipAddressReq extends BaseReq {

    /**
     * 收货地区
     * @ext 例如 辽宁省,沈阳市,铁西区,XXX镇; 三级与四级地址均可下单
     */
    private String shipArea;

    /**
     * 收货联系人姓名
     */
    private String shipName;

    /**
     * 收货地址
     * @ext 如创业路东
     */
    private String shipAddress;

    /**
     * 联系方式
     */
    private Long shipPhone;

    /**
     * 收货邮编
     */
    private String shipZipCode;

    /**
     * 收货省编码
     * @ext 省 CODE, 6 位
     */
    private Integer shipProvinceCode;

    /**
     * 收货市编码
     * @ext 市 CODE, 6 位
     */
    private Integer shipCityCode;

    /**
     * 收货区编码
     * @ext 区 CODE, 6 位
     */
    private Integer shipAreaCode;

    /**
     * 是否默认
     * @ext 0 否 1 是; 无对应枚举, 保留 Integer
     */
    private Integer isDefault;

    /**
     * 角色类型 ID (领域层按登录态回填)
     */
    private Long roleId;

    /**
     * 账号 ID (领域层按登录态回填)
     */
    private Long accountId;
}
