package com.newzkl.platform.base.biz.order.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/11/1016:49
 */
@Data
public class ShipVO implements Serializable {
    /**
     * 收货地址id
     */
    private Long id;
    /**
     * 收货人姓名
     */
    private String shipName;
    /**
     * 收货人联系方式
     */
    private String shipPhone;
    /**
     * 收货地区，例如:辽宁省,沈阳市,铁西区,XXX镇
     */
    private String shipArea;
    /**
     * 收货地址，例如:创业路东
     */
    private String shipAddress;
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
     * 收货邮编
     */
    private String shipZipCode;

    public String buildShipAreaCode(){
        return String.join(",",String.valueOf(shipProvinceCode),String.valueOf(shipCityCode),String.valueOf(shipAreaCode));
    }
}
