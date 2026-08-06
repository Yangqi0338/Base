package com.newzkl.platform.base.biz.account.facade.model;


import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

import java.io.Serializable;

/**
 * 收货地址
 * @author fang
 */
@Data
public class ShipAddressOutVO extends BaseRes implements Serializable {
     /**
     * ID
     */
     private Long id;
     /**
     * 收货地区，例如:辽宁省,沈阳市,铁西区,XXX镇（三级与四级地址均可下单）
     */
     private String shipArea;
     /**
     * 收货联系人姓名
     */
     private String shipName;
     /**
     * 收货地址，如创业路东
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
     * 收货地址编码，省CODE, 6位
     */
     private Integer shipProvinceCode;
     /**
     * 收货地址编码，市CODE, 6位
     */
     private Integer shipCityCode;
     /**
     * 收货地址编码，区CODE, 6位
     */
     private Integer shipAreaCode;
     /**
     * 是否默认
     */
     private Integer isDefault;
     /**
     * 角色类型Id 查询
     */
     private Long roleId;
     /**
      * 账号ID
      */
     private Long accountId;
}