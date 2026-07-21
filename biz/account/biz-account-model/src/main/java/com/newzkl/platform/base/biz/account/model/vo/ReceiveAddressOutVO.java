package com.newzkl.platform.base.biz.account.model.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * @author muc_fang
 * @Description: 收货信息
 * @date 2024/1/1916:57
 */
@Data
public class ReceiveAddressOutVO implements Serializable {
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
