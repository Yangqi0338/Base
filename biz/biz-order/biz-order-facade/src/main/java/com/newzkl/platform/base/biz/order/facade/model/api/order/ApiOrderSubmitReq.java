package com.newzkl.platform.base.biz.order.facade.model.api.order;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 提交订单请求对象
 * @Author: fang
 * @Date: 2023/4/19 14:43
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ApiOrderSubmitReq implements Serializable {
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
    /**
     * 商品信息
     */
    @NotEmpty(message = "商品信息不能为空")
    @Valid
    private List<ApiOrderSubmitItemReq> orderGoodsList;
    /**
     * 订单备注
     */
    private String remark;
    /**
     * 外部订单号
     */
    @NotNull
    private String outOrderNo;
}
