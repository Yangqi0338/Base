package com.newzkl.platform.base.biz.order.facade.model.api.order;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 收货信息
 * @author muc_fang
 */
@Data
public class ApiShipVO  implements Serializable {
    /**
     * 收货人姓名
     */
    @NotBlank
    private String shipName;
    /**
     * 收货人联系方式
     */
    @NotBlank
    private String shipPhone;
    /**
     * 收货地区，例如:辽宁省,沈阳市,铁西区,XXX镇
     */
    @NotBlank
    private String shipArea;
    /**
     * 收货地址，例如:创业路东
     */
    @NotBlank
    private String shipAddress;
    /**
     * 收货地址编码，省CODE, 6位
     */
    @NotNull
    private Integer shipProvinceCode;
    /**
     * 收货地址编码，市CODE, 6位
     */
    @NotNull
    private Integer shipCityCode;
    /**
     * 收货地址编码，区CODE, 6位
     */
    @NotNull
    private Integer shipAreaCode;
    /**
     * 收货邮编
     */
    private String shipZipCode;
}
