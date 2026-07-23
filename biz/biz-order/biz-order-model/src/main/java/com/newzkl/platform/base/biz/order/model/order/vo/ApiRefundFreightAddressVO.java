package com.newzkl.platform.base.biz.order.model.order.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 退货地址信息
 * @author muc_fang
 */
@Data
public class ApiRefundFreightAddressVO implements Serializable {
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
}
