package com.newzkl.platform.base.biz.account.model.address.res;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 收货地址出参
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.address.model.vo.ShipAddressVO} 的对外出参角色。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ShipAddressRes extends BaseRes {

    /**
     * 收货地区, 例如: 辽宁省,沈阳市,铁西区,XXX镇
     */
    private String shipArea;

    /**
     * 收货联系人姓名
     */
    private String shipName;

    /**
     * 收货地址, 如创业路东
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
     * 收货地址编码, 省 CODE, 6 位
     */
    private Integer shipProvinceCode;

    /**
     * 收货地址编码, 市 CODE, 6 位
     */
    private Integer shipCityCode;

    /**
     * 收货地址编码, 区 CODE, 6 位
     */
    private Integer shipAreaCode;

    /**
     * 是否默认: 0 否 1 是
     */
    private Integer isDefault;

    /**
     * 角色类型 ID
     */
    private Long roleId;

    /**
     * 账号 ID
     */
    private Long accountId;
}
