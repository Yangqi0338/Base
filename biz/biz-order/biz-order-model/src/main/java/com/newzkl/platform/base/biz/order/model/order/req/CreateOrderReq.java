package com.newzkl.platform.base.biz.order.model.order.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 创建订单请求参数
 *
 * @author sijiwang
 */
@Data
public class CreateOrderReq {

    /**
     * 订单类型：1-C端订单（其他类型可扩展）
     */
    @NotNull(message = "订单类型不能为空")
    @Min(value = 1, message = "订单类型暂仅支持1（C端订单）")
    private Integer orderType;

    /**
     * 收货地址ID（关联地址表主键）
     */
    @NotNull(message = "收货地址ID不能为空")
    @Min(value = 1, message = "收货地址ID必须为正整数")
    private Long shipId;

    /**
     * 订单备注（长度限制200字符）
     */
    @Size(max = 200, message = "订单备注长度不能超过200字符")
    private String remark;

    /**
     * 订单商品明细列表（至少包含一个商品）
     */
    @NotEmpty(message = "订单商品明细不能为空")
    private List<GoodsItem> goodsItems;

    /**
     * 登录账号id
     */
    private Long accountId;

    /**
     * 外部订单号
     */
    private String outOrderNo;

    /**
     * 订单商品明细内部类
     */
    @Data
    public static class GoodsItem {

        /**
         * 铺货表ID（关联铺货表主键）
         */
        @NotNull(message = "铺货表ID不能为空")
        @Min(value = 1, message = "铺货表ID必须为正整数")
        private Long distributionId;

        /**
         * 购买数量（最小1件）
         */
        @NotNull(message = "购买数量不能为空")
        @Min(value = 1, message = "购买数量至少为1件")
        private Integer buyNum;
    }
}