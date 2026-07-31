package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.req;


import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 创建订单请求类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuiDingHuoCreateOrderReq extends HuiDingHuoBaseReq {

    /**
     * 收件人名称（必填）
     */
    @NotBlank(message = "收件人名称不能为空")
    private String name;

    /**
     * 收件人手机（必填）
     */
    @NotBlank(message = "收件人手机不能为空")
    private String phone;

    /**
     * 省份（必填，需与会订货系统省份一致）
     */
    @NotBlank(message = "省份不能为空")
    private String province;

    /**
     * 城市（必填）
     */
    @NotBlank(message = "城市不能为空")
    private String city;

    /**
     * 地区（必填）
     */
    @NotBlank(message = "地区不能为空")
    private String district;

    /**
     * 具体地址（必填）
     */
    @NotBlank(message = "具体地址不能为空")
    private String address;

    /**
     * 身份证号码（可选）
     */
    private String idCardNumber;

    /**
     * 用户自定义订单号（必填，唯一标识）
     */
    @NotBlank(message = "用户自定义订单号不能为空")
    private String userOrderNum;

    /**
     * 商品总金额（必填，用于校验价格变动）
     */
    @NotNull(message = "商品总金额不能为空")
    private BigDecimal price;

    /**
     * 购买商品的备注（可选）
     */
    private String desc;

    /**
     * 购买商品的信息（必填）
     */
    @NotEmpty(message = "商品信息列表不能为空")
    private List<SkuItem> skuList;

    /**
     * 业务渠道（可选）
     */
    private String bizChannel;

    /**
     * 运费总额（可选，用于下单运费校验）
     */
    private BigDecimal expAmount;

    /**
     * 商品信息项（skuList元素）
     */
    @Data
    public static class SkuItem {
        /**
         * 商品id（与itemCode二选一，优先）
         */
        private String itemId;

        /**
         * 商品skuId（与itemCode二选一，优先）
         */
        private String skuId;

        /**
         * 商品渠道类型（与itemCode二选一，优先）
         */
        private String channelType;

        /**
         * 商品编码（与itemId,skuId,channelType二选一）
         */
        private String itemCode;

        /**
         * 购买数量（必填）
         */
        @NotNull(message = "购买数量不能为空")
        private Integer buyNum;

        /**
         * 当前商品sku的总价（可选，用于校验价格变动）
         */
        private BigDecimal price;

        /**
         * 校验：itemId+skuId+channelType 与 itemCode 至少填一项
         */
        @AssertTrue(message = "itemId、skuId、channelType与itemCode至少需填写一项")
        public boolean isParamsValid() {
            boolean hasIdGroup = !(itemId == null || skuId == null || channelType == null);
            boolean hasItemCode = itemCode != null && !itemCode.isEmpty();
            return hasIdGroup || hasItemCode;
        }
    }
}