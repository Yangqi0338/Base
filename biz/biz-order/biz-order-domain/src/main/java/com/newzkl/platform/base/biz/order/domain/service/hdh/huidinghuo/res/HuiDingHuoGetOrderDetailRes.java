package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.res;

import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * 获取订单详情响应类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuiDingHuoGetOrderDetailRes extends HuiDingHuoBaseRes<HuiDingHuoGetOrderDetailRes.OrderDetail> {

    /**
     * 订单详情类
     */
    @Data
    public static class OrderDetail {
        /**
         * 用户订单号（必填）
         */
        private String userOrderNum;

        /**
         * 系统订单号（必填）
         */
        private String orderNum;

        /**
         * 费用信息（必填）
         */
        private FeeInfo feeInfo;

        /**
         * 子订单列表（必填）
         */
        private List<SubOrder> subOrderList;
    }

    /**
     * 费用信息类
     */
    @Data
    public static class FeeInfo {
        private BigDecimal expAmount; // 运费（可选）
        private BigDecimal tax; // 税费（可选）
        private BigDecimal actDeductAmount; // 活动减免金额（可选）
        private BigDecimal itemAmount; // 商品金额（必填）
        private BigDecimal payAmount; // 支付金额（必填）
    }

    /**
     * 子订单类
     */
    @Data
    public static class SubOrder {
        /**
         * 子订单号（必填）
         */
        private String subOrderNum;

        /**
         * 订单状态（必填）
         */
        private String orderStatus;

        /**
         * 订单商品列表（必填）
         */
        private List<OrderItem> itemList;
    }

    /**
     * 订单商品类
     */
    @Data
    public static class OrderItem {
        /**
         * 商品id（必填）
         */
        private String itemId;

        /**
         * 商品skuId（必填）
         */
        private String skuId;

        /**
         * 渠道类型（必填）
         */
        private SpuEnum.ChannelType spuChannelType;

        /**
         * 商品编码（必填）
         */
        private String itemCode;

        /**
         * 购买数量（必填）
         */
        private Integer buyNum;

        /**
         * 退货数量（必填）
         */
        private Integer refundNum;
    }
}