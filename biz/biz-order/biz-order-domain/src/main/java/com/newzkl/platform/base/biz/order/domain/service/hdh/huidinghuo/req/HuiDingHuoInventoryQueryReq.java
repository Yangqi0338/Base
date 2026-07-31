package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.req;


import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;

/**
 * 查询库存请求类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuiDingHuoInventoryQueryReq extends HuiDingHuoBaseReq {

    /**
     * 查询方式
     * PRODUCT_ID：商品ID
     * SKU_ID：SKU ID
     * BAR_CODE：条形码
     */
    @NotBlank(message = "查询方式不能为空")
    private String queryType;

    /**
     * 查询值
     * 根据queryType填写对应的值
     * 多个值用逗号分隔
     */
    @NotBlank(message = "查询值不能为空")
    private String queryValue;

    /**
     * 仓库ID
     * 不填则查询所有仓库
     */
    private String warehouseId;

    /**
     * 是否需要库存明细
     * Y：是
     * N：否
     * 默认：N
     */
    private String needDetail;
}
