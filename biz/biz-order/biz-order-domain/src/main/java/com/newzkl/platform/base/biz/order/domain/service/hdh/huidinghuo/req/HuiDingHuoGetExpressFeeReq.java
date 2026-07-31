package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.req;


import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 计算运费请求类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuiDingHuoGetExpressFeeReq extends HuiDingHuoBaseReq {

    /**
     * 省份（必填）
     */
    @NotBlank(message = "省份不能为空")
    private String province;

    /**
     * 城市（必填）
     */
    @NotBlank(message = "城市不能为空")
    private String city;

    /**
     * 区（必填）
     */
    @NotBlank(message = "区不能为空")
    private String district;

    /**
     * 详细地址（可选）
     */
    private String address;

    /**
     * 商品信息列表（必填）
     */
    @NotEmpty(message = "商品信息列表不能为空")
    private List<SkuItem> skuList;

    /**
     * 商品信息项
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
        @NotBlank(message = "购买数量不能为空")
        private Integer buyNum;

        /**
         * 全参构造器
         *
         * @param itemId 商品id
         * @param skuId 商品skuId
         * @param channelType 商品渠道类型
         * @param itemCode 商品编码
         * @param buyNum 购买数量
         */
        public SkuItem(String itemId, String skuId, String channelType, String itemCode, Integer buyNum) {
            this.itemId = itemId;
            this.skuId = skuId;
            this.channelType = channelType;
            this.itemCode = itemCode;
            this.buyNum = buyNum;
        }

        /**
         * 无参构造器
         */
        public SkuItem() {
        }

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