package com.newzkl.platform.base.biz.order.facade.model.hdh;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 包裹内商品信息
 */
@Data
public class ItemInfo implements Serializable {
    /**
     * 渠道名称（必填）
     */
    @NotBlank(message = "channelName不能为空")
    private String channelName;

    /**
     * 渠道类型（必填）
     */
    @NotBlank(message = "channelType不能为空")
    private String channelType;

    /**
     * 商品id（必填）
     */
    @NotBlank(message = "itemId不能为空")
    private String itemId;

    /**
     * 商品skuId（必填）
     */
    @NotBlank(message = "skuId不能为空")
    private String skuId;

    /**
     * 商品编码（必填）
     */
    @NotBlank(message = "itemCode不能为空")
    private String itemCode;

    /**
     * 商品数量（必填）
     */
    @NotNull(message = "number不能为空")
    private Integer number;
}