package com.newzkl.platform.base.biz.order.facade.model.hdh;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 包裹信息
 */
@Data
public class PkgInfo implements Serializable {
    /**
     * 包裹号（必填）
     */
    @NotBlank(message = "pkgNo不能为空")
    private String pkgNo;

    /**
     * 子订单状态（必填）
     */
    @NotBlank(message = "orderStatus不能为空")
    private String orderStatus;

    /**
     * 子订单状态code（必填）
     */
    @NotNull(message = "orderStatusCode不能为空")
    private Integer orderStatusCode;

    /**
     * 物流公司（非必填）
     */
    private String expressCompany;

    /**
     * 物流单号（非必填）
     */
    private String expressNum;

    /**
     * 修改时间
     */
    private String modifyTime;

    /**
     * 包裹下商品列表（必填，至少一个商品）
     */
    @NotEmpty(message = "itemList不能为空")
    private List<ItemInfo> itemList;
}